#!/usr/bin/perl

system("mksquashfs", 

"gentoo",
"gentoo.sqfs",


"-regex",
(map { /\/(.*)/; ("-e" => "$1/.*") } qw(
	/dev
	/proc
	/sys
	/tmp
	/var/tmp
	/var/lock
	/var/run
)),


);
