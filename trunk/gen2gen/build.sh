#!/bin/bash

if ! (type -p shellpack >& /dev/null); then
	echo "Looks like App::ShellPack isn't installed!"
	echo "(or maybe the relevant environment variables weren't updated)"
	exit 1
fi

build_gen2gen () {
	echo "#!/bin/bash"
	shellpack bs-*.sh chroot.sh make.conf #confset.pl grub.conf util.sh
	echo
	cat main.sh #run.sh
}

build_gen2gen | tee gen2gen && chmod +x gen2gen
