#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld::cgi();
my $dbh = MindMeld::dbh();

print MindMeld::header();

my $show_question = 0;

my $action = $cgi->param('action');

if(!defined($action)) {
print q`<p><strong>MindMeld</strong> will be a system for training your mind like a Vulcan. Not becoming an emotionless automaton is left as an exercise for the user.</p>
<p><iframe width="853" height="510" src="https://www.youtube.com/embed/ds7dBoWLlrc?rel=0" frameborder="0" allowfullscreen></iframe></p>`;
}

if(defined($action) && $action eq 'set_grade') {
	my $grade = $cgi->param('grade');
	my $id = $cgi->param('id');
	if(defined($grade) && defined($id)) {
		# needs error checking
		$dbh->prepare("UPDATE questions SET grade = ? WHERE id = ?")->execute($grade, $id);
	}
	$show_question = 1;
}

if(defined($action) && $action eq 'study') {
	$show_question = 1;
}

if(defined($action) && $action eq 'stats') {
	my $sth = $dbh->prepare("SELECT ROUND(grade), COUNT(*) FROM questions GROUP BY ROUND(grade) ORDER BY ROUND(grade)");
	my($total, $count, $grade) = 0;
	$sth->execute;
	$sth->bind_columns(\$grade, \$count);
	while($sth->fetch) {
		print $cgi->p("Grade $grade: $count");
		$total += $count;
	}
	print $cgi->p("Total: $total");
	$show_question = 0;
}

if(defined($action) && $action eq 'opts') {
	print $cgi->start_form(-action => 'index.pl');
	if(lc($cgi->request_method) eq 'post') {
		my $sth = $dbh->prepare('SELECT id FROM categories');
		my $id;
		$sth->execute;
		$sth->bind_columns(\$id);
		while($sth->fetch) {
			my $cn = "cat_${id}_active";
			my $val = $cgi->param($cn);
			$dbh->prepare('UPDATE categories SET active = ? WHERE id = ?')->execute((defined($val) && $val eq '1'), $id);
		}
		print $cgi->p('Active categories successfully updated!');
	}
	my $sth = $dbh->prepare('SELECT id, name, active FROM categories ORDER BY name');
	my($id, $name, $active);
	$sth->execute;
	$sth->bind_columns(\$id, \$name, \$active);
	while($sth->fetch) {
		my $cn = "cat_${id}_active";
		print $cgi->p($cgi->checkbox(-name => $cn, -checked => $active, -label => $name, -value => 1, -override => 1));
	}
	print $cgi->hidden(-name => 'action', -default => 'opts', -override => 1);
	print $cgi->submit('Update');
	print $cgi->end_form;
	$show_question = 0;
}

if($show_question) {
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
			$cgi->p({-id => 'sa'}, $cgi->a({-href => 'javascript:void(0);', -onclick => q{javascript:document.getElementById('sa').style.display='none';document.getElementById('a').style.display='block';document.getElementById('gr').style.display='block';}}, 'Show Answer')),
			$cgi->p({-id => 'a', -style => 'display:none;'}, $answer),
			$cgi->p({-id => 'gr', -style => 'display:none;'}, "Grade: " . join(" ", map {$cgi->a({-href => "index.pl?action=set_grade&id=$qid&grade=$_"}, $_)} 0..5)),
		);
	}
	else {
		print $cgi->p({-style => 'text-align:center;'}, sprintf("There are no active categories. Perhaps you'd like to enable some in the %s?", $cgi->a({-href => 'index.pl?action=opts'}, 'options')));
	}
}

print MindMeld::footer();

