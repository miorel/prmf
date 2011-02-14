#!/bin/bash

if ! (type -p shellpack >& /dev/null); then
	echo "Looks like App::ShellPack isn't installed!"
	echo "(or maybe the relevant environment variables weren't updated)"
	exit 1
fi

build_gen2gen () {
	echo "#!/bin/bash"
	cat run.sh
	echo
	shellpack confset.pl mkgentoo.sh grub.conf make.conf util.sh
	echo
	echo 'run "$@"'
}

build_gen2gen | tee gen2gen
chmod +x gen2gen
