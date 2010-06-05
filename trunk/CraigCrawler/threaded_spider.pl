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
my %ads : shared = ();  #Will contain all advertisement links
my $max_depth = 10;
my $max_threads = 10;

sub threaded_spider
{
	#create a queue structure, add @_ to this queue
	my $link_queue : shared = Thread::Queue->new();
	my $seed_page = { url  => $_[0], seed_name   => $_[1], depth => 0 };
	$link_queue->enqueue($seed_page);

	while( $link_queue->pending() || scalar threads->list() ) 
	{
		if(scalar threads->list() < $max_threads && $link_queue->pending()) 
		{
				lock($link_queue);
				my $dequeue = $link_queue->dequeue();
				threads->create(\&threaded_crawl,$dequeue->{url},$dequeue->{seed_name},$dequeue->{depth},$link_queue);
				print "New thread created with seed ".$dequeue->{url}."\n";
		}
		threads->yield();
	}
}

sub threaded_crawl
{	
	my $seed = $_[0];
	my $seed_name = $_[1];
	my $depth = $_[2];
	my $link_queue = $_[3];
	return if($depth > $max_depth);

	my $page;
	{
		{
			lock(%list);
			$list{$seed} = $seed_name;
		}
		$page = get($seed) or print "Could not download page ".$seed."\n" && return;
		{
			lock(%list_hash);
			my $hash = md5_base64($page);
			return if (exists $list_hash{$hash}) && print "Page's hash found in visited list\n";
			$list_hash{$hash} = 1; #Valueless hash?
		}
	}

	my $link_pattern = pattern::get_link_pattern();
	scan: while($page =~ /$link_pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
		next scan if(!util::validate_link($curr_link));
		
		$curr_link = util::clean_link($seed,$curr_link);
		
		my $exists_link;
		{
			lock(%list);
			$exists_link = exists $list{$curr_link};
		}
    	next scan if($exists_link);
    	
    	print "tid:".threads->tid()." ".$depth." ".$seed." => ".$curr_link." ".$page_name."\n";
    	
    	my $listing_pattern = pattern::get_listing_pattern();
    	if ($curr_link =~ /$listing_pattern/)
    	{
    		lock(%ads);
    		$ads{$curr_link} = $page_name;
    	}
    	else 
    	{
			lock($link_queue);
			my $seed_page = { url  => $curr_link, seed_name   => $page_name, depth => $depth+1 };
			$link_queue->enqueue($seed_page);
    	}
	}
	my $next_page;
	{
		lock($link_queue);
		return if(!$link_queue->pending()) && print "Thread ".threads->tid()." is done.";
		$next_page = $link_queue->dequeue();
	}
	threaded_spider($next_page->{url},$next_page->{seed_name},$next_page->{depth},$link_queue);
}
1;