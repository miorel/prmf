#!/usr/bin/perl

use warnings;
use strict;

use LWP::Simple;

my $url = "http://craigslist.org";
my $content = get($url);

# Are these 'global' variables?
# These are global variables in the sense that you can access them anywhere
# within this file. However, if you had multiple files they wouldn't be
# accessible there. For true global variables use "our" instead of "my".
my $pattern = "<a href=\"(http:\/\/[^\"]*\.craigslist\.org\/[^\"]*)\">([^<]*)<\/a>";
my %links = ();

spider($url);

sub spider
{
	# Method will take a single URL as a parameter.
	# Create array of links with get($_) that follow $pattern.
	# Attempt to save all the links in the hash for easy lookup
	# spider($key) unless exists $map{$key} 
}

sub is_listing
{
	# Determine if given page is a listing
}
