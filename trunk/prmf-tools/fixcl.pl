#!/usr/bin/perl

use warnings;
use strict;

my $tab_length = 8;
my $line_length = 78;

my $first = 1; # whether or not this is the first entry, initially yes

while(<STDIN>) { # for each line of standard input only (avoid <> magic)
	for(;;) { # infinite loop for processing, will break out explicitly
		last unless /\S/; # skip empty lines

		if(/^(\d+\-\d+\-\d+)\s+(.+?)\s+<([^@]+@[^@.]+(?:\.[^@.>]+))>/) {
			# Looks like the first line of an entry.
			# YYYY-MM-DD  Rick Astley <rick@example.org>

			# empty line between entries
			# i.e. empty line before every entry except first
			print "\n" unless $first;

			$first = 0; # now no longer first

			print "$1  $2 <$3>\n\n";
			
			# done processing this line, break out of infinite loop
			last;
		}
		if(s/^\s*\*\s+//) {
			my $msg = $_;
			my $continue = 0;
			while(<STDIN>) {
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

# finish off the file with an empty line
print "\n";
