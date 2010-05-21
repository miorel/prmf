#!/usr/bin/perl

use warnings;
use strict;
use LWP::Simple;

my $url = "http://craigslist.org";
my $content = get($url);

print $content; 
