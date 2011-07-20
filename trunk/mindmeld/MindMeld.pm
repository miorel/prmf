package MindMeld;

use warnings;
use strict;

use CGI;
use DBI;

use PRMF::Auth;

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

my $info_str = 'MindMeld beta 201107200721';
my($cgi, $dbh);

sub cgi {
	$cgi = CGI->new
		unless defined $cgi;
	return $cgi;
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
	my $package = shift;
	
	my $cgi = $package->cgi;
	
	my @actions = (
		['study.pl' => 'Study'],
		['options.pl' => 'Options'],
		['stats.pl' => 'Statistics'],
	);
	my $title = $package->info_str;
	
	my $username;
	my $login_attempt = 0;
	my $login_success = 0;
	if(lc($cgi->request_method) eq 'post') {
		$username = $cgi->param('username');
		my $password = $cgi->param('password');
		my $auth = PRMF::Auth->new(db => 'mm.db');
		$login_attempt = 1;
		$login_success = $auth->login($username, $password);
	}
	
	my $login_div;
	if($login_success) {
		$login_div = "Welcome, <strong>$username</strong>!";
	}
	else {
		$login_div =
			$cgi->start_form(-action => $cgi->url(-query => 1)) .
			'Username: ' .
			$cgi->textfield(-name => 'username', -value => '', -size => 12, -maxlength => 16) .
			' Password: ' .
			$cgi->password_field(-name => 'password', -value => '', -size => 12, -maxlength => 16, -override => 1) .
			' ' .
			$cgi->submit('Log in') .
			$cgi->end_form;
		$login_div .= '<p>Wrong username or password!</p>' if $login_attempt;
	}
	
	return
		qq`Content-Type: text/html; charset=utf-8

<!DOCTYPE html>
<html lang="en-US">
<head>
<meta charset="utf-8" />
<link rel="stylesheet" type="text/css" href="css/green.css" />
<meta name="description" content="$title" />
<title>$title</title>
</head>
<body>
<div id="topbar">
	<div id="logo"><a href="index.pl"><img src="mindmeld.png" height="50" alt="MindMeld"/></a></div>
	<div id="beta">&beta;</div>
	<div id="taskbar">` .
	join(" | ", map {$cgi->a({-href => $_->[0]}, $_->[1])} @actions) . qq`
	</div><div id="login">$login_div</div>` . 

q`
</div>
<div style="margin:5px 5px 5px 5px">`;
}

sub footer {
	my $package = shift;
	my $cgi = $package->cgi;
	return
		$cgi->hr .
		$cgi->p($package->info_str) .
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
