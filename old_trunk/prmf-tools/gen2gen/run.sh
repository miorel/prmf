run () {
	parse_opts=1
	operand=()
	for arg in $@; do
		parsed=0
		if (( $parse_opts )); then
			case "$arg" in
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
	if [ ${#operand[*]} -gt 0 ]; then
		extra_operand ${operand[0]}
		return 1
	fi

	wd=$PWD
	tmp=`mktemp -d -p .`
	cd $tmp
	_extract
	source util.sh
	source mkgentoo.sh
	_mkgentoo
	ret=$?
	cd $wd
	rm -rf $tmp
	return $ret
}

print_version () {
	cat << '_VERSION_'
gen2gen 0.0.1 (November 5, 2010)
Copyright (C) 2010 Miorel-Lucian Palii
License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>.
This is free software: you are free to change and redistribute it.
There is NO WARRANTY, to the extent permitted by law.
_VERSION_
}

print_help () {
	cat << _HELP_
Usage: $0 [OPTION]...
Install a basic Gentoo system.

      --          suppress further option parsing (useful for passing operands
                  that start with a hyphen without confusing them for options)
  -h, -?, --help  display this help and exit
  -v, --version   output version information and exit

Exit status:
 0  if OK,
    otherwise nonzero
_HELP_
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
