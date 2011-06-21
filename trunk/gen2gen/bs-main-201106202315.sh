#
# bigstick - A tool for better bash-ing!
# Copyright (C) 2011 Miorel-Lucian Palii <mlpalii@gmail.com>
#
# This library is free software: you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later
# version.
#
# This library is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
# FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
# details: <http://www.gnu.org/licenses/>
#
BIGSTICK_VERSION="201106202315"
SED_WHITESPACE_REGEX='[ \f\n\r\t\v]'
chomp(){
tr -d '\r\n'
}
trim(){
sed "s/^$SED_WHITESPACE_REGEX\\+//; s/$SED_WHITESPACE_REGEX\\+\$//"
}
strip_empties(){
awk NF
}
strip_comment_lines(){
grep -vE '^[[:space:]]*#'
}
squeeze(){
trim | sed "s/$SED_WHITESPACE_REGEX\\+/ /g"
}
collapse(){
squeeze | strip_empties
}
crush(){
squeeze | strip_empties | strip_comment_lines
}
line_count(){
awk 'END {ORS = ""; print NR}'
}
first_line(){
head -n1
}
last_line(){
tail -n1
}
random_line(){
awk 'BEGIN {srand()}; {l[NR] = $0}; END {print l[1 + int(rand() * NR)]}'
}
first_word(){
awk '{print $1}'
}
last_word(){
awk '{print $NF}'
}
success_if_zero(){
good=$(( !($1) ))
echo $good
if (( $good )); then
[ "$2" ] && echo "$2" >&2
return 0
else
[ "$3" ] && echo "$3" >&2
return 1
fi
}
success_if_nonzero(){
success_if_zero $(( !($1) )) "$2" "$3"
}
am_i_root(){
success_if_zero "$EUID" "$1" "$2"
}
can_i_work_with_partitions(){
fm="Root privileges are needed to work with partitions!"; [[ $# -ge 2 ]] && fm=$2
success_if_nonzero $(am_i_root) "$1" "$fm"
}
print_gpl_footer(){
cat << 'EOF'
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.
EOF
}
make_swap(){
(( $(can_i_work_with_partitions) & $(release_partition "$1") )) || return 1
mkswap "$1" >&2 && swapon "$1" >&2
}
make_ext2(){
(( $(can_i_work_with_partitions) & $(release_partition "$1") )) || return 1
mkfs.ext2 "$1" >&2 && tune2fs -c 10 -i 30 "$1" >&2
}
make_ext3(){
(( $(can_i_work_with_partitions) & $(release_partition "$1") )) || return 1
mkfs.ext3 "$1" >&2 && tune2fs -c 10 -i 30 "$1" >&2
}
list_partitions(){
(( $(can_i_work_with_partitions) )) || return 1
fdisk -cul "$1" 2>&1 | awk 'BEGIN {p = 0}; p; $1 == "Device" && $2 == "Boot" {p = 1}' | first_word | sort | uniq
}
list_mounts(){
df -a --sync 2>&1 | awk 'NR > 1 && $1 != "none"' | first_word | sort | uniq
}
is_mounted(){
ret=1
old_ifs=$IFS
IFS=$'\n'
for m in $(list_mounts); do
if [[ "$m" == "$1" ]]; then
ret=0
break
fi
done
IFS=$old_ifs
success_if_zero $ret
}
delete_partitions(){
(( $(can_i_work_with_partitions) )) || return 1
disk="$1"
release_partitions "$disk" && echo "Deleting any partitions on $disk..." >&2 &&
{ for p in {1..4}; do echo -e "d\n$p"; done; echo w; } | fdisk -cu "$disk" >& /dev/null
partprobe "$disk" > /dev/null
(( ! $(list_partitions "$disk" | line_count) )) &&
(( ! $? )) &&
echo "Partition table on $disk cleared." >&2
}
_do_release_partition(){
umount "$1" || swapoff "$1"
}
release_partition(){
(( $(can_i_work_with_partitions) )) || return 1
partition=$1
ret=0
wd=$PWD
cd /
progress=1
while (( $progress )); do
progress=0
_do_release_partition "$partition" >& /dev/null && progress=1
done
cd "$wd"
success_if_zero $(is_mounted "$partition") "" "$partition remains mounted despite my best efforts :/"
}
release_partitions(){
(( $(can_i_work_with_partitions) )) || return 1
disk=$1
ret=0
echo "Releasing partitions on $disk..." >&2
old_ifs=$IFS
IFS=$'\n'
wd=$PWD
cd /
progress=1
while (( $progress )); do
progress=0
for p in $(list_partitions "$disk"); do
_do_release_partition "$p" >& /dev/null && progress=1
done
done
cd "$wd"
for p in $(list_partitions "$disk"); do
if (( $(is_mounted "$p") )); then
echo "$p remains mounted despite my best efforts :/" >&2
ret=1
break
fi
done
IFS=$old_ifs
return $ret
}
sleep_until_exists(){
while [[ ! -e "$1" ]]; do
echo "Can't see $1 just yet, sleeping for a sec." >&2
sleep 1
done
return 0
}
gentoo_default_partition(){
(( $(can_i_work_with_partitions) )) || return 1
disk=$1
delete_partitions "$disk" &&
{
echo -e "n\np\n1\n\n+32M\na\n1"
echo -e "n\np\n2\n\n+2G\nt\n2\n82"
echo -e "n\np\n3\n\n"
echo w
} | fdisk -cu "$disk" >& /dev/null
if (( $? )); then
echo "There was a problem preparing the partition table." >&2
return 1
fi
echo "New partition table successfully written!" >&2 &&
fdisk -cul "$disk" >&2
partprobe "$disk" > /dev/null
sleep_until_exists "${disk}2" && make_swap "${disk}2" &&
sleep_until_exists "${disk}1" && make_ext2 "${disk}1" &&
sleep_until_exists "${disk}3" && make_ext3 "${disk}3"
}
check_md5(){
md5sum -c - <<< "$2  $1" >&2
success_if_zero $?
}
get_file_checking_md5(){
url=$1
checksum=$2
if [[ $# -lt 2 ]]; then
success=1
checksum=$(wget "$url.md5sum" -O -)
ec=$?
if [[ $ec -eq 8 ]]; then
checksum=$(wget "$url.DIGESTS" -O -)
(( $? )) && success=0
elif (( $ec )); then
success=0
fi
if (( ! $success )); then
echo "Failed to retrieve MD5 sum :(" >&2
return 1
fi
checksum=$(crush <<< "$checksum" | first_line | first_word)
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
gentoo_mirrors(){
url="http://www.gentoo.org/main/en/mirrors3.xml"; [[ $# -ge 1 ]] && url=$1
wget "$url" -O - | # print the mirror list XML to stdout
chomp | # make everything one line
sed -r 's/<uri(\s[^>]*)?>([^<]+)<\/uri>/\n<uri> \2\n/g' | # isolate the URIs
awk '$1 == "<uri>" && $2 ~ /^(ftp|http)/ {print $2}' # print them
}
gentoo_random_mirror(){
gentoo_mirrors "$@" | random_line
}
gentoo_get_portage_snapshot(){
mirror=$1
if [[ $# -lt 1 ]]; then
echo "No mirror specified, will pick a random one." >&2
mirror=$(gentoo_random_mirror)
fi
file="/snapshots/portage-latest.tar.bz2"; [[ $# -ge 2 ]] && file=$2
get_file_checking_md5 "$mirror/$file"
}
gentoo_get_stage3(){
mirror=$1
if [[ $# -lt 1 ]]; then
echo "No mirror specified, will pick a random one." >&2
mirror=$(gentoo_random_mirror)
fi
file_info="/releases/x86/autobuilds/latest-stage3-i686.txt"; [[ $# -ge 2 ]] && file_info=$2
file="$(dirname "$file_info")/$(wget "$mirror/$file_info" -O - | crush)"
if (( $? )); then
echo "Couldn't download stage information :(" >&2
return 1
fi
get_file_checking_md5 "$mirror/$file"
}
proc_count(){
awk 'BEGIN {n = 0}; $1 == "processor" {++n}; END {print n}' < /proc/cpuinfo
}
