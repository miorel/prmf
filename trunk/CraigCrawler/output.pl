package output;

use strict;
use warnings;
use FileHandle;

my $output_filename;
my $fh;


sub prepare_file
{
	$output_filename = $_[0];
	$fh = FileHandle->new;
	$fh->open("<$output_filename");
	print "Error opening file\n" if defined !$fh;
}

sub add_line
{
	
}

sub flush
{
	
}


1;