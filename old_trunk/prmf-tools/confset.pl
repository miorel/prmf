#!/usr/bin/perl

use warnings;
use strict;

die "must specify a file" unless @ARGV;

my $file = shift;
my %opt = ();
my $fh;

for(@ARGV) {
	my @arr = get_pair($_);
	die "bad key/value pair: $_" unless @arr;
	$opt{$arr[0]} = $arr[1];
}

open $fh, "< $file" or die "failed to open $file for reading";
my @lines = <$fh>;
close $fh;

my($key,$val);
while(($key, $val) = each %opt) {
	my $i = scalar @lines;
	my @i = grep {my @arr = get_pair(uncomment($lines[$_])); @arr && $arr[0] eq $key} 0..$#lines;
	if(@i) {
		$i = $i[-1];
		my @nc_i = grep {!is_comment($lines[$_])} @i;
		if(@nc_i) {
			$i = $nc_i[0];
			$lines[$_] = "#" . $lines[$_] for @nc_i;
		}
	}
	$lines[$i] = "$key=$val\n";
}

open $fh, "> $file" or die "failed to open $file for writing";
print $fh $_ for @lines;
close $fh;

sub is_comment {
	my $text = shift;
	return $text =~ /^\s*#/;
}

sub uncomment {
	my $text = shift;
	$text =~ s/^[\s#]+//;
	return $text;
}

sub get_pair {
	my $text = shift;
	return $text =~ /^\s*([^\s=]+)=(.*)$/s ? ($1, $2) : ();
}
