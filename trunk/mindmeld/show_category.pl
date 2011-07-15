#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use Mindmeld;

my $cgi = Mindmeld::cgi();

my $cid = $cgi->param('cid');
unless(defined($cid)) {
	print $cgi->redirect(-uri => 'index.pl?action=study', -status => 302);
}
else {
	my $dbh = Mindmeld::dbh();

	print Mindmeld::header();
	
	my $sth = $dbh->prepare(sprintf('SELECT %s, %s FROM questions WHERE %s = ?',
		Mindmeld::QUESTION_TEXT_FROM_QUESTIONS,
		Mindmeld::QUESTION_ID_FROM_QUESTIONS,
		Mindmeld::CATEGORY_ID_FROM_QUESTIONS,
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
	
	print Mindmeld::footer();
}

