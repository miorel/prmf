package settings;

use warnings;
use strict;

my %Config = (
	"threading_on" => 1,
	"threads_number" => 5,
	
	"check_headers_hash" => 0,
	"check_page_hash" => 1,
	
	"print_output" => 1,
	"output_file" => 1,
	"output_filename" => "list.txt",
);

sub print_output
{
	return $Config{"print_output"};
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