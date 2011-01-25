#!/usr/bin/perl
#
# Script to get a list of stock tickers from Google Finance
#
# Copyright (C) 2011 Miorel-Lucian Palii
# This code is licensed under the GPLv3 or any later version.
# <http://www.gnu.org/licenses/gpl.html>
#

use warnings;
use strict;

use JSON;
use LWP::Simple;

my($fh, $json);

my $url = "http://www.google.com/finance?start=0&num=10000&gl=us&hl=en&q=((exchange%3ANYSE) OR (exchange%3ANASDAQ) OR (exchange%3AAMEX)) []&restype=company&output=json&noIL=1";
my $file = "stocks.json";

if(-e $file) {
	open $fh, $file or die "Failed to open $file for reading";
	$json = join('', <$fh>);
	close $fh;
}
else {
	$json = get($url);
	open $fh, ">$file" or die "Failed to open $file for writing";
	print $fh $json;
	close $fh;
}

$json =~ s/\\x(..)/\\u00$1/g;
my @stocks = @{ decode_json($json)->{searchresults} };

for(@stocks) {
	printf("%s:%s\n", $_->{exchange}, $_->{ticker});
}
