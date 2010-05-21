#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

my $url = "http://craigslist.org";
my @page = split /\n/, get($url);
foreach (@page)
{
	#Trying to print the relevent websites, broken!
	$_ =~ /<a href=http:\/\/([a-zA-Z]+)\.craigslist\.org\/>([a-zA-Z]+)</a>/;
	print $1." ".$2."\n";
}