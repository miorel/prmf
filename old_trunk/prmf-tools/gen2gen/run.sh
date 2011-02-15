wd=$PWD
tmp=$(mktemp -d -p .)
cd "$tmp"
_extract
source util.sh
cd "$wd"
rm -rf "$tmp"

destination="/dev/sda"
mountpoint="/mnt/gentoo"
password=$(generate_word)
hostname="vito"
timezone="America/New_York"

gentoo_default_partition "$destination"
rm -rf "$mountpoint"
mkdir -p "$mountpoint"
mount "${destination}3" "$mountpoint"
cd "$mountpoint"
mkdir boot
mount "${destination}1" boot

mirror=$(gentoo_random_mirror)

stage3=$(gentoo_get_stage3 "$mirror")
tar xjvpf "$stage3"
portage_snapshot=$(gentoo_get_portage_snapshot "$mirror")
tar xjvpf "$portage_snapshot" -C usr

cp -L /etc/resolv.conf etc

mount -t proc none proc
mount -o bind /dev dev

cd root
_extract
cat << 'CHROOT.SH' >> chroot.sh
cd /root
source util.sh
rm chroot.sh util.sh
env-update
source /etc/profile
emerge --sync
gentoo_update sys-apps/portage
CHROOT.SH
cd ..

chroot . /bin/bash /root/chroot.sh

umount "$mountpoint/proc"
umount "$mountpoint/dev"

release_partitions "$destination"
