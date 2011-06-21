source bs-main.sh
dir=`mktemp -d --tmpdir=.`
version="201106202315"
cat header.txt | tr -d '\r' > "$dir/bs-main-$version.sh"
cat bs-main.sh | tr -d '\r' | trim | strip_comment_lines | strip_empties | perl -ple 'BEGIN{$v=shift}s/^(BIGSTICK_VERSION=).*/$1"$v"/' "$version" | perl -pe '$_="$1(){\n" if /^(\w+)\s*\(\)\s*\{$/' >> "$dir/bs-main-$version.sh"
