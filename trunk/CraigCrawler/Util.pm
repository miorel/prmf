#!/usr/bin/perl
package Util;
my @bad_patterns = ( qr"^/$", qr/mailto/, qr/https/, qr/blog\./, qr"/about/", qr"^(/?cal)", qr/forums[\/\.]?/, qr/cgi-bin/, qr/(index[0-9]+\.html)$/);
my @good_patterns = ( qr/craigslist\.org/, qr/^(?!http)/ );
sub validate_link
{
		my $curr_link = $_[0];
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
