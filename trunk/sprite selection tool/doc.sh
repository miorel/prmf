#!/bin/bash
#
# A probably unstable script to build/install the project documentation.
#

if ([ -d ../../www/doc ] && [ -f Doxyfile ]); then
	if (which doxygen >& /dev/null); then
		doxygen
		svn del --force ../../www/doc/sst
		mkdir ../../www/doc/sst
		svn add --non-recursive ../../www/doc/sst
		cd doc
		find | perl -nle 'print if -d && $_ ne "."' | perl -nle '$_="../../../www/doc/sst/$_";system("mkdir -p $_");system("svn add --non-recursive $_")'
		find | perl -nle 'print unless -d' | perl -nle '$d="../../../www/doc/sst/$_";system("mv $_ $d");system("svn add $d")'
		cd ..
		rm -rf doc
	else
		echo "Oops! It looks like you don't have a doxygen command :/"
		echo "Get it from <http://www.doxygen.org/> and try again."
		exit 1
	fi
else
	echo "This command needs the entire repository tree to work."
	echo "Check out the root and try again."
	exit 1
fi
