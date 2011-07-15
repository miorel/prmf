package Mindmeld;

use warnings;
use strict;

use CGI;
use DBI;

use constant QUESTION_ID_FROM_QUESTIONS => 'questions.rowid';
use constant QUESTION_TEXT_FROM_QUESTIONS => 'questions.q';
use constant ANSWER_FROM_QUESTIONS => 'questions.a';
use constant GRADE_FROM_QUESTIONS => 'questions.grade';
use constant CATEGORY_ID_FROM_QUESTIONS => 'questions.cat';
use constant CATEGORY_NAME_FROM_CATEGORIES => 'categories.name';
use constant CATEGORY_ID_FROM_CATEGORIES => 'categories.rowid';
use constant CATEGORY_NAME_FROM_QUESTIONS => sprintf("(SELECT %s FROM categories WHERE %s = %s)", CATEGORY_NAME_FROM_CATEGORIES, CATEGORY_ID_FROM_CATEGORIES, CATEGORY_ID_FROM_QUESTIONS);
use constant ACTIVE_FROM_CATEGORIES => 'categories.active';
use constant ACTIVE_FROM_QUESTIONS => sprintf("(SELECT %s FROM categories WHERE %s = %s)", ACTIVE_FROM_CATEGORIES, CATEGORY_ID_FROM_CATEGORIES, CATEGORY_ID_FROM_QUESTIONS);

my $info_str = 'mindmeld beta 201107141403';
my($cgi, $dbh);

sub cgi {
	$cgi = CGI->new;
}

sub dbh {
	$dbh = DBI->connect("dbi:SQLite:dbname=mm.db", "", "")
		unless defined $dbh;
	return $dbh;
}

sub sql {
	
}

sub ensure_schema {
	my $dbh = dbh();
	$dbh->do("CREATE TABLE IF NOT EXISTS questions (q TEXT, a TEXT, cat INTEGER, grade REAL DEFAULT 0)");
	$dbh->do("CREATE TABLE IF NOT EXISTS categories (name TEXT UNIQUE NOT NULL, active BOOLEAN DEFAULT 1 NOT NULL)");
}

sub add_question {
	my($q, $a, $cat) = @_;
	my $dbh = dbh();
	my $sth;
	
	$sth = $dbh->prepare_cached("INSERT OR IGNORE INTO categories (name) VALUES (?)");
	$sth->execute($cat);
	$sth = $dbh->prepare_cached("INSERT INTO questions (q, a, cat) VALUES (?, ?, (SELECT rowid FROM categories WHERE name = ?))");
	$sth->execute($q, $a, $cat);
}

sub header {
	my $cgi = cgi();
	my @actions = (
		['study' => 'Study'],
		['opts' => 'Options'],
		['stats' => 'Statistics'],
	);
	return
		$cgi->header .
		$cgi->start_html(info_str()) .
		$cgi->p(join(" | ", map {$cgi->a({-href => "index.pl?action=$_->[0]"}, $_->[1])} @actions)) .
		$cgi->hr;
}

sub footer {
	my $cgi = cgi();
	return
		$cgi->hr .
		$cgi->p(info_str()) .
		$cgi->p('Copyright &copy; 2011 Miorel-Lucian Palii') .
		$cgi->end_html;
}

sub info_str {
	return $info_str;
}

=head1 AUTHOR

Miorel-Lucian Palii E<lt>mlpalii@gmail.comE<gt>

=head1 COPYRIGHT AND LICENSE

Copyright (C) 2011 by Miorel-Lucian Palii

This library is free software; you can redistribute it and/or modify it under the terms of either: the GNU General Public License as published by the Free Software Foundation; or the Artistic License.

See L<perlartistic> or L<http://dev.perl.org/licenses/> for more information.

=cut

1;

__END__
