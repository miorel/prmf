#!/bin/bash

if ! (type -p shellpack >& /dev/null); then
	echo "Looks like App::ShellPack isn't installed!"
	echo "(or maybe the relevant environment variables weren't updated)"
	exit
fi

function _build {
	echo "#!/bin/bash"
	cat run.sh
	shellpack confset.pl mkgentoo.sh grub.conf make.conf
	echo _run
}

_build | tee gen2gen.sh
