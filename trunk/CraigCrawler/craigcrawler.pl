#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

my $url = "http://craigslist.org";
my $pattern = "<a href=\"([^\"]*)\">([^<]*)<\/a>";
# example listing http://miami.craigslist.org/brw/act/1755874712.html , not catching it..
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
	    if(!exists $list{$1})
	    {
	    	$list{$1} = $2;
	    	print $depth." ".$seed." => ".$1." ".$2;
	    	if($depth > $max_depth || dont_spider($1))
	    	{
				print " - not spidered\n";
	    	}
	    	else
	    	{
	    		print "\n";
	    		if($1 =~ /http/)
	    		{
	    			spider($1,$depth+1);
	    		}
	    		else
	    		{
	    			spider($seed."/".$1,$depth+1);
	    		}
	    	}
	    }
	}
	
}

sub dont_spider
{
	#List of types of pages not to spider, first one is listings (spidering includes downloading)
	my @bad = ( qr/(\d+\.html)$/ , qr/blog/ , qr/\/about\//, qr/^https:/, qr/http:\/\/.*\.(?!craigslist.org)/ );
	my $ret = 0;
	foreach my $badpattern (@bad)
	{
		$ret = 1 if $_[0] =~ /$badpattern/;
	}
	return $ret;
}
