#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
print $cgi->redirect(-uri => 'study.pl', -status => 302);

my $dbh = MindMeld->dbh;

my $width = 400;

my $qid = $cgi->param('id');
my $gc = $cgi->param('gc');
if(defined($gc) && defined($qid) && $gc =~ /^\??(\d{1,5})/) {
	my $grade = $1;
	if($grade >= 0 && $grade < $width) {
		$grade = $grade / ($width - 1) * 5;
		$dbh->prepare("UPDATE questions SET grade = ? WHERE id = ?")->execute($grade, $qid);
	}
}

