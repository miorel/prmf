#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;
use PRMF::Auth;

my $cgi = MindMeld->cgi;

print MindMeld->header;

my $message;
my $success = 0;

if(lc($cgi->request_method) eq 'post') {
	my $username = $cgi->param('username');
	my $password = $cgi->param('password');
	my $auth = PRMF::Auth->new(db => 'mm.db');
	if($auth->add_user($username, $password)) {
		$success = 1;
	}
	else {
		$message = $auth->err;
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

print MindMeld->footer;

