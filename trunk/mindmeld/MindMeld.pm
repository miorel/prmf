package MindMeld;

use warnings;
use strict;

use CGI;
use DBI;

use constant QUESTION_ID_FROM_QUESTIONS => 'questions.id';
use constant QUESTION_TEXT_FROM_QUESTIONS => 'questions.question';
use constant ANSWER_FROM_QUESTIONS => 'questions.answer';
use constant GRADE_FROM_QUESTIONS => 'questions.grade';
use constant CATEGORY_ID_FROM_QUESTIONS => 'questions.category';
use constant CATEGORY_NAME_FROM_CATEGORIES => 'categories.name';
use constant CATEGORY_ID_FROM_CATEGORIES => 'categories.id';
use constant CATEGORY_NAME_FROM_QUESTIONS => sprintf("(SELECT %s FROM categories WHERE %s = %s)", CATEGORY_NAME_FROM_CATEGORIES, CATEGORY_ID_FROM_CATEGORIES, CATEGORY_ID_FROM_QUESTIONS);
use constant ACTIVE_FROM_CATEGORIES => 'categories.active';
use constant ACTIVE_FROM_QUESTIONS => sprintf("(SELECT %s FROM categories WHERE %s = %s)", ACTIVE_FROM_CATEGORIES, CATEGORY_ID_FROM_CATEGORIES, CATEGORY_ID_FROM_QUESTIONS);

my $info_str = 'MindMeld beta 201107192042';
my($cgi, $dbh);

sub cgi {
	$cgi = CGI->new;
}

sub dbh {
	$dbh = DBI->connect('dbi:SQLite:dbname=mm.db', '', '')
		unless defined $dbh;
	return $dbh;
}

sub _select {
	my($package, $statement, @args) = @_;
	my $sth = $package->dbh->prepare_cached($statement);
	$sth->execute(@args);
	my $ret = ($sth->fetchrow_array)[0];
	$sth->fetch; # deactivate
	return $ret;
}

sub header {
	my $cgi = cgi();
	my @actions = (
		['study.pl' => 'Study'],
		['options.pl' => 'Options'],
		['stats.pl' => 'Statistics'],
	);
	
	my $title = info_str();
	
	return
		$cgi->header . qq`<html>
<head>
<title>$title</title>
<style type="text/css">
body {
	margin: 0;
	font-size: 100%;
}

a {
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

#topbar {
	position: relative;
	background-color: 228b22;
	height: 50px;
	color: fff;
	text-align: center;
}

#logo {
	position: absolute;
}

#beta {
	position: absolute;
	font-size: 2em;
	left: 197px;
}

#taskbar {
	position: absolute;
	top: 18px;
	height: 32px;
	left: 280px;
	
	background-color: 2aab2a;
	text-align: left;
	line-height: 2em;
	font-weight: bold;
	font-family: sans-serif;
	font-size: 1em;
}

#taskbar a {
	color: fff;
	text-decoration: none;
	display: inline-block;
	margin: 0 0 0 0;
	padding: 0 10px 0 10px;
	height: 2em;
}

#taskbar a:hover {
	background-color: 33d133;
	
}

</style>
</head>
<body>
<div id="topbar">
	<div id="logo"><a href="index.pl"><img src="mindmeld.png" height="50px" border="0"/></a></div>
	<div id="beta">&beta;</div>
	<div id="taskbar">
		` .
	join(" | ", map {$cgi->a({-href => $_->[0]}, $_->[1])} @actions) . q`
	</div>
</div>
<div style="margin:5px 5px 5px 5px">`;
}

sub footer {
	my $cgi = cgi();
	return
		$cgi->hr .
		$cgi->p(info_str()) .
		$cgi->p('Copyright &copy; 2011 Miorel-Lucian Palii') .
		q`</div>` .
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
