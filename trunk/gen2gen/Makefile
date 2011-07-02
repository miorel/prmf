SHELL=/bin/bash

SHELLPACK_BUNDLE=bs-main-201106241845.sh chroot.sh confset.pl grub.conf make.conf grub.conf random_antarctic_island.sh

gen2gen: $(SHELLPACK_BUNDLE) main.sh Makefile
	if ! (type -p shellpack >& /dev/null); then \
		echo "Looks like App::ShellPack isn't installed!" >&2; \
		echo "(or maybe the relevant environment variables weren't updated)" >&2; \
		false; \
	else \
		echo "#!/bin/bash" > gen2gen; \
		shellpack $(SHELLPACK_BUNDLE) >> gen2gen; \
		cat main.sh >> gen2gen; \
		chmod +x gen2gen; \
	fi

random_antarctic_island.sh: random_antarctic_island.pl
	perl random_antarctic_island.pl > random_antarctic_island.sh

clean:
	rm -f gen2gen random_antarctic_island.sh

.PHONY: clean
