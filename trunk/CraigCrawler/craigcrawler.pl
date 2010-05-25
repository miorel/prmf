#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

#Current problems: (In order of priority)
#1.Main regex pattern is bad, it doesnt filter out actual adpages, blog.craigslist,craigslist.org/ABOUT, etc..
#2.organization of data gathered 
#3.Very slow, need to possibly have a few threads downloading at once
#4.code is not very slick

my $url = "http://craigslist.org";
my $pattern = qr/<a href=\"([^\"]*craigslist.org[^\"]*)\">([^<]*)<\/a>/;
my %list = ();
my $max_depth = 8;

spider($url,0);
print scalar keys %list," found";

sub spider
{
	#Method will take a single URL as a parameter.
	my $seed = $_[0];
	my $depth = $_[1];
	
	#Get page and store it
	my $page = get($seed) or print "Could not download page ".$seed."\n" && return;
	
	#Find all valid links in the page and add it to our map.
	while($page =~ /$pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
	    if(!exists $list{$curr_link})
	    {
	    	$list{$curr_link} = $page_name;
	    	if($depth <= $max_depth)
	    	{
	    		print $depth." ".$seed." => ".$curr_link." ".$page_name."\n";
	    		if($curr_link =~ /http/)
	    		{
	    			if($curr_link =~ /(.*)\/$/) #remove trailing slashes in full URLs
	    			{
	    				$curr_link = $1;
	    			}
	    			spider($curr_link,$depth+1);
	    		}
	    		else #handle links that are subdirectories
	    		{
	    			if($curr_link =~ /^\/(.*)/) #remove leading slash
	    			{
	    				$curr_link = $1;
	    			}
	    			
	    				spider($seed."/".$1,$depth+1);
	    		}
	    	}
	    }
	}
	
}

