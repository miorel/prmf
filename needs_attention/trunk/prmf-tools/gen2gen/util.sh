clean_text () {
	trim | grep -P '^[^\s#]'
}

proc_count () {
	# increment a counter for every line of /proc/cpuinfo having "processor" as the first word, print at end
	awk 'BEGIN { n = 0 }; $1 == "processor" { ++n }; END { print n }' < /proc/cpuinfo
}

random_line () {
	# save lines in an array as they come, print random array element at end
	awk 'BEGIN { srand() }; { l[NR] = $0 }; END { print l[1 + int(rand() * NR)] }'
}

generate_word () {
	len=8; [[ $# -ge 1 ]] && len=$1
	
	tr -dc A-Za-z0-9 < /dev/urandom | # read randoms, deleting the complement of alphanumerics
	head -c $len # pick out the specified number of characters
}

gentoo_mirrors () {
	url="http://www.gentoo.org/main/en/mirrors3.xml"; [[ $# -ge 1 ]] && url=$1

	wget "$url" -O - | # print the mirror list XML to stdout
	chomp | # make everything one line
	sed -r 's/<uri(\s[^>]*)?>([^<]+)<\/uri>/\n<uri> \2\n/g' | # isolate the URIs
	awk '$1 == "<uri>" && $2 ~ /^(ftp|http)/ { print $2 }' # print them
}

gentoo_random_mirror () {
	gentoo_mirrors "$@" | random_line
}

gentoo_get_portage_snapshot () {
	mirror=$1
	if [[ $# -lt 1 ]]; then
		echo "No mirror specified, will pick a random one." >&2
		mirror=$(gentoo_random_mirror)
	fi
	
	file="/snapshots/portage-latest.tar.bz2"; [[ $# -ge 2 ]] && file=$2

	get_file_checking_md5 "$mirror/$file"
}

gentoo_get_stage3 () {
	mirror=$1
	if [[ $# -lt 1 ]]; then
		echo "No mirror specified, will pick a random one." >&2
		mirror=$(gentoo_random_mirror)
	fi

	file_info="/releases/x86/autobuilds/latest-stage3-i686.txt"; [[ $# -ge 2 ]] && file_info=$2
	file="$(dirname "$file_info")/$(wget "$mirror/$file_info" -O - | clean_text)"
	if (( $? )); then
		echo "Couldn't download stage information :(" >&2
		return 1
	fi

	get_file_checking_md5 "$mirror/$file"
}

get_file_checking_md5 () {
	url=$1
	checksum=$2

	if [[ $# -lt 2 ]]; then
		success=1
		checksum=$(wget "$url.md5sum" -O -)
		ec=$?
		if [[ $ec -eq 8 ]]; then
			# server error, let's try a .DIGESTS file
			checksum=$(wget "$url.DIGESTS" -O -)
			(( $? )) && success=0
		elif (( $ec )); then
			success=0
		fi

		if (( ! $success )); then
			echo "Failed to retrieve MD5 sum :(" >&2
			return 1
		fi

		checksum=$(clean_text <<< "$checksum" | first_line | first_word)
	fi

	file=$(basename "$url")

	do_get=1

	if [[ -e "$file" ]]; then
		echo "$file seems to exist already, will check the MD5 sum and maybe avoid redownloading." >&2
		if (( $(check_md5 "$file" "$checksum") )); then
			do_get=0
		else
			echo "Redownloading!" >&2
		fi
	fi
	
	if (( $do_get )); then
		wget "$url" -O "$file"
		(( $(check_md5 "$file" "$checksum") )) || return 1
	fi

	echo "$file"
}

check_md5 () {
	md5sum -c - <<< "$2  $1" >&2
	success_if_zero $?
}

am_i_root () {
	success_if_zero $EUID
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

success_if_zero () {
	echo $(( ! $1 ))
	return $1
}

success_if_nonzero () {
	success_if_zero $(( ! $1 ))
}

gentoo_add_service () {
	service=$1
	level=default; [[ $# -ge 2 ]] && level=$2
	rc-update add "$service" "$level"
}

list_partitions () {
	assert_can_work_with_partitions || return 1
	fdisk -cul "$1" 2>&1 | awk 'BEGIN { PR = 0 }; PR; $1 == "Device" && $2 == "Boot" { PR = 1 }' | first_word
}

list_mounts () {
	df -a --sync 2>&1 | awk 'NR > 1 && $1 != "none"' | first_word | sort | uniq
}

is_mounted () {
	ret=0

	old_ifs=$IFS
	IFS=$'\n'

	for m in $(list_mounts); do
		if [[ "$m" == "$1" ]]; then
			ret=1
			break
		fi
	done

	IFS=old_ifs
	
	success_if_nonzero $ret
}

release_partitions () {
	assert_can_work_with_partitions || return 1

	disk="$1"
	ret=0

	old_ifs="$IFS"
	IFS=$'\n'

	wd="$PWD"
	cd /
	progress=1
	while (( $progress )); do
		progress=0
		for p in $(list_partitions "$disk"); do
			{ umount "$p" || swapoff "$p"; } >& /dev/null && progress=1
		done
	done
	cd "$wd"

	for p in $(list_partitions "$disk"); do
		if (( $(is_mounted "$p") )); then
			echo "$p remains mounted despite my best efforts :/" >&2
			ret=1
		fi
	done

	IFS="$old_ifs"

	return $ret
}

delete_partitions () {
	assert_can_work_with_partitions || return 1

	disk="$1"

	release_partitions "$disk" &&
	{ for p in {1..4}; do echo -e "d\n$p"; done; echo w; } | fdisk -cu "$disk" >& /dev/null

	partprobe "$disk"

	(( ! $(list_partitions "$disk" | line_count) )) &&
	(( ! $? )) && echo "Partition table successfully deleted." >&2
}

gentoo_default_partition () {
	assert_can_work_with_partitions || return 1

	disk="$1"

	delete_partitions "$disk" &&
	{
		echo -e "n\np\n1\n\n+32M\na\n1";
		echo -e "n\np\n2\n\n+2G\nt\n2\n82";
		echo -e "n\np\n3\n\n";
		echo w;
	} | fdisk -cu "$disk" >& /dev/null &&
	echo "New partition table successfully written!" >&2 &&
	fdisk -cul "$disk" && partprobe "$disk"
	if (( $? )); then
		echo "There was a problem preparing the partition table." >&2
		return 1
	fi

	while [[ ! -e "${disk}2" ]]; do
		echo "Can't see ${disk}2 just yet, sleeping for a sec." >&2
		sleep 1
	done

	make_swap "${disk}2"
	make_ext2 "${disk}1"
	make_ext3 "${disk}3"
}

assert_can_work_with_partitions () {
	if (( ! $(am_i_root) )); then
		echo "Root privileges are needed to work with partitions!" >&2
		return 1
	fi
}

make_swap () {
	assert_can_work_with_partitions || return 1
	{ mkswap "$1" && swapon "$1"; } >&2
}

make_ext2 () {
	assert_can_work_with_partitions || return 1
	{ mkfs.ext2 "$1" && tune2fs -c 10 -i 30 "$1"; } >&2
}

make_ext3 () {
	assert_can_work_with_partitions || return 1
	{ mkfs.ext3 "$1" && tune2fs -c 10 -i 30 "$1"; } >&2
}

gentoo_update () {
	emerge -1u --noreplace --keep-going "$1"
}

countdown () {
	max=10; [[ $# -ge 1 ]] && max="$1"
	i="$max"
	while [[ $i -ge 1 ]]; do
		echo "$i " | chomp
		sleep 1
		i=$(($i - 1))
	done
}
