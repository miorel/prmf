package settings;

use warnings;
use strict;

my %Config = (
	#Basic options, only use one of use_poe and use_threads obviously (or none)
	"use_poe" => 0,
	"use_threads" => 1,
	"threads_number" => 4,
	
	#Hash checking settings
	"check_headers_hash" => 0,
	"check_page_hash" => 1,
	
	#File settings
	"print_screen" => 1,
	"output_file" => 0,
	"output_filename" => "list.txt",
	"output_dirs" => 1,
	"save_only_ads" => 1,
);

sub valid_settings
{
	my $method = use_poe() + use_threads() <= 1 ? 1 : 0;
	my $store = output_file() + output_dirs() <=1 ? 1 : 0;
	my $purpose = output_file() + output_dirs() + print_screen() >= 1 ? 1 : 0;
	my $ad_dirs = !(save_only_ads() xor output_dirs());
	return ($method && $store && $purpose && $ad_dirs);
}

sub use_poe
{
	return $Config{"use_poe"};
}

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

sub output_dirs
{
	return $Config{"output_dirs"};
}
sub output_filename
{
	return $Config{"output_filename"};
}
sub threads_number
{
	return $Config{"threads_number"};
}

sub use_threads
{
	return $Config{"use_threads"};
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