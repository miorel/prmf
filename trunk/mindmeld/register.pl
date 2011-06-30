#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use Mindmeld;

use Digest::SHA qw(sha256_hex);
use DBI;

srand(time ^ $$ ^ unpack "%L*", `ps axww | gzip -f`);

my $cgi = Mindmeld::cgi();

print Mindmeld::header();

my $dbh = DBI->connect("dbi:SQLite:dbname=auth.db", "", "");
$dbh->do("CREATE TABLE IF NOT EXISTS users (username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, salt TEXT NOT NULL)");

my $message;
my $success = 0;

if(lc($cgi->request_method) eq 'post') {
	my $username = $cgi->param('username');
	my $password = $cgi->param('password');
	if(defined($username) && defined($password)) {
		if(length($username) < 3 || length($username) > 16) {
			$message = "Username must be between 3 and 16 characters in length.";
		}
		elsif($username =~ /[^A-Za-z0-9_]/) {
			$message = "Username may only contain A-Z, a-z, 0-9, or underscore.";
		}
		elsif($username !~ /^[A-Za-z]/) {
			$message = "Username must start with a letter.";
		}
		elsif(length($password) < 6) {
			$message = "Password must be at least 6 characters.";
		}
		elsif(length($password) > 32) {
			$message = "Password may not be longer than 32 characters.";
		}
		else {
			my $salt = join('', map {my @arr = ('A'..'Z', 'a'..'z', 0..9, '.', '/'); $arr[int(rand(scalar(@arr)))]} 1..16);
			my $sth = $dbh->prepare("INSERT INTO users (username, password, salt) VALUES (?, ?, ?)");
			$password = sha256_hex($password . $salt);
			if($sth->execute($username, $password, $salt)) {
				$success = 1;
			}
			elsif($sth->err == 19) { # constraint failed
				$message = "Username unavailable :(";
			}
			else {
				$message = "A previously unseen internal error has occurred! Thanks for discovering this for us; we'll try to have it fixed as soon as possible!";
			}
		}
	}
}

if($success) {
	print $cgi->p("Successfully registered!");
}
else {
	print $cgi->start_form(-action => 'register.pl');
	print $cgi->p($message) if defined $message;
	print $cgi->p("Username: " . $cgi->textfield(-name => 'username', -value => '', -size => 16, -maxlength => 16));
	print $cgi->p("Password: " . $cgi->password_field(-name => 'password', -value => '', -size => 16, -maxlength => 32, -override => 1));
	print $cgi->submit('Register');
	print $cgi->end_form;
}

print Mindmeld::footer();

