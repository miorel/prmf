#
# mkgentoo - install a basic Gentoo setup
#
# Author: Miorel-Lucian Palii <mlpalii@gmail.com>
# License: GNU General Public License version 3 or any later version
#          <http://www.gnu.org/licenses/gpl.html>
# Version: script last updated November 5, 2010
#
# To use, boot up the destination system from your favorite installation medium (Gentoo's minimal install image is fine); then transfer the script (or just redownload it if possible) and run it.
#
# The script makes at least the following assumptions:
#  * The destination device is /dev/sda (the first SATA device).
#  * You're happy with your whole system on a single partition of type ext4.
#  * Internet access is available.
#  * The architecture is x86 (32-bit).
# Some of these are straightforward to change, some are not.
#
# This code was written for lazy people. There is some basic error-checking but in general you should have no expectations of robustness. The script works best in a virtual machine (which is how I developed it and also what the original intended platform was).
#

_mkgentoo () {

# be nice, warn people
cat << '_WARN_'
WARNING!!! You're about to install a new operating system.
This process will delete any data you might currently have on this machine.

Last chance to cancel! Press Ctrl + C now.
_WARN_

# countdown
for i in {10..1}; do
	printf "%d " $i
	sleep 1
done
echo "Liftoff!"

mirror=`random_gentoo_mirror`

device=/dev/sda
[[ -e $device ]] || { echo "Uhh... I can't seem to find $device anywhere."; exit 1; }
# killing swap, unmounting the destination device if necessary
swapoff -a
umount ${device}*

# deleting partition table
echo 'd 1 d 2 d 3 d 4 w' | sed 's/ /\n/g' | fdisk $device
[[ -e ${device}1 ]] && { echo "There was a problem deleting the partitions!"; exit 1; }

# creating partition table
echo 'n p 1   a 1 w' | sed 's/ /\n/g' | fdisk $device
[[ -e ${device}1 ]] || {
	echo "Can't see ${device}1 yet, sleeping for a bit.";
	sleep 5;
	[[ -e ${device}1 ]] || { echo "Still can't see ${device}1 and I don't feel like waiting anymore."; exit 1; };
}

# creating filesystem
mkfs.ext3 ${device}1
tune2fs -c 10 -i 30 ${device}1

# mounting root partition
mountpoint=/mnt/gentoo
rm -rf $mountpoint
mkdir -p $mountpoint
mount ${device}1 $mountpoint

# save working directory then change directory
wd=`pwd`
cd $mountpoint

# getting/installing stage tarball
file=latest-stage3.txt
wget $mirror/releases/x86/autobuilds/$file || { echo "Couldn't download stage information :("; exit 1; }
stage3=`grep -P '^\s*[^\s#]' $file | tail -n1 | sed 's/^\s+//; s/\s*$//'`
rm $file
wget $mirror/releases/x86/autobuilds/$stage3 || { echo "Problem downloading stage tarball!"; exit 1; }
stage3=`basename "$stage3"`
tar xjpf $stage3 || { echo "Problem installing stage tarball!"; exit 1; }
rm $stage3

# getting/installing Portage snapshot
file=`gentoo_get_portage_snapshot`
if [[ $? -ne 0 ]]; then
	echo "Problem downloading Portage snapshot!"
	exit 1
fi
tar xjpf $file -C usr || { echo "Problem installing Portage snapshot!"; exit 1; }
rm $file

# copying DNS info
cp -L /etc/resolv.conf etc

cp $wd/{grub.conf,confset.pl} root

# mounting /proc and /dev
mount -t proc none proc
mount -o bind /dev dev

cp $wd/make.conf etc/make.conf

pass=`generate_word`

# the following will be executed within a changed root
cat << CHROOT.SH > root/chroot.sh
device=$device
tz=America/New_York
hostname=vito
pass=$pass
CHROOT.SH
cat << 'CHROOT.SH' >> root/chroot.sh
env-update
source /etc/profile
emerge --sync -q
ln -snf ../usr/portage/profiles/default/linux/x86/10.0/ /etc/make.profile
emerge -1u --noreplace portage
perl -ple 's/^\s*#\s*(en_US)/$1/' -i /etc/locale.gen
locale-gen
cp "/usr/share/zoneinfo/$tz" /etc/localtime
device=$device perl -ple 's[^\s*(/dev/(?:cdrom|BOOT|SWAP))\b][#$1]; s[/dev/ROOT][$ENV{device}1]' -i /etc/fstab
perl /root/confset.pl /etc/conf.d/hostname HOSTNAME="$hostname"
perl /root/confset.pl /etc/conf.d/clock TIMEZONE="$tz" CLOCK_SYSTOHC="yes"
hostname=$hostname perl -ple 's/(localhost)/$ENV{hostname}\t$1/g unless /^\s*#/' -i /etc/hosts
perl /root/confset.pl /etc/conf.d/net 'config_eth0=( "dhcp" )'
perl /root/confset.pl /etc/conf.d/keymaps SET_WINDOWKEYS="yes"

grep -v rootfs /proc/mounts > /etc/mtab
emerge --keep-going net-misc/dhcpcd sys-fs/e2fsprogs sys-kernel/genkernel sys-kernel/gentoo-sources sys-boot/grub app-admin/logrotate app-admin/syslog-ng sys-process/vixie-cron
rc-update add net.eth0 default
rc-update add syslog-ng default
rc-update add vixie-cron default

kernel=`eselect kernel show | perl -nle 'print $1 if /linux-(\S+)/'`
perl -ple 'BEGIN{$k=shift} s/KERNEL/$k/' $kernel < /root/grub.conf > /boot/grub/grub.conf

grub-install --no-floppy $device
genkernel all
echo -e "$pass\n$pass" | passwd
passwd -e root
emerge --noreplace --keep-going app-portage/{eix,genlop,gentoolkit}
eix-update
{ clear; perl -ple 's/\.?\\O//g' < /etc/issue.logo; } > /etc/issue
CHROOT.SH
chroot . /bin/bash /root/chroot.sh

cd /
umount /mnt/gentoo/{proc,dev,.}

cat << _PASS_

The root password for your new system is: $pass

_PASS_

}

gentoo_add_service () {
	service=$1
	level=default; [[ $# -ge 2 ]] && level=$2
	rc-update add "$service" "$level"
}
