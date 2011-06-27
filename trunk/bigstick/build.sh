source bs-main.sh
dir=`mktemp -d --tmpdir=.`
vt="$(bigstick_version_time)"
{
	cat header.txt
	cat bs-main.sh | trim | strip_comment_lines | strip_empties | perl -pe '$_="$1(){\n" if /^(\w+)\s*\(\)\s*\{$/'
} | tr -d '\r' > "$dir/bs-main-$vt.sh"
