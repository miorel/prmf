run () {
	destination=hi

	parse_opts=1
	operand=()
	opt=()
	for arg in $@; do
		parsed=0
		if (( $parse_opts )); then
			case "$arg" in
			--conf)
				parsed=1
				opt[${#opt[*]}]="conf"
			;;
			--version|-v*)
				parsed=1
				print_version
				return 0
			;;
			--help|-h*|-\?*)
				parsed=1
				print_help
				return 0
			;;
			--)
				parsed=1
				parse_opts=0
			;;
			-?*)
				parsed=1
				[[ $arg == --* ]] || arg=${arg:0:2}
				unrecognized_option "$arg" >&2
				return 1
			;;
			esac
		fi
		(( $parsed )) || operand[${#operand[*]}]=$arg
	done

	j=0
	for i in `seq 0 $(( ${#opt[*]} - 1 ))`; do
		arg=${opt[i]}
		case "$arg" in
		conf)
			exec 3<&0
			file=${operand[$j]}
			if [ "$file" != "-" ]; then
				exec < "$file"
			fi
			while read line; do
				case "$line" in
				destination=*)
					key=${line%%=*}
					val=${line#*=}
					eval "$key=\$val"
				;;
				esac
			done
			exec 0<&3 3<&-
			j=$(( $j + 1 ))
		;;
		esac
	done

	if [ ${#operand[*]} -gt $j ]; then
		extra_operand ${operand[$j]}
		return 1
	fi

	echo "Destination = $destination"

	wd=$PWD
	tmp=`mktemp -d -p .`
	cd $tmp
	_extract
	source util.sh
	source mkgentoo.sh
	_mkgentoo $destination
	ret=$?
	cd $wd
	rm -rf $tmp
	return $ret
}

print_version () {
	cat << EOF
gen2gen 0.0.1 (November 9, 2010)
Copyright (C) 2010 Miorel-Lucian Palii
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.
EOF
}

print_help () {
	cat << EOF
Usage: $0 [OPTION]...
Install a basic Gentoo system.

      --conf FILE  read configuration from FILE (may be \`-' for stdin)
      --           suppress further option parsing (in case you need to pass
                   operands that start with a hyphen without confusing them for
                   options)
  -h, --help, -?   display this help and exit
  -v, --version    output version information and exit

Exit status:
 0  if OK,
    otherwise nonzero
EOF
}

suggest_help () {
	echo "Try \`$0 --help' for more information."
}

unrecognized_option () {
	echo "$0: unrecognized option \`$1'"
	suggest_help
}

extra_operand () {
	echo "$0: extra operand \`$1'"
	suggest_help
}
