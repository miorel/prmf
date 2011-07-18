#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld::cgi();

my $cid = $cgi->param('cid');
unless(defined($cid)) {
	print $cgi->redirect(-uri => 'index.pl?action=study', -status => 302);
}
else {
	my $dbh = MindMeld::dbh();

	print MindMeld::header();
	
	my $sth = $dbh->prepare(sprintf('SELECT %s, %s FROM questions WHERE %s = ?',
		MindMeld::QUESTION_TEXT_FROM_QUESTIONS,
		MindMeld::QUESTION_ID_FROM_QUESTIONS,
		MindMeld::CATEGORY_ID_FROM_QUESTIONS,
	));
	$sth->execute($cid);
	
	my($question, $qid);
	$sth->bind_columns(\$question, \$qid);
	my $count = 0;
	while($sth->fetch) {
		print $cgi->p(++$count . ". " . $cgi->a({-href => "show_question.pl?qid=$qid"}, $question));
	}
	if($count == 0) {
		print $cgi->p("No such category :(");
	}
	
	print MindMeld::footer();
}

