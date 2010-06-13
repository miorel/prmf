#!/usr/bin/perl
# Made by Rodrigo Salazar

use warnings;
use strict;
use Config;
require settings;
require "basic_spider.pl";
require "threaded_spider.pl";
require "output.pl";

#Current Issues: (In order of priority)
#1.Need to refine the code for the multithreaded spider, specifically the end conditions on the threads..
#2.Organization of data gathered, including the creation of folders for each state/city.
#2.SOCKS Proxy support + Nice-Bot support (Delays, etc.)
#4.Need more control over scanning, possibly the option to scan as far as 2 pages back, or even by day.
#5.Code is not very slick.

my $url = "http://craigslist.org";
my $site_name = "craigslist";

my $open_file = output::prepare_file() if settings::output_file();
output::new_line("Error creating/opening file\n") if $open_file == -1;

if ($Config{useithreads} && settings::threading_on() ) 
{
	threaded_spider::threaded_spider($url,$site_name);
}
else
{
	output::new_line("Threading not enabled in your perl\n") if settings::threading_on();
	basic_spider::basic_spider($url,$site_name);
}


