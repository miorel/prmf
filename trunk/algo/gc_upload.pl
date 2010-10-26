#!/usr/bin/perl

use warnings;
use strict;

use Date::Format;
use Date::Language;
use File::stat;
use LWP::UserAgent;

my $config_file = "googlecode.config";

unless(-f $config_file) {
	print STDERR "Missing configuration file: $config_file\n";
	print STDERR "Please create it following the model of $config_file.example\n";
	exit 1;
}

our($user, $pass, $project);
require $config_file;

my @labels = qw(AlgorithmPacket);

my $packet = "algo.pdf";
my $packet_desc = "Algorithm packet pdf";
my $archive = "algo.zip";
my $archive_desc = "Algorithm packet source archive";

gc_upload($user, $pass, $project, $packet_desc . " - " . nice_date(stat($packet)->mtime), \@labels, $packet, date_label($packet));
gc_upload($user, $pass, $project, $archive_desc . " - " . nice_date(stat($archive)->mtime), \@labels, $archive, date_label($archive));

sub nice_date {
	my $df = new Date::Language('English');
	my $ret = $df->time2str("%B %e, %Y", shift);
	$ret =~ s/\s+/ /g; # because the %e format will have a space for 1-digit dates
	return $ret;
}

sub date_label {
	my $file = shift;
	my $mtime = stat($file)->mtime;
	my $str = time2str("%Y%m%d", $mtime);
	$file =~ s/(\.|$)/-$str$1/s;
	return $file;
}

sub gc_upload {
	my($user, $pass, $project, $summary, $labels, $file_local, $file_remote) = @_;
	my $ua = new LWP::UserAgent;
	my $url = "https://$user:$pass\@$project.googlecode.com/files";
	print "Uploading $file_local to $file_remote on Google Code...\n";
	my $res = $ua->post($url,
		Content_Type => 'form-data',
		Content => [ summary => $summary, filename => [$file_local, $file_remote], map {('label', $_)} @$labels ],
	);
	printf("Status: %s %s\n", $res->status_line, $res->is_success ? ":D" : ":(");
}
