package MindMeld;

use warnings;
use strict;

use CGI;
use DBI;

use MindMeld::Category;
use MindMeld::Question;
use MindMeld::Session;
use MindMeld::User;
use MindMeld::User_category_rel;
use MindMeld::User_question_rel;

use PRMF::Auth;

my $info_str = 'MindMeld beta 201107221337';
my $dbfile = 'mm.db';
my($cgi, $dbh, $cookie, $user, $session, $login_attempt);

sub cgi {
	$cgi = CGI->new
		unless defined $cgi;
	return $cgi;
}

sub dbh {
	unless(defined $dbh) {
		my $exists = -f $dbfile; # there's technically a race condition here, but amusingly it has no negative consequences, so I'm letting it be
		$dbh = DBI->connect("dbi:SQLite:dbname=$dbfile", '', '');
		if(!$exists) {
			MindMeld::Category->_ensure_schema;
			MindMeld::Question->_ensure_schema;
			MindMeld::Session->_ensure_schema;
			MindMeld::User->_ensure_schema;
			MindMeld::User_category_rel->_ensure_schema;
			MindMeld::User_question_rel->_ensure_schema;
		}
		$dbh->do('PRAGMA foreign_keys = ON');
	}
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

sub user {
	my $package = shift;
	$package->_handle_login;
	$user = $session->user
		if defined($session) && !defined($user);
	return $user;
}

sub session {
	my $package = shift;
	$package->_handle_login;
	return $session;
}

sub login_attempted {
	my $package = shift;
	$package->_handle_login;
	return $login_attempt;
}

sub cookie {
	my $package = shift;
	$package->_handle_login;
	$cookie = $package->cgi->cookie(-name => 'mindmeld', -value => $session->text, -expires => '+15m')
		if defined($session) && !defined($cookie);
	return $cookie;
}

sub _handle_login {
	return if defined $login_attempt;
	my $package = shift;
	my $cgi = $package->cgi;
	$login_attempt = 0;
	my $action = $cgi->param('action');
	if(lc($cgi->request_method) eq 'post' && defined($action) && $action eq 'login') {
		$login_attempt = 1;
		my $auth = PRMF::Auth->new(db => 'mm.db');
		my $username = $cgi->param('username');
		if($auth->login($username, $cgi->param('password'))) {
			$user = MindMeld::User->retrieve(username => $username);
			if(defined($user)) {
				# insecure!
				my $text = join('', map {my @arr = ('A'..'Z', 'a'..'z', 0..9, '.', '/'); $arr[int(rand(scalar(@arr)))]} 1..32);
				$session = MindMeld::Session->create(user => $user, text => $text, expires => (time + 15 * 60));
				undef $user if !defined($session);
			}
		}
	}
	else {
		my $text = $cgi->cookie('mindmeld');
		$session = MindMeld::Session->retrieve(text => $text) if defined($text);
		$session->destroy if defined($session) && ($session->expires < time);
	}
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
	
	my $user = $package->user;
	
	my $login_div;
	if($user) {
		$login_div = "Welcome, <strong>" . ($user->username) . "</strong>! " .
			$cgi->a({-href => 'logout.pl'}, "Log out");
	}
	else {
		$login_div =
			$cgi->start_form(-action => $cgi->url(-full => 1, -query => 1)) .
			$cgi->hidden(-name => 'action', -default => 'login', -override => 1) .
			'Username: ' .
			$cgi->textfield(-name => 'username', -value => '', -size => 12, -maxlength => 32) .
			' Password: ' .
			$cgi->password_field(-name => 'password', -value => '', -size => 12, -maxlength => 32, -override => 1) .
			' ' .
			$cgi->submit('Log in') .
			$cgi->end_form;
		$login_div .= '<p>Wrong username or password!</p>' if $package->login_attempted;
	}

	my @header_args = (-Content_Type => 'text/html; charset=utf-8');
	my $cookie = $package->cookie;
	push @header_args, -cookie => $cookie if defined $cookie;
	
	return
		$cgi->header(@header_args) .
		qq`<!DOCTYPE html>
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
</div><div class="rc_outbox"><div class="rc_top"><div></div></div><div class="rc_content">`;
}

sub footer {
	my $package = shift;
	my $cgi = $package->cgi;
	return
		q`</div><div class="rc_bottom"><div></div></div></div>

<div id="footer">` .
		$cgi->p($package->info_str) .
		$cgi->p('Copyright &copy; 2011 Miorel-Lucian Palii') . '</div>' .
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
