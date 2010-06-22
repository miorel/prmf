package util;

use warnings;
use strict;
use URI;
require pattern;

sub validate_link
{
		my $curr_link = $_[0];
		my $seed = $_[1];
		my @bad_patterns = pattern::get_bad_patterns();
		my @good_patterns = pattern::get_good_patterns();
		
		foreach my $bad (@bad_patterns)
		{
			return 0 if($curr_link =~ /$bad/);
		}
		
		foreach my $good (@good_patterns)
		{
			return 1 if($curr_link =~ /$good/);
		}
		return 0;
}

sub clean_link
{
       #Does not support going up directories at the moment, but craigslist does not so not important

	   #curr_link is the new link we found while parsing the current file
		#seed is the actual file where the link was found in
		my $seed = $_[0];
		my $curr_link = $_[1];
		
	    if($curr_link =~ /http/)
    	{
    		if($curr_link =~ m"^(http://)www\.(.*)" ) #remove www
    		{
    			$curr_link = $1.$2;
    		}
    	}
    	else
    	{
    		if($curr_link =~ m"^/") #if link starts with a slash, meaning a different folder at same level
    		{
    			$seed =~ s"(/.*)$"$curr_link"; #replace the folder we are currently in with the new folder, for $curr_links like /ggg/
				$curr_link = $seed;
    		}
    		else #if link doesnt start with a slash, (likely then ends with a slash, indicating a subdirectory)
    		{
	    		$curr_link =~ s"/""; #replace slashes with empty string, and append a single slash to front
	   			$curr_link = "/".$curr_link;
	    		$curr_link = $seed.$curr_link;
    		}
    	}
    	$curr_link =~ s"/$""; #remove trailing slashes 
    	return $curr_link;
}

#This method might just be erased, its bad
sub get_filename
{
	#All this method does is get filename from a LINK, but only if one is exposed
	#We only want a filename if its exposed since otherwise I do not want to store such a file.
	my $link = URI->new($_[0])->path;
	$link =~ m#/([^/]*)$#; #cut off the relative path
	$link = $1;
	return $link;
}

sub dir_exists
{
	my $dir = $_[0];
	return (-d $dir ? 1 : 0); 
}

sub file_exists
{
	my $file = $_[0];
	return (-e $file ? 1 : 0);
}

sub cc_mkdir
{
	#This method takes an array of folder names, starting with the lowest depth.
	#mkdir a/b would fail if 'a' does not already exist, this method should not..
	my $result = 1;
	my @dir_arr = @_;

	print scalar @dir_arr;

	my $rel_path = shift @dir_arr;
	mkdir $rel_path if (!(-d $rel_path));

	foreach my $d (@dir_arr)
	{
		$rel_path = join "/",$rel_path,$d;
		print $rel_path."\n";
		if(!dir_exists($rel_path))
		{
			my $new_result = mkdir $rel_path;
			$result = ($result && $new_result ? 1 : 0);
			return 0 if !$result;
		}
	}
	return 1;
}

1;
