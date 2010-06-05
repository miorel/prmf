package settings;

use warnings;
use strict;

my %Config = (
	"threading_on" => 0,
	"check_headers_hash" => 1,
	"check_page_hash" => 1
);

sub threading_on
{
	return $Config{"threading_on"};
}

sub check_headers_hash
{
	return $Config{"check_headers_hash"};
}

sub check_page_hash
{
	return $Config{"check_page_hash"};
}
1;