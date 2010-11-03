function _main {
	cwd=`pwd`
	tmp=`mktemp -d -p .`
	cd $tmp
	_extract
	source mkgentoo.sh
	_mkgentoo
	cd $cwd
	rm -rf $tmp
}
