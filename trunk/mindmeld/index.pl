#!/usr/bin/perl

use warnings;
use strict;

use CGI;
use DBI;

my $dbh = DBI->connect("dbi:SQLite:dbname=states.db", "", "");
$dbh->do("CREATE TABLE IF NOT EXISTS flashcards (question TEXT, answer TEXT, grade REAL DEFAULT 0)");

#srand(time ^ $$ ^ unpack "%L*", `ps axww | gzip -f`);

my $cgi = CGI->new;

my $info_str = 'mindmeld beta 201106261815';

print $cgi->header, $cgi->start_html($info_str);

print $cgi->p(join(" | ", $cgi->a({-href => 'index.pl?action=stats'}, "Statistics"), $cgi->a({-href => 'index.pl'}, "Study"))), $cgi->hr;

my $show_question = 1;

my $action = $cgi->param('action');
if(defined($action) && $action eq 'set_grade') {
	my $grade = $cgi->param('grade');
	my $id = $cgi->param('id');
	if(defined($grade) && defined($id)) {
		$dbh->prepare("UPDATE flashcards SET grade = ? WHERE rowid = ?")->execute($grade, $id);
	}
}

if(defined($action) && $action eq 'stats') {
	my $sth = $dbh->prepare("SELECT ROUND(grade), COUNT(*) FROM flashcards GROUP BY ROUND(grade) ORDER BY ROUND(grade);");
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

if($show_question) {
	my $sth = $dbh->prepare("SELECT rowid, question, answer, grade FROM flashcards WHERE grade IS (SELECT MIN(grade) FROM flashcards) ORDER BY RANDOM() LIMIT 1");
	$sth->execute;
	my($id, $question, $answer, $grade) = $sth->fetchrow_array;
	print $cgi->div({-style => 'text-align:center;'},
		$cgi->p({-id => 'q'}, $question),
		$cgi->p({-id => 'sa'}, $cgi->a({-href => 'javascript:void(0);', -onclick => q{javascript:document.getElementById('sa').style.display='none';document.getElementById('a').style.display='block';document.getElementById('gr').style.display='block';}}, 'Show Answer')),
		$cgi->p({-id => 'a', -style => 'display:none;'}, $answer),
		$cgi->p({-id => 'gr', -style => 'display:none;'}, "Grade: " . join(" ", map {$cgi->a({-href => "index.pl?action=set_grade&id=$id&grade=$_"}, $_)} 0..5)),
	);
}

print $cgi->hr, $cgi->p($info_str), $cgi->p('Copyright &copy; 2011 Miorel-Lucian Palii');
print $cgi->end_html;

