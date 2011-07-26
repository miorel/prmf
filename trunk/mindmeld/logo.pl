#!/usr/bin/perl

use warnings;
use strict;

require 'lib/svg.pl';

my $margin = 15;
my $size = 100;

my $style = "font-family:LMSansDemiCond10;text-anchor:middle";

my $mind = {tag => 'text', attr => {id => 'mind', style => "$style;fill:#ffffff;font-size:${size}px"}, children => ['mind']};
my $meld = {tag => 'text', attr => {id => 'meld', style => "$style;fill:#b8b8b8;font-size:${size}px"}, children => ['meld']};
my $svg = {children => [$mind, $meld], attr => {height => 0, width => $margin * 2}};

my $fh;

my $file = "logo.svg";
my $tmpfile = "$file.tmp";

open $fh, ">$tmpfile" or die "Failed to open $tmpfile for writing";
write_svg_file($svg, $fh);
close $fh;

my @measurements = `inkscape -z "$tmpfile" -S`;
unlink $tmpfile;

for(@measurements) {
	chomp;
	my($id, $x, $y, $w, $h) = split(',', $_);
	if($id eq 'mind') {
		$svg->{attr}->{height} = max($svg->{attr}->{height}, $h + $margin * 2);
		$svg->{attr}->{width} += $w;
		$mind->{attr}->{'x'} = $w / 2;
		$meld->{attr}->{'x'} = $w;
	}
	elsif($id eq 'meld') {
		$svg->{attr}->{width} += $w;
	}
}
$mind->{attr}->{'y'} = $meld->{attr}->{'y'} = $svg->{attr}->{height} - $margin;
$mind->{attr}->{'x'} += $margin;
$meld->{attr}->{'x'} += $mind->{attr}->{'x'};

open $fh, ">$file" or die "Failed to open $file for writing";
write_svg_file($svg, $fh);
close $fh;

sub max {
	my $ret = shift;
	for(@_) {
		$ret = $_ if $_ > $ret;
	}
	return $ret;
}

