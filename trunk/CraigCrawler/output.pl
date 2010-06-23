package output;

use strict;
use warnings;
use FileHandle;
use File::Find;
require settings;

my $output_filename;
my $fh; #for single file output
my %fh_map = ();
my $fh_limit = 200;

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
	
	#TODO: move these output styles to settings somehow?
	my $screen_output = $seed." => ".$curr_link." ".$page_name."\n";
	my $file_output = $curr_link." ".$page_name."\n";
	
	my $ret1 = print_to_file($file_output, $is_ad, $seed);
	my $ret2 = print_to_screen($screen_output);
	return ($ret1 && $ret2);
}

sub print_to_file
{
	#TODO: There might be redundant logic in this method, check
	my $ret1=0;
	my $ret2=0;	

	my $print_ad = settings::save_only_ads() ? $_[1] : 1; #If only printing ads, check if input is flagged as an AD.
	return 1 if (!$print_ad); #Variable is poorly named
	if( settings::output_file() )
	{
		$ret1 = settings::output_file() && (defined $fh && $print_ad ? $fh->print($_[0]) : 1);
	}
	if( settings::output_dirs() )
	{
		$ret2 = print_to_dir(@_);
	}
	return $ret1 && $ret2;
}

sub print_to_dir
{
	#TODO: Complete, but be sure to consider the fact that some links are redirects..
	
	my $file_output = $_[0];
	my $is_ad = $_[1]; #This actually should ALWAYS be 1, because of settings enforcement.
	my $seed = $_[2];
		
	#Decided to use URI, instead of simply cutting off http:// and using that as a DIR.
	my $uri = URI->new($seed)->canonical();
	my $authority = $uri->authority;
	my @path_seg = $uri->path_segments;
	
	shift @path_seg if !$path_seg[0]; #if is absolute path, always will be though.
	unshift @path_seg, $authority;
	unshift @path_seg, "output";
	my $path = join "/", @path_seg;
	
	if ( !util::dir_exists($path) )
	{
		my $result = util::cc_mkdir(@path_seg);
		print "result: ".$result."\n";
		return 0 if !$result && print "Error making path ".$path."\n";
	}
	$path = join "/",$path,"results";
	#print $path."\n";
	if (!(exists $fh_map{$path}))
	{
		$fh_map{$path} = FileHandle::new();
		$fh_map{$path}->open($path, ">>");
	}
	$fh_map{$path}->print($file_output);
	util::close_file_map(\%fh_map) if scalar keys %fh_map >= $fh_limit;
	return 1;
}

sub print_to_screen
{
	return (settings::print_screen() ? print($_[0]) : 1);
}

1;