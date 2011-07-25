#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
my $dbh = MindMeld->dbh;

print MindMeld->header;

my $user = MindMeld->user;

if(defined $user) {
	print $cgi->start_form(-action => 'options.pl');
	if(lc($cgi->request_method) eq 'post') {
		my $action = $cgi->param('action');
		if(defined($action) && $action eq 'update_categories_active') {
			my $sth = $dbh->prepare_cached('SELECT id FROM categories');
			my $cid;
			$sth->execute;
			$sth->bind_columns(\$cid);
			while($sth->fetch) {
				my $cn = "cat_${cid}_active";
				my $val = $cgi->param($cn);
				$dbh->prepare_cached('INSERT INTO user_category_rels (user, category, active) VALUES (?, ?, ?)')->execute($user, $cid, (defined($val) && $val eq '1' ? 1 : 0));
			}
			print $cgi->p('Active categories successfully updated!');
		}
	}
	my $sth = $dbh->prepare('SELECT id, name, (SELECT active FROM user_category_rels WHERE category = categories.id AND user = ?) FROM categories ORDER BY name');
	my($id, $name, $active);
	$sth->execute($user);
	$sth->bind_columns(\$id, \$name, \$active);
	while($sth->fetch) {
		my $cn = "cat_${id}_active";
		print $cgi->p($cgi->checkbox(-name => $cn, -checked => ($active eq '1'), -label => $name, -value => 1, -override => 1));
	}
	print $cgi->hidden(-name => 'action', -default => 'update_categories_active', -override => 1);
	print $cgi->submit('Update');
	print $cgi->end_form;
}
else {
	print $cgi->p("This page requires you to be logged in. <a href=\"index.pl\">Register</a> an account.");
}

print MindMeld->footer;

