package pattern;

use warnings;
use strict;

# For some reason this doesn't want to match the # sign correctly.. (pages with # link to themselves).
my @bad_patterns = ( qr/#/, qr"^/$", qr/mailto/, qr/https/, qr/blog\./, qr"about/", qr"^(/?cal)", qr/forums[\/\.]?/, qr/cgi-bin/, qr/(index[0-9]+\.html)$/);
my @good_patterns = ( qr/craigslist\.org/, qr/^(?!http)/ );
my $link_pattern = qr/<a href=\"([^\"]*)\">([^<]*)<\/a>/;
my $listing_pattern = qr"(/[0-9]+\.html)$";

sub get_listing_pattern
{
	return $listing_pattern;
}

sub get_link_pattern
{
	return $link_pattern;
}

sub get_bad_patterns
{
	return @bad_patterns;
}

sub get_good_patterns
{
	return @good_patterns;
}
1;