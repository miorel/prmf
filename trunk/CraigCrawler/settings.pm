package settings;

use warnings;
use strict;

my %Config = (
	#Basic options
	"threading_on" => 1,
	"threads_number" => 8,
	
	#Hash checking settings
	"check_headers_hash" => 0,
	"check_page_hash" => 1,
	
	#File settings
	"print_screen" => 1,
	"output_file" => 1,
	"output_filename" => "list.txt",
	"save_only_ads" => 1,
);

sub save_only_ads
{
	return $Config{"save_only_ads"};
}

sub print_screen
{
	return $Config{"print_screen"};
}

sub output_file
{
	return $Config{"output_file"};
}

sub output_filename
{
	return $Config{"output_filename"};
}
sub threads_number
{
	return $Config{"threads_number"};
}

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