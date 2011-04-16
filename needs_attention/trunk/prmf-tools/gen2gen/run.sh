wd=$PWD
tmp=$(mktemp -d -p .)
cd "$tmp"
_extract
source util.sh
cd "$wd"
rm -rf "$tmp"

countdown >&2

destination="/dev/sda"
mountpoint="/mnt/gentoo"
password="$(generate_word)"
hostname="vito"
timezone="America/New_York"

gentoo_default_partition "$destination"
rm -rf "$mountpoint"
mkdir -p "$mountpoint"
mount "${destination}3" "$mountpoint"
cd "$mountpoint"
mkdir -p boot
mount "${destination}1" boot

mirror=$(gentoo_random_mirror)

stage3=$(gentoo_get_stage3 "$mirror")
tar xjvpf "$stage3" >&2
portage_snapshot=$(gentoo_get_portage_snapshot "$mirror")
tar xjvpf "$portage_snapshot" -C usr >&2
rm -rf "$stage3" "$portage_snapshot"

cp -L /etc/resolv.conf etc

mount -t proc none proc
mount -o bind /dev dev

cd root
cat << VARS.SH > vars.sh
destination="$destination"
timezone="$timezone"
hostname="$hostname"
password="$password"
VARS.SH
_extract
cd ..

chroot . /bin/bash /root/chroot.sh

umount "$mountpoint/proc"
umount "$mountpoint/dev"

release_partitions "$destination"

echo -e "\nThe root password for your new system is: $password\n"
