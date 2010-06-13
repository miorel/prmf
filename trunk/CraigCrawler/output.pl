package output;

use strict;
use warnings;
use FileHandle;
require settings;

my $output_filename;
my $fh;

sub prepare_file
{
	return 0 if !settings::output_file();
	
	$output_filename = settings::output_filename();
	$fh = FileHandle->new;
	$fh->open("$output_filename", "w");
	return -1 if !(defined $fh);
	
	return 1;
}

sub new_line
{
	#Problem .. File gets corrupted for some reason (sometimes)
	
	#Supposedly Perl will handle buffering on its own and flush when it's appropriate
	my $ret1 = settings::output_file() && defined $fh ? $fh->print($_[0]) : 1;
	my $ret2 = settings::print_output() ? print($_[0]) : 1;

	return ($ret1 && $ret2);
}

1;