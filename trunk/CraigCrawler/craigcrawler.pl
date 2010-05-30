#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;
use Digest::MD5 qw(md5_base64);

#Current problems: (In order of priority)
#1.Very slow, need to possibly have a few threads downloading at once .. project will get more interesting as well
#2.Organization of data gathered, currently the links are separated as only ads or not ads.
#3.Need more control over scanning, possibly the option to scan as far as 2 pages back, or even by day.
#4.code is not very slick

my $url = "http://craigslist.org";
my $link_pattern = qr/<a href=\"([^\"]*)\">([^<]*)<\/a>/;

my @bad_patterns = ( qr"^/$", qr/mailto/, qr/https/, qr/blog\./, qr"/about/", qr"^(cal)", qr/forums[\/\.]?/, qr/cgi-bin/, qr/(index[0-9]+\.html)$/);
my @good_patterns = ( qr/craigslist\.org/, qr/^(?!http)/ );
my $listing_pattern = qr"(/[0-9]+.html)$";

my %list = (); #Will contain all links spidered
my %list_hash = (); #Will contain hash for page
my %ads = ();  #Will contain all advertisement links
my $max_depth = 10;

spider($url,"craigslist",0);
print scalar keys %list," found";

sub spider
{
	#Method will take a single URL as a parameter.
	my $seed = $_[0];
	my $seed_name = $_[1];
	my $depth = $_[2];
	
	return if($depth > $max_depth);
	
	#Get page, store it, check its hash
	$list{$seed} = $seed_name;
	my $page = get($seed) or print "Could not download page ".$seed."\n" && return;
	my $hash = md5_base64($page);
	return if exists $list_hash{$hash} && print "Page's hash found in visited list\n";
	$list_hash{$hash} = 1; #Valueless hash?
	
	#Find all valid links in the page and add it to our map.
	scan: while($page =~ /$link_pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
		next scan if(!validate_link($curr_link));
		
		$curr_link = clean_link($seed,$curr_link);
		
    	next scan if(exists $list{$curr_link});
    	print $depth." ".$seed." => ".$curr_link." ".$page_name."\n";
    	if ($curr_link =~ /$listing_pattern/)
    	{
    		$ads{$curr_link} = $page_name;
    	}
    	else 
    	{
			spider($curr_link,$page_name,$depth+1);
    	}
	}
}

sub validate_link
{
		my $curr_link = $_[0];
		foreach my $bad (@bad_patterns)
		{
			return 0 if($curr_link =~ /$bad/);
		}
		
		foreach my $good (@good_patterns)
		{
			return 1 if($curr_link =~ /$good/);
		}
		return 0;
}

sub clean_link
{
		my $seed = $_[0];
		my $curr_link = $_[1];
		
	    if($curr_link =~ /http/)
    	{
    		$curr_link =~ s"/$""; #remove trailing slashes 
    	}
    	else
    	{

	    	$curr_link =~ s"/""; #replace slashes with empty string, and append a single slash to front
	    	$curr_link = "/".$curr_link;
	    	$curr_link = $seed.$curr_link;
    	}
    	return $curr_link;
}
