#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
print $cgi->redirect(-uri => 'study.pl', -status => 302);

my $dbh = MindMeld->dbh;

my $grade = $cgi->param('grade');
my $qid = $cgi->param('id');
if(defined($grade) && defined($qid)) {
	# needs error checking
	$dbh->prepare("UPDATE questions SET grade = ? WHERE id = ?")->execute($grade, $qid);
}

