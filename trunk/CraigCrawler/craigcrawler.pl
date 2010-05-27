#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

#Current problems: (In order of priority)
#1.Main regex pattern is bad, could be way cooler..and more functional, still broken
#2.organization of data gathered 
#3.Very slow, need to possibly have a few threads downloading at once .. project will get more interesting as well
#4.code is not very slick

my $url = "http://craigslist.org";
my $pattern = qr/<a href=\"([^\"]*craigslist\.org[^\"]*)\">([^<]*)<\/a>/; #.org, not sure if we want .ca ... etc
my $pattern2 = qr/<a href=\"(\/?[^\"\/]*\/?)\">([^<]*)<\/a>/; #for local directories ex. "/books" or "books/"
#are $pattern and $pattern2 redundant? does $pattern2 catch everything that $pattern does anyway? ops

my @bad_patterns = (qr/https/, qr/blog/, qr/\/about\//, qr/forums\./);

my %list = ();
my $max_depth = 10;

spider($url,"craigslist",0);
print scalar keys %list," found";

sub spider
{
	#Method will take a single URL as a parameter.
	my $seed = $_[0];
	my $seed_name = $_[1];
	my $depth = $_[2];
	
	$list{$seed} = $seed_name;
	
	return if($depth > $max_depth);
	
	#Get page and store it
	my $page = get($seed) or print "Could not download page ".$seed."\n" && return;
	
	#Find all valid links in the page and add it to our map.
	scan: while($page =~ /$pattern/gic || $page =~ /\G$pattern2/gi) #c flag stores last failed match location
	{
		my $curr_link = $1;
		my $page_name = $2;
		
		foreach my $bad (@bad_patterns)
		{
			if($curr_link =~ /$bad/)
			{
				next scan;
			}
		}

    	if($curr_link =~ /http/)
    	{
    		if($curr_link =~ /(.*)\/$/) #remove trailing slashes in full URLs
    		{
    			$curr_link = $1;
    		}
    	}
    	else
    	{
	    	if($curr_link =~ /^[^\/](.*)/) #remove leading slash, for links that are subdirectories
	    	{
	    		$curr_link = "/".$1;
	    	}
	    	$curr_link = $seed.$curr_link;
    	}
    	next scan if(exists $list{$curr_link});
    	print $depth." ".$seed." => ".$curr_link." ".$page_name."\n";
    	spider($curr_link,$page_name,$depth+1);
	}
	
}


