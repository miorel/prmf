#!/usr/bin/perl

use warnings;
use strict;

use File::Find;

### BEGIN CONFIG ###############################################################
my $java = <<'EOF';
/*
 * Merapi - Multi-purpose Java library
 * Copyright (C) 2009-2010 Miorel-Lucian Palii <mlpalii@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
EOF
### END CONFIG #################################################################

find(sub {
	my $file = $File::Find::name;
	return unless -f && $file !~ /\.svn/ && $file !~ /^\.\/lib/;
	if($file =~ /\.java$/i && $file !~ /\/package-info\.java$/i) {
		print "Processing $file\n";
		my $fh;
		open $fh, "<$_" or die "Failed to open $file for reading";
		my @in = <$fh>;
		shift @in until $in[0] =~ /^\s*(?:package|import)/;
		close $fh;
		my @out = ($java, @in);
		open $fh, ">$_" or die "Failed to open $file for writing";
		print $fh $_ for @out;
		close $fh;
	}
}, '.');
