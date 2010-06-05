package threaded_spider;

use warnings;
use strict;
use LWP::UserAgent;
use Digest::MD5 qw(md5_base64);
use threads;
use threads::shared;
use Thread::Queue;
use Thread::Semaphore;
require settings;
require pattern;
require util;

my %list : shared= (); #Will contain all links spidered
my %list_hash : shared = (); #Will contain hash for pages downloaded
my %list_minihash : shared = (); #Will contain hash for part of page header info
my %ads : shared = ();  #Will contain all advertisement links found
my @link_stack : shared = (); #Will contain links to be downloaded and added to @page_queue
my $link_sema : shared = Thread::Semaphore->new(); #Semaphore for @link_stack;
my $page_queue : shared = Thread::Queue->new(); #Will contain pages to be scanned for new links to be added to @link_stack
my $thread_count : shared = 0; #Number of spawned downloader threads
my @threads = ();
my $max_threads = 40; #Too fast.. not nice for craigslist. :)
my $page_processing : shared = 0;
my $page_downloaded : shared = 0;

sub threaded_spider
{
	my $seed = $_[0];
	my $seed_name = $_[1];
	thread_pool($seed);
	threaded_process_page();
	#my $thr_processor = threads->create(\&threaded_process_page);
}

sub thread_pool
{
	push @link_stack , $_[0];
	while($thread_count < $max_threads)
	{
			my $thr = threads->create(\&threaded_get_page);
			$thr->detach();
			push @threads,$thr;
			$thread_count++;
	}
}

sub threaded_get_page
{
	my $ua = LWP::UserAgent->new;
	$ua->timeout(5);
	$ua->agent("");
	while( scalar @link_stack || $page_queue->pending() || $page_processing ) #Is this a sufficient condition?
	{
		#print threads->tid()."\n";
		my $seed;
		$link_sema->down();
			$seed = (scalar @link_stack ? shift @link_stack : 0);
		$link_sema->up();
		if($seed)
		{
			my $resp = $ua->get($seed); #sends get request, returns response obj
			if($resp->is_success)
			{
				$page_downloaded++;
				print $page_downloaded.") Thread ".threads->tid(). " downloaded ".$seed."\n";
				$page_queue->enqueue({source=>$resp->decoded_content,url=>$seed});
			}
			else
			{
				print "Could not download page: ".$seed."\n";
			}
		}
		sleep 1;
	}
	lock($thread_count);
	$thread_count--;
}

sub threaded_process_page
{	
	#need nice condition here .. $page_queue->pending will not do since we can process much faster than download..
	#for example we can process the first page much faster than the 2nd page can be downloaded.
	while( 1 ) 
	{
		$page_processing = 0;
		next if !$page_queue->pending();
		$page_processing = 1;
		
		my $page_obj = $page_queue->dequeue();
		#Now we check the page hash
		if(settings::check_page_hash())
		{
			my $hash = md5_base64($page_obj->{source});
			next if exists $list_hash{$hash} && print "hash found in visited list:".$page_obj->{url}."\n";
			$list_hash{$hash} = 1; #Valueless hash? Might actually store modified time in here at later point in project.
		}
		
		#Find all valid links in the page and add it to our map.
		my $link_pattern = pattern::get_link_pattern();
		my $page_src = $page_obj->{source};
		scan: while($page_src =~ /$link_pattern/gi)
		{
			my $curr_link = $1;
			my $page_name = $2;
			next scan if(!util::validate_link($curr_link, $page_obj->{url}));
			
			$curr_link = util::clean_link($page_obj->{url},$curr_link);
			
	    	next scan if(exists $list{$curr_link});
	    	print $page_obj->{url}." => ".$curr_link." ".$page_name."\n";
	    	
	    	my $listing_pattern = pattern::get_listing_pattern();
	    	if ($curr_link =~ /$listing_pattern/)
	    	{
	    		$ads{$curr_link} = $page_name;
	    	}
	    	else 
	    	{
				$link_sema->down();
		   			push (@link_stack, $curr_link);
	    		$link_sema->up();
	   			$list{$curr_link} = $page_name;
	    	}
		}
	}
}
1;