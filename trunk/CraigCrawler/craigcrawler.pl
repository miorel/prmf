#!/usr/bin/perl

use warnings;
use strict;

use LWP::Simple;

my $url = "http://craigslist.org";
my $content = get($url);

#are these 'global' variables? needs to learn more perl
my $pattern = "<a href=\"(http:\/\/[^\"]*\.craigslist\.org\/[^\"]*)\">([^<]*)<\/a>";
my %links = ();

spider($url);

sub spider
{
	#Method will take a single URL as a parameter.
	#Create array of links with get($_) that follow $pattern.
	#Attempt to save all the links in the map(?) for easy lookup
	#spider($key) unless exists %map{$key} 
}

sub is_listing
{
	#Determine if given page is a listing
}