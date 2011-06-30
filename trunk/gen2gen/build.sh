#!/bin/bash

if ! (type -p shellpack >& /dev/null); then
	echo "Looks like App::ShellPack isn't installed!" >&2
	echo "(or maybe the relevant environment variables weren't updated)" >&2
	exit 1
fi

echo "#!/bin/bash"
shellpack bs-*.sh chroot.sh confset.pl grub.conf make.conf grub.conf random_antarctic_island.sh
cat main.sh
