#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

#Current problems: (In order of priority)
#1.Program needs to be examined to see if it really does visit all webpages intended..it does seem to.
#2.Very slow, need to possibly have a few threads downloading at once .. project will get more interesting as well
#3.organization of data gathered, for example: do we need to download AD-pages or only their descriptions?
#4.code is not very slick

my $url = "http://craigslist.org";
my $link_pattern = qr/<a href=\"([^\"]*)\">([^<]*)<\/a>/;

#TODO: need to exclude very old listings
my @bad_patterns = ( qr/mailto/, qr/https/, qr/blog\./, qr/\/about\//, qr/forums\./, qr/cgi-bin/);
my @good_patterns = ( qr/craigslist\.org/, qr/^(?!http)/ );

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
	scan: while($page =~ /$link_pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
		next scan if(validate_link($curr_link));
		
		$curr_link = clean_link($seed,$curr_link);
		
    	next scan if(exists $list{$curr_link});
    	print $depth." ".$seed." => ".$curr_link." ".$page_name."\n";
    	spider($curr_link,$page_name,$depth+1);
	}
}

sub validate_link
{
		my $curr_link = $_[0];
		foreach my $bad (@bad_patterns)
		{
			return 1 if($curr_link =~ /$bad/);
		}
		
		foreach my $good (@good_patterns)
		{
			return 0 if($curr_link =~ /$good/);
		}
		return 1;
}

sub clean_link
{
		#this method has a bug, with double slashes appearing in links after its done
		#causing double downloads , one for link with single slash, and one with double
		my $seed = $_[0];
		my $curr_link = $_[1];
		
	    if($curr_link =~ /http/)
    	{
    		if($curr_link =~ /(.*)\/$/) #remove trailing slashes in full URLs, BROKEN..causes double slash?
    		{
    			$curr_link = $1;
    		}
    	}
    	else
    	{
	    	if($curr_link =~ /^\/?(\w*)\/?/) #get subdirectory name, without slashes
	    	{
	    		$curr_link = "/".$1; #append slash to create a full URL with the seed.
	    	}
	    	$curr_link = $seed.$curr_link;
    	}
    	return $curr_link;
}
