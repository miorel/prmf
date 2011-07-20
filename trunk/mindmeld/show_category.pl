#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;
use MindMeld::Category;
use MindMeld::Question;

my $cgi = MindMeld->cgi;

my $cid = $cgi->param('cid');
unless(defined $cid) {
	print $cgi->redirect(-uri => 'study.pl', -status => 302);
}
else {
	my $dbh = MindMeld->dbh;

	print MindMeld->header;
	
	my $c = MindMeld::Category->retrieve($cid);
	if(defined $c) {
		my $sth = $dbh->prepare('SELECT id FROM questions WHERE category = ?');
		$sth->execute($cid);
	
		my $qid;
		$sth->bind_columns(\$qid);
		my @q;
		push @q, $qid while $sth->fetch;

		my $count = 0;
		for $qid (@q) {
			my $q = MindMeld::Question->retrieve($qid);
			print $cgi->p(++$count . ". " . $cgi->a({-href => "show_question.pl?qid=$qid"}, $q->question));
		}
	}
	else {
		print $cgi->p("Category doesn't exist :(");
	}
	
	print MindMeld->footer;
}

