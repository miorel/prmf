#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld::cgi();

my $qid = $cgi->param('qid');
unless(defined($qid)) {
	print $cgi->redirect(-uri => 'index.pl?action=study', -status => 302);
}
else {
	my $dbh = MindMeld::dbh();

	print MindMeld::header();
	
	my $sth = $dbh->prepare(sprintf('SELECT %s, %s, %s, %s, %s FROM questions WHERE %s = ?',
		MindMeld::QUESTION_TEXT_FROM_QUESTIONS,
		MindMeld::ANSWER_FROM_QUESTIONS,
		MindMeld::CATEGORY_NAME_FROM_QUESTIONS,
		MindMeld::CATEGORY_ID_FROM_QUESTIONS,
		MindMeld::GRADE_FROM_QUESTIONS,
		MindMeld::QUESTION_ID_FROM_QUESTIONS,
	));
	$sth->execute($qid);
	my($question, $answer, $category, $cid, $grade) = $sth->fetchrow_array;
	if(defined($question)) {
		print $cgi->p("Category: " . $cgi->a({-href => "show_category.pl?cid=$cid"}, $category));
		print $cgi->p("Question: $question");
		print $cgi->p("Answer: $answer");
		print $cgi->p("Grade: $grade");
	}
	else {
		print $cgi->p("Question doesn't exist :(");
	}
	
	print MindMeld::footer();
}

