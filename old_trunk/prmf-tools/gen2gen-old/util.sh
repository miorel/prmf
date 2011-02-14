chomp () {
	# set the output record separator to the empty string
	awk 'BEGIN { ORS = "" }; 1'
}

proc_count () {
	# increment a counter for every line of /proc/cpuinfo having "processor" as the first word, print at end
	awk 'BEGIN { n = 0 }; $1 == "processor" { ++n }; END { print n }' < /proc/cpuinfo
}

random_line () {
	# save lines in an array as they come, print random array element at end
	awk 'BEGIN { srand() }; { l[NR] = $0 }; END { print l[1 + int(rand() * NR)] }'
}

gentoo_mirror_list () {
	url=http://www.gentoo.org/main/en/mirrors3.xml; [[ $# -ge 1 ]] && url=$1
	wget $url -O - | chomp | sed -r 's/<uri(\s[^>]*)?>([^<]+)<\/uri>/\n<uri> \2\n/g' | awk '$1 == "<uri>" && $2 ~ /^(ftp|http)/ { print $2 }'
}

random_gentoo_mirror () {
	gentoo_mirror_list "$@" | random_line
}

generate_word () {
	len=8; [ $# -ge 1 ] && len=$1
	tr -dc A-Za-z0-9 < /dev/urandom | head -c $len
}

gentoo_get_portage_snapshot () {
	mirror=$1
	if ! [ $# -ge 1 ]; then
		echo "No mirror specified, will pick a random one." >&2
		mirror=`random_gentoo_mirror`
	fi
	
	file="/snapshots/portage-latest.tar.bz2"; [[ $# -ge 2 ]] && file=$2

	url="$mirror/$file"
	file=`basename "$url"`

	do_get=1

	checksum=`wget "$url.md5sum" -O - | cut -f1 -d" "`
	if [ $? -ne 0 ]; then
		echo "Failed to retrieve the Portage snapshot MD5 sum :(" >&2
		return 1
	fi 

	if [ -e $file ]; then
		echo "$file seems to exist already, will check the MD5 sum and maybe avoid redownloading!" >&2
		md5sum -c - <<< "$checksum  $file" >&2
		if [ $? -eq 0 ]; then
			do_get=0
		fi
	fi
	
	if (( $do_get )); then
		wget "$url" -O "$file" || return 1
		md5sum -c - <<< "$checksum  $file" >&2 || return 1
	fi

	echo "$file"
}

am_i_root () {
	ret=0; [ $EUID -eq 0 ] && ret=1
	echo $ret
}
