package threaded_spider;

use warnings;
use strict;
use LWP::UserAgent;
use Digest::MD5 qw(md5_base64);
use threads;
use threads::shared;
use Thread::Queue;
require settings;
require pattern;
require util;

my %list : shared= (); #Will contain all links spidered
my %list_hash : shared = (); #Will contain hash for pages downloaded
my %list_minihash : shared = (); #Will contain hash for part of page header info
my %ads : shared = ();  #Will contain all advertisement links found
my @link_stack : shared = (); #Will contain links to be downloaded and added to @page_stack
my @page_stack : shared = (); #Will contain pages to be scanned for new links to be added to @link_stack
my $thread_count : shared = 0; #Number of spawned downloader threads
my $max_threads = 10;

sub threaded_spider
{
	my $seed = $_[0];
	my $seed_name = $_[1];
	my $pool = threads->create(\&thread_pool,$seed,$seed_name);
	my $processor = threads->create(\&threaded_process_page);
}

sub thread_pool
{
	while( scalar @link_stack || scalar threads->list() ) 
	{
		if(scalar threads->list() < $max_threads && scalar @link_stack) #incorrect since *this* itself is a thread
		{
				my @pop;
				{
					lock(@link_stack);
					@pop = pop @link_stack;
				}
				my $thr = threads->create(\&threaded_get_page,$pop[0],$pop[1]);
				$thr->detach();
				print "New thread created with seed ".$pop[0]."\n";
				lock($thread_count);
				$thread_count++;
		}
		threads->yield();
	}
}

sub threaded_get_page
{
	my $ua = LWP::UserAgent->new;
	$ua->timeout(5);
	while()
	{
		my $seed = $_[0];
		my $page_name = $_[1];
		$ua->agent("");
		my $resp = $ua->get('GET',$seed); #sends get request, returns response obj
		print "Could not download page: ".$seed."\n" if(!$resp->is_success);
		
		##
		#Insert page contents , page link, and page name into some data structure here.
		##
	}
	lock($thread_count);
	$thread_count--;
}

sub threaded_process_page
{	
	while( scalar @page_stack ) 
	{
		my $page; # pop off page stack .. page should be nice data structure with link and page_name (???)
		
		#Now we check the page hash
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
}
1;