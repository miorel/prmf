package output;

use strict;
use warnings;
use FileHandle;
use File::Find;
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
	#This method prints the same content to file and to screen
	my $ret1 = print_to_file(@_);
	my $ret2 = print_to_screen($_[0]);
	return ($ret1 && $ret2);
}

sub new_special_line
{
	#This method prints differently to file than it does to screen..
	my ($is_ad,$seed,$curr_link,$page_name) = (@_);
	my $screen_output = $seed." => ".$curr_link." ".$page_name."\n";
	my $file_output = $curr_link." ".$page_name."\n";
	my $ret1 = print_to_file($file_output, $is_ad, $seed);
	my $ret2 = print_to_screen($screen_output);
	return ($ret1 && $ret2);
}

sub print_to_file
{
	my $print_ad = settings::save_only_ads() ? $_[1] : 1; #If only printing ads, check if input is flagged as an AD.
	return 1 if (!$print_ad); #Variable is named poorly
	if( settings::output_file() )
	{
		my $ret1 = settings::output_file() && (defined $fh && $print_ad ? $fh->print($_[0]) : 1);
		return $ret1;
	}
	else
	{
		return print_to_dir(@_);
	}
}

sub print_to_dir
{
	my $file_output = $_[0];
	my $is_ad = $_[1];
	my $seed = $_[2];
	if( $is_ad )
	{
		# separate filename from directory
		# check if directory exists
			#if not, record directory location
		# append seed link to the advertisements list in the folder!
	}
	else #Is a directory (actually its an index page, which we don't want to store)
	{
		#check if directory exists
			#if not, make it!
	}
}

sub print_to_screen
{
	return (settings::print_screen() ? print($_[0]) : 1);
}

1;