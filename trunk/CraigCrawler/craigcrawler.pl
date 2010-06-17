#!/usr/bin/perl
# Made by Rodrigo Salazar

use warnings;
use strict;
use Config;
require settings;
require "basic_spider.pl";
require "threaded_spider.pl";
require "poe_spider.pl";
require "output.pl";

#Current Issues: (In order of priority)
#0.Fix problem with links involving geo.craigslist.org and its sub folders...
#1.Organization of data gathered, including the creation of folders for each state/city.
#2.Implement new spider method using POE and LWP::UserAgent::POE !!!
#3.SOCKS Proxy support + Nice-Bot support (Delays, etc.)
#4.Need more control over scanning, possibly the option to scan as far as 2 pages back, or even by day.
#5.Code is not very slick.

my $url = "http://craigslist.org";
my $site_name = "craigslist";

die if !settings::valid_settings() && output::new_line("Bad settings\n");

my $open_file = output::prepare_file() if settings::output_file();
output::new_line("Error creating/opening file\n") if $open_file == -1;

if( settings::use_poe() )
{
	#Insert command here!!
}
elsif ( settings::use_threads() ) 
{
	die if !$Config{useithreads} && output::new_line("Threading not enabled in your perl\n");
	threaded_spider::threaded_spider($url,$site_name);
}
else
{
	basic_spider::basic_spider($url,$site_name);
}


