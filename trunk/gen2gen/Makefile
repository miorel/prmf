SHELL=/bin/bash

gen2gen: bs-main-201106241845.sh build.sh chroot.sh confset.pl grub.conf main.sh make.conf Makefile random_antarctic_island.sh
	bash build.sh | tee gen2gen
	chmod +x gen2gen

random_antarctic_island.sh: random_antarctic_island.pl
	perl random_antarctic_island.pl > random_antarctic_island.sh

clean:
	rm -f gen2gen random_antarctic_island.sh

.PHONY: clean
