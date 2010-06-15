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
	my $ret1 = print_to_file($file_output, $is_ad);
	my $ret2 = print_to_screen($screen_output);
}

sub print_to_file
{
	#Problem .. File gets corrupted for some reason (Probably because its so large? (5MB+))
	#Supposedly Perl will handle buffering on its own and flush when it's appropriate
	#The way the conditional ad printing is handled is much too messy to be happy with.
	
	my $print_ad = settings::save_only_ads() ? $_[1] : 1; #If only printing ads, check if input is flagged as an AD.
	my $ret1 = settings::output_file() && (defined $fh && $print_ad ? $fh->print($_[0]) : 1);
}

sub print_to_screen
{
	return (settings::print_screen() ? print($_[0]) : 1);
}

1;