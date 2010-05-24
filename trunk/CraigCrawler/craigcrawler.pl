#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

my $url = "http://craigslist.org";
my $pattern = qr/<a href=\"([^\"]*)\">([^<]*)<\/a>/;
my %list = ();
my $max_depth = 10;

spider($url,0);
print scalar keys %list," found";

sub spider
{
	#Method will take a single URL as a parameter.
	my $seed = $_[0];
	my $depth = $_[1];
	
	#Get page and store it
	my $page = get($seed) or print "Could not download page ".$url."\n";
	
	#Find all valid links in the page and add it to our map.
	while($page =~ /$pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
	    if(!exists $list{$curr_link})
	    {
	    	$list{$curr_link} = $page_name;
	    	print $depth." ".$seed." => ".$curr_link." ".$page_name;
	    	if($depth > $max_depth || dont_spider($curr_link))
	    	{
				print " - not spidered\n";
	    	}
	    	else
	    	{
	    		print "\n";
	    		if($curr_link =~ /http/)
	    		{
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

sub dont_spider
{
	#List of types of pages not to spider, first one is listings (spidering includes downloading)
	my @bad = (  qr/(\d+\.html)$/ ,
				 qr/blog/ , 
				 qr/\/about\//, 
				 qr/^https:/, 
				 qr/?(^(http))(?!(\.craigslist\.org))|(?!(^\/))/ ,#broken, trying to match only craigslist sites and local directories
				 qr/\/forums\//
				  );
	my $ret = 0;
	foreach my $badpattern (@bad)
	{
		$ret = 1 if $_[0] =~ /$badpattern/i && print " ".$badpattern;
	}
	return $ret;
}
