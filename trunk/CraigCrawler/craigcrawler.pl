#!/usr/bin/perl
# Made by Rodrigo Salazar

use warnings;
use strict;
use Config;
require "basic_spider.pl";
require "threaded_spider.pl";

#Current Issues: (In order of priority)
#1.Get websites downloading using multiple threads, threads are locking up.. not sure if being done properly.
#2.Organization of data gathered, currently the links are separated as only ads or not ads.
#3.Need more control over scanning, possibly the option to scan as far as 2 pages back, or even by day.
#4.Code is not very slick.

my $url = "http://craigslist.org";
my $site_name = "craigslist";
my $threading_off = 1;

if ($Config{useithreads} && !$threading_off) 
{
	threaded_spider::threaded_spider($url,$site_name,0);
}
else
{
	basic_spider::basic_spider($url,$site_name,0);
}


