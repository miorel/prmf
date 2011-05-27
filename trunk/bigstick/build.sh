source bs-main.sh
dir=`mktemp -d --tmpdir=.`
version="201105262100"
cat bs-main.sh | collapse | strip_empties > "$dir/bs-main-$version.sh"
