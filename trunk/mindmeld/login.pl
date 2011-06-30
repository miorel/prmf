#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use Mindmeld;

use Digest::SHA qw(sha256_hex);
use DBI;

my $cgi = Mindmeld::cgi();

print Mindmeld::header();

my $dbh = DBI->connect("dbi:SQLite:dbname=auth.db", "", "");
$dbh->do("CREATE TABLE IF NOT EXISTS users (username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, salt TEXT NOT NULL)");

my $username;
my $login_attempt = 0;
my $login_success = 0;
if(lc($cgi->request_method) eq 'post') {
	$login_attempt = 1;
	$username = $cgi->param('username');
	my $password = $cgi->param('password');
	if(defined($username) && defined($password)) {
		my $sth = $dbh->prepare("SELECT salt FROM users WHERE username = ?");
		$sth->execute($username);
		my($salt) = $sth->fetchrow_array;
		if(defined($salt)) {
			$password = sha256_hex($password . $salt);
			$sth = $dbh->prepare("SELECT username FROM users WHERE username = ? AND password = ?");
			$sth->execute($username, $password);
			$sth->fetch and $login_success = 1;
		}
	}
}

if($login_success) {
	print $cgi->p("Successfully logged in as $username!");
}
else {
	print $cgi->start_form(-action => 'login.pl');
	print $cgi->p("Wrong username or password :(") if $login_attempt;
	print $cgi->p("Username: " . $cgi->textfield(-name => 'username', -value => '', -size => 16, -maxlength => 16));
	print $cgi->p("Password: " . $cgi->password_field(-name => 'password', -value => '', -size => 16, -maxlength => 16, -override => 1));
	print $cgi->submit('Login');
	print $cgi->end_form;
}

print Mindmeld::footer();

