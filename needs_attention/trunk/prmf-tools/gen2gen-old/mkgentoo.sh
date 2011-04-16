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

else # it's a directory
	cd $destination
fi

# the following will be executed within a changed root
cat << CHROOT.SH > root/chroot.sh
dest_is_block=$dest_is_block
CHROOT.SH
cat << 'CHROOT.SH' >> root/chroot.sh



if (( $dest_is_block )); then
	grub-install --no-floppy $destination
	genkernel all
fi

rm /root/{chroot.sh,grub.conf,confset.pl}
CHROOT.SH

echo -e "\nThe root password for your new system is: $password\n"

}

