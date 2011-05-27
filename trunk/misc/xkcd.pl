#!/usr/bin/perl
#
# Copyright (C) 2011 Miorel-Lucian Palii <mlpalii@gmail.com>
# Last update May 26, 2011
#

use warnings;
use strict;

use Carp;
use Data::Dumper;
use Date::Format;
use LWP::UserAgent;

my $filename = 'xkcd_dump.txt';

my $d = Data::Dumper->new([])
	->Terse(1)
	->Indent(0)
	->Pair(',')
	->Quotekeys(1)
	->Useqq(1)
;

my $ua = LWP::UserAgent->new;
$ua->timeout(10);
$ua->env_proxy;

my $fh;
my $count = 0;
if(-e $filename) {
	open $fh, "<$filename" or croak("Couldn't open $filename for reading");
	my @lines = (<$fh>);
	close $fh;
	$count = eval($lines[-1])->{n} if @lines; # evil?
}

print "Last comic read was $count\n";

for(;;) {
	++$count;
	next if $count == 404; # Randall being clever
	my $data = undef;
	my $content = get_xkcd($count) or last;
	my @elem = map {{/(\w+)\s*=\s*\"([^"]*)\"/sig}} get_elements_by_type($content, 'img');
	for(@elem) {
		next unless $_->{src} =~ m[\A\Qhttp://imgs.xkcd.com/comics/\E];
		my $hash = {title => $_->{alt}, url => $_->{src}, caption => $_->{title}, n => $count};
		print "Read comic $count, \"$hash->{title}\"\n";
		$data = $d->Values([$hash])->Dump;
		last;
	}
	croak "Didn't get any data for comic $count" unless defined $data;
	open $fh, ">>$filename" or croak("Couldn't open $filename for appending");
	printf($fh "%s\n", $data);
	close $fh;
	sleep 10; # being nice to the server... might have used LWP::RobotUA but I didn't feel like filling out its mandatory constructor arguments
}

sub get_xkcd {
	my $n = shift;

	my $response = $ua->get(sprintf('http://xkcd.com/%d/', $n));

	my $ret = undef;

	if($response->is_success) {
		$ret = $response->decoded_content;
	}
	else {
		carp($response->status_line);
	}

	return $ret;
}

# a hackish "get elements by type" from HTML, not suitable for general purposes
sub get_elements_by_type {
	my($html, $type) = @_;
	my @ret = ();
	push @ret, $_ for $html =~ /<\s*\Q$type\E[^>]*?\/?>/sig;
	return @ret;
}
