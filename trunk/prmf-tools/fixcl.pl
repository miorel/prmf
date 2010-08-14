#!/usr/bin/perl

use warnings;
use strict;

my $tab_length = 8;
my $line_length = 78;

my $first = 1;
while(<>) {
	for(;;) {
		last unless /\S/;
		if(/^(\d+\-\d+\-\d+)\s+(.+?)\s+<([^@]+@[^@.]+(\.[^@.>]+))>/) {
			print "\n" unless $first;
			$first = 0;
			print "$1  $2 <$3>\n\n";
			last;
		}
		if(s/^\s*\*\s+//) {
			my $msg = $_;
			my $continue = 0;
			while(<>) {
				if(/^\s*\*\s+/ || !/\S/) {
					$continue = 1;
					last;
				}
				$continue = 0;
				$msg .= $_;
			}
			$msg =~ s/\s+/ /sg;
			$msg =~ s/ $//;
			local @_ = split / /, $msg;
			my $asterisk = '*';
			while(@_) {
				$msg = shift @_;
				$msg .= " " . shift(@_) while @_ && (length($msg) + length($_[0]) + 1) <= ($line_length - ($tab_length + 2));
				print "\t$asterisk $msg\n";
				$asterisk = ' ';
			}
			$continue ? next : last;
		}
	}
}
print "\n";
