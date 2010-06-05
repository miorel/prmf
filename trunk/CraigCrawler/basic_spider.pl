package basic_spider;

use warnings;
use strict;
use LWP::Simple;
use Digest::MD5 qw(md5_base64);
require settings;
require pattern;
require util;

my %list = (); #Will contain all links spidered
my %list_hash = (); #Will contain hash for pages downloaded
my %list_minihash = (); #Will contain hash for part of page header info
my %ads = ();  #Will contain all advertisement links found
my @link_stack = ();

sub basic_spider
{
	my $seed_page = { url  => $_[0], seed_name   => $_[1]};
	push(@link_stack,$seed_page);
	
	while(scalar @link_stack)
	{
		my $popped = pop @link_stack;
		basic_crawler($popped->{url},$popped->{seed_name});
		my $stack_size = scalar @link_stack;
		print "Links in stack: ".$stack_size."\n";
	}
	my $num_listings = scalar keys %ads;
	print $num_listings." found.\n";
}

sub basic_crawler
{
	#Method takes the URL, pagename, and recursion depth as parameters
	my $seed = $_[0];
	my $seed_name = $_[1];
	
	#Get page, store it, check its hash
	#Here we download & check just the header data before we continue with full page download.
	if(settings::check_headers_hash())
	{
		my @header_data = head($seed) or print "Could not get page header info: ".$seed."\n";
		if($header_data[2] && $header_data[3]) #Check if data we want to hash is there..
		{
			#Question.. Should we even hash this data or just use the concatenation as key? (Unique enough?)
			my $minihash = md5_base64($header_data[2].$header_data[3]); #modified time.expiration data
			return if exists $list_minihash{$minihash} && "minihash found in visited list:".$seed."\n";
			$list_minihash{$minihash} = 1; #Valueless hash?
		}
	}
	
	#Now we download the entire page and do the same process..
	my $page = get($seed) or print "Could not download page: ".$seed."\n";
	return if !$page;
	if(settings::check_page_hash())
	{
		my $hash = md5_base64($page);
		return if exists $list_hash{$hash} && print "hash found in visited list:".$seed."\n";
		$list_hash{$hash} = 1; #Valueless hash? Might actually store modified time in here at later point in project.
	}
	
	#Find all valid links in the page and add it to our map.
	my $link_pattern = pattern::get_link_pattern();
	scan: while($page =~ /$link_pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
		next scan if(!util::validate_link($curr_link, $seed));
		
		$curr_link = util::clean_link($seed,$curr_link);
		
    	next scan if(exists $list{$curr_link});
    	print $seed." => ".$curr_link." ".$page_name."\n";
    	
    	my $listing_pattern = pattern::get_listing_pattern();
    	if ($curr_link =~ /$listing_pattern/)
    	{
    		$ads{$curr_link} = $page_name;
    	}
    	else 
    	{
    		my $new_item = { url=>$curr_link, seed_name=>$page_name };
   			push (@link_stack,$new_item);
   			$list{$curr_link} = $page_name;
    	}
	}
}
1;