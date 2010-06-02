package util;

require pattern;

sub validate_link
{
		my $curr_link = $_[0];
		my @bad_pattens = pattern::get_bad_patterns();
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
		my $seed = $_[0];
		my $curr_link = $_[1];
		
	    if($curr_link =~ /http/)
    	{
    		$curr_link =~ s"/$""; #remove trailing slashes 
    		if($curr_link =~ m"^(http://)www\.(.*)" )
    		{
    			$curr_link = $1.$2;
    		}
    	}
    	else
    	{
	    	$curr_link =~ s"/""; #replace slashes with empty string, and append a single slash to front
	    	$curr_link = "/".$curr_link;
	    	$curr_link = $seed.$curr_link;
    	}
    	return $curr_link;
}
1;
