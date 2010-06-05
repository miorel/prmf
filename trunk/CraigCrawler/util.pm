package util;

use warnings;
use strict;
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
      #Does not support going up directories at the moment

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
    			$seed =~ s"(/[^/]*)$"$curr_link"; #replace the folder we are currently in with the new folder, for $curr_links like /ggg/
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
1;
