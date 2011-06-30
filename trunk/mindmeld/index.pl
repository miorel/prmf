#!/usr/bin/perl

use warnings;
use strict;

use CGI;
use DBI;

my $dbh = DBI->connect("dbi:SQLite:dbname=mm.db", "", "");
$dbh->do("CREATE TABLE IF NOT EXISTS questions (q TEXT, a TEXT, cat TEXT, grade REAL DEFAULT 0)");
$dbh->do("CREATE TABLE IF NOT EXISTS categories (name TEXT UNIQUE NOT NULL, active BOOLEAN DEFAULT 1 NOT NULL)");

#srand(time ^ $$ ^ unpack "%L*", `ps axww | gzip -f`);

my $cgi = CGI->new;

my $info_str = 'mindmeld beta 201106262030';

print $cgi->header, $cgi->start_html($info_str);

my @actions = (
	['study' => 'Study'],
	['opts' => 'Options'],
	['stats' => 'Statistics'],
);
print $cgi->p(join(" | ", map {$cgi->a({-href => "index.pl?action=$_->[0]"}, $_->[1])} @actions)), $cgi->hr;

my $show_question = 1;

my $action = $cgi->param('action');
if(defined($action) && $action eq 'set_grade') {
	my $grade = $cgi->param('grade');
	my $id = $cgi->param('id');
	if(defined($grade) && defined($id)) {
		# needs error checking
		$dbh->prepare("UPDATE questions SET grade = ? WHERE rowid = ?")->execute($grade, $id);
	}
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
		my $sth = $dbh->prepare("SELECT rowid FROM categories");
		my $id;
		$sth->execute;
		$sth->bind_columns(\$id);
		while($sth->fetch) {
			my $cn = "cat_${id}_active";
			my $val = $cgi->param($cn);
			$dbh->prepare('UPDATE categories SET active = ? WHERE rowid = ?')->execute((defined($val) && $val eq '1'), $id);
		}
		print $cgi->p('Active categories successfully updated!');
	}
	my $sth = $dbh->prepare("SELECT rowid, name, active FROM categories ORDER BY name");
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
	my $qc_is_active = "(SELECT active FROM categories WHERE rowid = questions.cat) = 1";
	my $qc_name = "(SELECT name FROM categories WHERE rowid = questions.cat)";
	my $grade_is_min = "grade = (SELECT MIN(grade) FROM questions WHERE $qc_is_active)";
	my $sth = $dbh->prepare("SELECT rowid, q, a, $qc_name, grade FROM questions WHERE $qc_is_active AND $grade_is_min ORDER BY RANDOM() LIMIT 1");
	$sth->execute;
	my($id, $question, $answer, $category, $grade) = $sth->fetchrow_array;
	if(defined($id)) {
		print $cgi->p("Category: $category");
		print $cgi->div({-style => 'text-align:center;'},
			$cgi->p({-id => 'q'}, $question),
			$cgi->p({-id => 'sa'}, $cgi->a({-href => 'javascript:void(0);', -onclick => q{javascript:document.getElementById('sa').style.display='none';document.getElementById('a').style.display='block';document.getElementById('gr').style.display='block';}}, 'Show Answer')),
			$cgi->p({-id => 'a', -style => 'display:none;'}, $answer),
			$cgi->p({-id => 'gr', -style => 'display:none;'}, "Grade: " . join(" ", map {$cgi->a({-href => "index.pl?action=set_grade&id=$id&grade=$_"}, $_)} 0..5)),
		);
	}
	else {
		print $cgi->p({-style => 'text-align:center;'}, sprintf("There are no active categories. Perhaps you'd like to enable some in the %s?", $cgi->a({-href => 'index.pl?action=opts'}, 'options')));
	}
}

print $cgi->hr, $cgi->p($info_str), $cgi->p('Copyright &copy; 2011 Miorel-Lucian Palii');
print $cgi->end_html;

