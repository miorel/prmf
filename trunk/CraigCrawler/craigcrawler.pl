#!/usr/bin/perl
# Made by Rodrigo Salazar

use warnings;
use strict;
use Config;
require settings;
require "basic_spider.pl";
require "threaded_spider.pl";

#Current Issues: (In order of priority)
#1.Get websites downloading using multiple threads, threads are locking up.. not sure if being done properly.
#2.File output for easier testing.
#3.Organization of data gathered, currently the links are separated as only ads or not ads.
#4.Need more control over scanning, possibly the option to scan as far as 2 pages back, or even by day.
#5.Code is not very slick.

my $url = "http://craigslist.org";
my $site_name = "craigslist";

if ($Config{useithreads} && settings::threading_on() ) 
{
	threaded_spider::threaded_spider($url,$site_name,0);
}
else
{
	print "Threading not enabled in your perl\n" if settings::threading_on();
	basic_spider::basic_spider($url,$site_name);
}


