echo "gen2gen 201106241945 by Miorel-Lucian Palii" >&2

if (( $EUID )); then
	echo "I'm not root, quitting from sadness." >&2
	exit 1
fi

wd="$PWD"
tmp="$(mktemp -d -p .)"
cd "$tmp"
_extract >& /dev/null
source bs-main-201106241845.sh
cd "$wd"
rm -rf "$tmp"

check_connectivity () {
	addy="google.com"; [[ $# -ge 1 ]] && addy=$1
	echo "Checking network connectivity..." >&2
	ping -c3 "$addy" >&2 || wget "$addy" -O - > /dev/null
}

generate_word () {
	len=8; [[ $# -ge 1 ]] && len=$1
	
	tr -dc A-Za-z0-9 < /dev/urandom | # read randoms, deleting the complement of alphanumerics
	head -c $len # pick out the specified number of characters
}

suggest_help () {
	echo "Try \`$0 --help' for more information."
}

report_unrecognized_option () {
	echo "$0: unrecognized option \`$1'"
	suggest_help
}

report_extra_operand () {
	echo "$0: extra operand \`$1'"
	suggest_help
}

countdown () {
	max=10; [[ $# -ge 1 ]] && max="$1"
	i="$max"
	while [[ $i -ge 1 ]]; do
		echo -n "$i "
		sleep 1
		i=$(($i - 1))
	done
	echo 0
}

#(( $(am_i_root "" "I'm not root, quitting from sadness.") )) || exit 1

countdown >&2

check_connectivity gentoo.org
if (( $? )); then
	echo -e "Internet access seems unavailable :(\nThis will be a problem." >&2
	exit 1
fi

disk="/dev/sda"
mountpoint="/mnt/gentoo"
hostname="vito"
timezone="America/New_York"
password="$(generate_word)"

gentoo_default_partition "$disk" || exit 1

rmdir -p "$mountpoint" >& /dev/null
mkdir -p "$mountpoint" >& /dev/null

mount "${disk}3" "$mountpoint"
cd "$mountpoint"
mkdir -p boot
mount "${disk}1" boot

mirror="$(gentoo_random_mirror)"
(( $? )) && exit 1

stage3="$(gentoo_get_stage3 "$mirror")"
(( $? )) && exit 1
tar xjvpf "$stage3" >&2 || exit 1

portage_snapshot="$(gentoo_get_portage_snapshot "$mirror")"
(( $? )) && exit 1
tar xjvf "$portage_snapshot" -C usr >&2 || exit 1

rm -rf "$stage3" "$portage_snapshot"

cp -L /etc/resolv.conf etc

mount -t proc none proc >&2 && mount --rbind /dev dev >&2
(( $? )) && exit 1

cd root
_extract >& /dev/null
cat << VARS.SH > vars.sh
disk="$disk"
timezone="$timezone"
hostname="$hostname"
password="$password"
VARS.SH
cd ..

chroot . /bin/bash /root/chroot.sh

cd /

umount "$mountpoint/"{dev,proc} >&2 || umount -lv "$mountpoint/"{dev,proc} >&2
release_partitions "$disk" || umount -v "$disk"{1,3} >&2 || umount -lv "$disk"{1,3} >&2
swapoff "${disk}2" >& /dev/null

cd "$wd"

echo -e "\nThe root password for your new system is: $password\n"
exit 0
