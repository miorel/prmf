#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
my $dbh = MindMeld->dbh;

print MindMeld->header;

my $show_question = 0;

my $grade_is_min = sprintf('%1$s = (SELECT MIN(%1$s) FROM questions WHERE %2$s = 1)', MindMeld::GRADE_FROM_QUESTIONS, MindMeld::ACTIVE_FROM_QUESTIONS);
my $sth = $dbh->prepare(sprintf("SELECT %s, %s, %s, %s, %s, %s FROM questions WHERE %s = 1 AND $grade_is_min ORDER BY RANDOM() LIMIT 1",
	MindMeld::QUESTION_ID_FROM_QUESTIONS,
	MindMeld::QUESTION_TEXT_FROM_QUESTIONS,
	MindMeld::ANSWER_FROM_QUESTIONS,
	MindMeld::CATEGORY_NAME_FROM_QUESTIONS,
	MindMeld::CATEGORY_ID_FROM_QUESTIONS,
	MindMeld::GRADE_FROM_QUESTIONS,
	MindMeld::ACTIVE_FROM_QUESTIONS,
));
$sth->execute;
my($qid, $question, $answer, $category, $cid, $grade) = $sth->fetchrow_array;
if(defined($qid)) {
	print $cgi->p("Category: " . $cgi->a({-href => "show_category.pl?cid=$cid"}, $category));
	print $cgi->div({-style => 'text-align:center;'},
		$cgi->p({-id => 'q'}, $question),
		$cgi->p({-id => 'a', -style => 'visibility:hidden;'}, $answer),
		$cgi->p({-id => 'sa'}, $cgi->a({-href => 'javascript:void(0);', -onclick => q{javascript:document.getElementById('sa').style.display='none';document.getElementById('a').style.visibility='visible';document.getElementById('gr').style.display='block';}}, 'Show Answer')),
		$cgi->p({-id => 'gr', -style => 'display:none;'}, "Grade: " . join(" ", map {$cgi->a({-href => "set_grade.pl?qid=$qid&grade=$_"}, $_)} 0..5)),
	);
}
else {
	print $cgi->p({-style => 'text-align:center;'}, sprintf("There are no active categories. Perhaps you'd like to enable some in the %s?", $cgi->a({-href => 'options.pl'}, 'options')));
}

print MindMeld->footer;

