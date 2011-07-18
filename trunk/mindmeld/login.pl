#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;
use PRMF::Auth;

my $cgi = MindMeld::cgi();

print MindMeld::header();

my $username;
my $login_attempt = 0;
my $login_success = 0;
if(lc($cgi->request_method) eq 'post') {
	$username = $cgi->param('username');
	my $password = $cgi->param('password');
	my $auth = PRMF::Auth->new(db => 'auth.db');
	$login_attempt = 1;
	$login_success = $auth->login($username, $password);
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

print MindMeld::footer();

