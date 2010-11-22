#!/bin/bash
#
# A probably unstable script to build/install the project documentation.
#

if ([ -d ../../www/doc ]); then
	ant generate-javadoc
	svn del --force ../../www/doc/merapi
	mkdir ../../www/doc/merapi
	svn add --non-recursive ../../www/doc/merapi
	cd build/doc
	find | perl -nle 'print if -d && $_ ne "."' | perl -nle '$_="../../../../www/doc/merapi/$_";system("mkdir -p $_");system("svn add --non-recursive $_")'
	find | perl -nle 'print unless -d' | perl -nle '$d="../../../../www/doc/merapi/$_";system("mv $_ $d");system("svn add $d")'
	cd ../..
	rm -rf build/doc
else
	echo "This command needs the entire repository tree to work."
	echo "Check out the root and try again."
	exit 1
fi
