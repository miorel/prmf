source bs-main.sh
dir=`mktemp -d --tmpdir=.`
version="201105301900"
cat header.txt | tr -d '\r' > "$dir/bs-main-$version.sh"
cat bs-main.sh | collapse | strip_comment_lines | strip_empties | perl -pe '$_="$1(){" if /^(\w+)\s*\(\)\s*\{$/' >> "$dir/bs-main-$version.sh"
