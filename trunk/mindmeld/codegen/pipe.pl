#!/usr/bin/perl

use warnings;
use strict;

my $fh;

while(<>) {
	if(/^\s*package\s+([^\s;]+)/) {
		my $file = $1;
		$file =~ s[::][/]g;
		$file = "dist/$file.pm";
		system(qq(mkdir -p "\$(dirname "$file")"));
		close $fh if defined $fh;
		open $fh, ">$file" or die "Failed to open $file for writing";
	}
	print $fh $_;
}

close $fh if defined $fh;
