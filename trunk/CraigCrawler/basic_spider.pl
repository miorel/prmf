package basic_spider;

use LWP::Simple;
use Digest::MD5 qw(md5_base64);
use threads;
use threads::shared;
use Thread::Queue;
require pattern;
require util;

my %list = (); #Will contain all links spidered
my %list_hash = (); #Will contain hash for page
my %ads = ();  #Will contain all advertisement links
my $max_depth = 10;
my $max_threads = 10;

sub basic_spider
{
	#Method takes the URL, pagename, and recursion depth as parameters
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
	my $link_pattern = pattern::get_link_pattern();
	scan: while($page =~ /$link_pattern/gi)
	{
		my $curr_link = $1;
		my $page_name = $2;
		next scan if(!util::validate_link($curr_link));
		
		$curr_link = util::clean_link($seed,$curr_link);
		
    	next scan if(exists $list{$curr_link});
    	print $depth." ".$seed." => ".$curr_link." ".$page_name."\n";
    	if ($curr_link =~ /pattern::get_listing_pattern()/)
    	{
    		$ads{$curr_link} = $page_name;
    	}
    	else 
    	{
			basic_spider::basic_spider($curr_link,$page_name,$depth+1);
    	}
	}
}
1;