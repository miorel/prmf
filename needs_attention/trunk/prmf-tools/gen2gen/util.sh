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

gentoo_add_service () {
	service=$1
	level=default; [[ $# -ge 2 ]] && level=$2
	rc-update add "$service" "$level"
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
