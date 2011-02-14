#
# mkgentoo - install a basic Gentoo setup
#
# Author: Miorel-Lucian Palii <mlpalii@gmail.com>
# License: GNU General Public License version 3 or any later version
#          <http://www.gnu.org/licenses/gpl.html>
#
# To use, boot up the destination system from your favorite installation medium (Gentoo's minimal install image is fine); then transfer the script (or just redownload it if possible) and run it.
#
# The script makes at least the following assumptions:
#  * The destination device is /dev/sda (the first SATA device).
#  * You're happy with your whole system on a single partition of type ext3.
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

destination=$1

dest_is_block=0
if ! [ -e $destination ]; then
	mkdir -p $destination
fi
if [ -b $destination ]; then
	dest_is_block=1
else
	if ! [ -d $destination ]; then
		echo "Neither a directory nor a block device!"
		exit 1
	fi
fi


# save working directory
wd=$PWD

[ -e $destination ] || { echo "Uhh... I can't seem to find $destination anywhere."; exit 1; }

if (( $dest_is_block )); then
	# killing swap, unmounting the destination device if necessary
	swapoff -a
	umount ${destination}*

	# deleting partition table
	echo 'd 1 d 2 d 3 d 4 w' | sed 's/ /\n/g' | fdisk $destination
	[ -e ${destination}1 ] && { echo "There was a problem deleting the partitions!"; exit 1; }

	# creating partition table
	echo 'n p 1   a 1 w' | sed 's/ /\n/g' | fdisk $destination
	[ -e ${destination}1 ] || {
		echo "Can't see ${destination}1 yet, sleeping for a bit.";
		sleep 5;
		[ -e ${destination}1 ] || { echo "Still can't see ${destination}1 and I don't feel like waiting anymore."; exit 1; };
	}

	# creating filesystem
	mkfs.ext3 ${destination}1
	tune2fs -c 10 -i 30 ${destination}1

	# mounting root partition
	mountpoint=/mnt/gentoo
	rm -rf $mountpoint
	mkdir -p $mountpoint
	mount ${destination}1 $mountpoint

	cd $mountpoint

else # it's a directory
	cd $destination
fi

mirror=`random_gentoo_mirror`

# getting/installing stage tarball
stage3=`wget $mirror/releases/x86/autobuilds/latest-stage3.txt -O - | grep -P '^\s*[^\s#]' | tail -n1 | sed 's/^\s+//; s/\s*$//'`
if [ $? -ne 0 ]; then
	echo "Couldn't download stage information :("
	exit 1
fi
wget $mirror/releases/x86/autobuilds/$stage3 || { echo "Problem downloading stage tarball!"; exit 1; }
stage3=`basename "$stage3"`
tar xjpf $stage3 || { echo "Problem installing stage tarball!"; exit 1; }
rm $stage3

# getting/installing Portage snapshot
file=`gentoo_get_portage_snapshot $mirror`
if [ $? -ne 0 ]; then
	echo "Problem downloading Portage snapshot!"
	exit 1
fi
tar xjpf $file -C usr || { echo "Problem installing Portage snapshot!"; exit 1; }
rm $file

# copying DNS info
cp -L /etc/resolv.conf etc

cp $wd/grub.conf root
cp $wd/confset.pl root
cp $wd/make.conf etc

# mounting /proc and /dev
mount -t proc none proc
mount -o bind /dev dev

password=`generate_word`
hostname=vito
timezone=America/New_York

# the following will be executed within a changed root
cat << CHROOT.SH > root/chroot.sh
dest_is_block=$dest_is_block
destination=$destination
timezone=$timezone
hostname=$hostname
password=$password
CHROOT.SH
cat << 'CHROOT.SH' >> root/chroot.sh
cd /root
env-update
source /etc/profile
emerge --sync -q
ln -snf ../usr/portage/profiles/default/linux/x86/10.0/ /etc/make.profile
emerge -1u --noreplace sys-apps/portage

perl -ple 's/^\s*#\s*(en_US)/$1/' -i /etc/locale.gen
locale-gen
cp "/usr/share/zoneinfo/$timezone" /etc/localtime
root=${destination}1 perl -ple 's[^\s*(/dev/(?:cdrom|BOOT|SWAP))\b][#$1]; s[/dev/ROOT][$ENV{root}]' -i /etc/fstab
perl confset.pl /etc/conf.d/hostname HOSTNAME="\"$hostname\""
perl confset.pl /etc/conf.d/clock TIMEZONE="\"$timezone\"" 'CLOCK_SYSTOHC="yes"'
hostname=$hostname perl -ple 's/(localhost)/$ENV{hostname}\t$1/g unless /^\s*#/' -i /etc/hosts
perl confset.pl /etc/conf.d/net 'config_eth0=( "dhcp" )'
perl confset.pl /etc/conf.d/keymaps 'SET_WINDOWKEYS="yes"'
{ clear; perl -ple 's/\.?\\O//g' < /etc/issue.logo; } > /etc/issue

grep -v rootfs /proc/mounts > /etc/mtab
emerge -u --noreplace --keep-going net-misc/dhcpcd sys-fs/e2fsprogs sys-kernel/genkernel sys-kernel/gentoo-sources sys-boot/grub app-admin/logrotate app-admin/syslog-ng sys-process/vixie-cron
rc-update add net.eth0 default
rc-update add syslog-ng default
rc-update add vixie-cron default

kernel=`eselect kernel show | perl -nle 'print $1 if /linux-(\S+)/'`
perl -ple 'BEGIN{$k=shift; $r=shift} s/KERNEL/$k/g; s/ROOT/$r/g' $kernel ${destination}1 < /root/grub.conf > /boot/grub/grub.conf

if (( $dest_is_block )); then
	grub-install --no-floppy $destination
	genkernel all
fi

echo -e "$password\n$password" | passwd
passwd -e root
emerge -u --noreplace --keep-going app-portage/{eix,genlop,gentoolkit}
eix-update

rm /root/{chroot.sh,grub.conf,confset.pl}
CHROOT.SH
chroot . /bin/bash /root/chroot.sh

cd /
umount /mnt/gentoo/{proc,dev,.}

echo -e "\nThe root password for your new system is: $password\n"

}

gentoo_add_service () {
	service=$1
	level=default; [[ $# -ge 2 ]] && level=$2
	rc-update add "$service" "$level"
}
