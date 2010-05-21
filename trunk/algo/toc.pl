#!/usr/bin/perl
#
# This script gets the algorithm packet's table of contents in Google Code wiki
# format.
#

use warnings;
use strict;

my $file = 'algo.toc';
my $fh;

unless(-f $file) {
	print STDERR "Couldn't find file: $file\n";
	print STDERR "Please execute `make` to generate it.\n";
	exit 1;
}

unless(open $fh, $file) {
	print STDERR "Couldn't open $file for reading!\n";
	exit 1;
}

while(<$fh>) {
	chomp;
	/^\\contentsline\s*\{((?:sub)*)section\}\{\\numberline\s*\{[^\}]+\}([^\}]+)\}/;
	print " " x (2 + length($1) / 3) . "# $2\n";
}
