#
# mkgentoo - install a basic Gentoo setup
#
# Author: Miorel-Lucian Palii <mlpalii@gmail.com>
# License: GNU General Public License version 3 or any later version
#          <http://www.gnu.org/licenses/gpl.html>
# Version: script last updated November 2, 2010
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

function _mkgentoo {

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

GENTOO_MIRROR=http://www.gtlib.gatech.edu/pub/gentoo/

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
wget $GENTOO_MIRROR/releases/x86/autobuilds/$file || { echo "Couldn't download stage information :("; exit 1; }
stage3=`grep -P ^\\s\*[^\\s\#] $file | tail -n1 | sed 's/^\s+//; s/\s*$//'`
rm $file
wget $GENTOO_MIRROR/releases/x86/autobuilds/$stage3 || { echo "Problem downloading stage tarball!"; exit 1; }
tar xjpf $stage3 || { echo "Problem installing stage tarball!"; exit 1; }
rm $stage3

# getting/installing Portage snapshot
wget $GENTOO_MIRROR/snapshots/portage-latest.tar.bz2 || { echo "Problem downloading Portage snapshot!"; exit 1; }
tar xjpf portage-latest.tar.bz2 -C usr || { echo "Problem installing Portage snapshot!"; exit 1; }
rm portage-latest.tar.bz2

# copying DNS info
cp -L /etc/resolv.conf etc

cp $wd/{grub.conf,confset.pl} root

# mounting /proc and /dev
mount -t proc none proc
mount -o bind /dev dev

cp $wd/make.conf etc/make.conf

# the following will be executed within a changed root
cat << CHROOT.SH > chroot.sh
device=$device
tz=America/New_York
hostname=vito
pass=123456
CHROOT.SH
cat << 'CHROOT.SH' >> chroot.sh
rm chroot.sh
env-update
source /etc/profile
emerge --sync --quiet
ln -snf ../usr/portage/profiles/default/linux/x86/10.0/ /etc/make.profile
emerge -1u --noreplace portage
perl -ple 's/^\s*#\s*(en_US)/$1/' -i /etc/locale.gen
locale-gen
cp "/usr/share/zoneinfo/$tz" /etc/localtime
perl -ple 's[^\s*(/dev/(?:cdrom|BOOT|SWAP))\b][#$1]; s[/dev/ROOT][/dev/sda1]' -i /etc/fstab
perl /root/confset.pl /etc/conf.d/hostname HOSTNAME="$hostname"
perl /root/confset.pl /etc/conf.d/clock TIMEZONE="$tz" CLOCK_SYSTOHC="yes"
HOSTNAME=$hostname perl -ple 's/(localhost)/$ENV{HOSTNAME}\t$1/g unless /^\s*#/' -i /etc/hosts
perl /root/confset.pl /etc/conf.d/net 'config_eth0=( "dhcp" )'
rc-update add net.eth0 default

perl /root/confset.pl /etc/conf.d/keymaps SET_WINDOWKEYS="yes"

grep -v rootfs /proc/mounts > /etc/mtab
emerge --keep-going net-misc/dhcpcd sys-fs/e2fsprogs sys-kernel/genkernel sys-kernel/gentoo-sources sys-boot/grub app-admin/logrotate app-admin/syslog-ng sys-process/vixie-cron
rc-update add syslog-ng default
rc-update add vixie-cron default

kernel=`eselect kernel show | perl -nle 'print $1 if /linux-(\S+)/'`
perl -ple 's/KERNEL/$ARG[0]/' $kernel < /root/grub.conf > /boot/grub/grub.conf

grub-install --no-floppy $device
genkernel all
{ echo $pass; echo $pass; } | passwd
emerge eix genlop gentoolkit
eix-update
{ clear; perl -ple 's/\.?\\O//g' < /etc/issue.logo; } > /etc/issue
CHROOT.SH
chroot . /bin/bash chroot.sh

cd /
umount /mnt/gentoo/{proc,dev,.}

}
