#!/usr/bin/perl

use warnings;
use strict;

my $dir = shift;

print "Running consigliere on directory: $dir\n";

opendir(my $dh, $dir) or die "failed to opendir $dir: $!";
my @entries = grep { !/^\.(?:|\.|svn)$/ } readdir($dh);
closedir $dh;

printf("Entry \"%s\"\n", $_) for @entries;
