echo "gen2gen 201106202315 by Miorel-Lucian Palii" >&2
wd=$PWD
tmp=$(mktemp -d -p .)
cd "$tmp"
_extract >& /dev/null
source bs-main-201106202315.sh
cd "$wd"
rm -rf "$tmp"

check_connectivity () {
	addy="google.com"; [[ $# -ge 1 ]] && addy=$1
	echo "Checking network connectivity..." >&2
	ping -c3 "$addy" >&2 || wget "$addy" -O - > /dev/null
}

(( $(am_i_root "" "I'm not root, quitting from sadness.") )) || exit 1

check_connectivity gentoo.org
if (( $? )); then
	echo -e "Internet access seems unavailable :(\nThis will be a problem." >&2
	exit 1
fi


disk="/dev/sda"
mountpoint="/mnt/gentoo"

gentoo_default_partition "$disk" || exit 1

rmdir -p "$mountpoint" >& /dev/null
mkdir -p "$mountpoint" >& /dev/null

mount "${disk}3" "$mountpoint"
cd "$mountpoint"
mkdir -p boot
mount "${disk}1" boot

mirror=$(gentoo_random_mirror)
(( $? )) && exit 1

stage3=$(gentoo_get_stage3 "$mirror")
(( $? )) && exit 1
tar xjvpf "$stage3" >&2 || exit 1

portage_snapshot=$(gentoo_get_portage_snapshot "$mirror")
(( $? )) && exit 1
tar xjvpf "$portage_snapshot" -C usr >&2 || exit 1

rm -rf "$stage3" "$portage_snapshot"

cp -L /etc/resolv.conf etc

mount -t proc none proc >&2 && mount -o bind /dev dev >&2
(( $? )) && exit 1

cd root
_extract >& /dev/null
cat make.conf > /etc/make.conf
cd ..

chroot . /bin/bash /root/chroot.sh

umount "$mountpoint/"{dev,proc} >&2
release_partitions "$disk"

exit 0
