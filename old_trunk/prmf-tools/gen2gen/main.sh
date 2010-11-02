function _main {
	cwd=`pwd`
	tmp=`mktemp -d -p .`
	_extract $tmp
	cd $tmp
	source mkgentoo.sh
	_mkgentoo
	cd $cwd
	rm -rf $tmp
}
