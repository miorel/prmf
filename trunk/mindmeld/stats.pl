#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
my $dbh = MindMeld->dbh;

print MindMeld->header;

my $user = MindMeld->user;

if(defined $user) {
	print $cgi->p("Studied questions:");
	my $sth = $dbh->prepare_cached("SELECT ROUND(grade), COUNT(*) FROM user_question_rels WHERE user = ? GROUP BY ROUND(grade) ORDER BY ROUND(grade)");
	my($total, $count, $grade) = 0;
	$sth->execute($user);
	$sth->bind_columns(\$grade, \$count);
	while($sth->fetch) {
		print $cgi->p("Grade $grade: $count");
		$total += $count;
	}
	print $cgi->p("Total: $total");
}
else {
	print $cgi->p("This page requires you to be logged in. <a href=\"index.pl\">Register</a> an account.");
}

print MindMeld->footer;

