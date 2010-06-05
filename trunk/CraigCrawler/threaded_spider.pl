package threaded_spider;

use warnings;
use strict;
use LWP::Simple;
use Digest::MD5 qw(md5_base64);
use threads;
use threads::shared;
use Thread::Queue;
require pattern;
require util;

my %list : shared= (); #Will contain all links spidered
my %list_hash : shared = (); #Will contain hash for page
my %list_minihash : shared = (); #Will contain hash for part of page header info
my %ads : shared = ();  #Will contain all advertisement links
my @link_stack : shared = ();
my $max_threads = 10;

sub threaded_spider
{
	##
	#Create 2 threads here
	#-The first one for the thread_pool method switch will spawn new downloader threads
	#-The second one for the page_process method which process the new pages.
	##
}

sub thread_pool
{
	while( scalar @link_stack || scalar threads->list() ) 
	{
		if(scalar threads->list() < $max_threads && scalar @link_stack) 
		{
				lock(@link_stack);
				my @pop = pop @link_stack;
				threads->create(\&threaded_get_page,$pop[0],$pop[1]);
				print "New thread created with seed ".$pop[0]."\n";
		}
		threads->yield();
	}
}

sub threaded_get_page
{
	##
	#Need to wrap this method in a while loop since creating new threads is expensive..
	##
	
	my $seed = $_[0];
	my $page_name = $_[1];
	my $ua = LWP::UserAgent->new;
	$ua->timeout(5);
	$ua->agent("");
	my $resp = $ua->get('GET',$seed); #sends get request, returns response obj
	print "Could not download page: ".$seed."\n" if(!$resp->is_success);
	
	##
	#Insert page contents , page link, and page name into some data structure here.
	##
}

sub threaded_process_page
{	
	##
	#Need to wrap this method in a while loop.. and it will just pull new pages off a stack.
	##
	
	#Method takes the URL, pagename, and recursion depth as parameters
	my $seed = $_[0];
	my $seed_name = $_[1];
	my $seed_page = $_[2];
	
	#Get page, store it, check its hash
	#Here we download & check just the header data before we continue with full page download.
	
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
    		my @new_item = ( $curr_link, $page_name );
   			push (@link_stack, [@new_item]);
   			$list{$curr_link} = $page_name;
    	}
	}
}
1;