#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
my $dbh = MindMeld->dbh;

print MindMeld->header;

my $sth = $dbh->prepare("SELECT ROUND(grade), COUNT(*) FROM questions GROUP BY ROUND(grade) ORDER BY ROUND(grade)");
my($total, $count, $grade) = 0;
$sth->execute;
$sth->bind_columns(\$grade, \$count);
while($sth->fetch) {
	print $cgi->p("Grade $grade: $count");
	$total += $count;
}
print $cgi->p("Total: $total");

print MindMeld->footer;

