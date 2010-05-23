#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

my $url = "http://craigslist.org";
my $pattern = "<a href=\"(http:\/\/[^\"]*\.craigslist\.org\/(?!about)[^\"]*)\">([^<]*)<\/a>";
my %list = ();

spider($url);

sub spider
{
	#Method will take a single URL as a parameter.
	my $seed = $_[0];
	
	#Get page and store it
	#Problem: recursively, doesn't the page stay in memory for the entire spider?
	#There might be memory problem with recursive method?
	my $page = get($seed);
	
	#Find all valid links in the page and add it to our map.
	#Major problem: theres a TON of pages..must consider not getting Adpages, only names of ads
	while($page =~ /$pattern/gi)
	{
	    if(!exists $list{$1})
	    {
	    	$list{$1} = $2;
	    	print $1." ".$2."\n";
			spider($1);
	    }
	}
}

sub is_listing
{
	# Determine if given page is a listing
}
