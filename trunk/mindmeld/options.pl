#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

my $cgi = MindMeld->cgi;
my $dbh = MindMeld->dbh;

print MindMeld->header;

print $cgi->start_form(-action => 'options.pl');
if(lc($cgi->request_method) eq 'post') {
	my $action = $cgi->param('action');
	if(defined($action) && $action eq 'update_categories_active') {
		my $sth = $dbh->prepare('SELECT id FROM categories');
		my $id;
		$sth->execute;
		$sth->bind_columns(\$id);
		while($sth->fetch) {
			my $cn = "cat_${id}_active";
			my $val = $cgi->param($cn);
			$dbh->prepare('UPDATE categories SET active = ? WHERE id = ?')->execute((defined($val) && $val eq '1'), $id);
		}
		print $cgi->p('Active categories successfully updated!');
	}
}
my $sth = $dbh->prepare('SELECT id, name, active FROM categories ORDER BY name');
my($id, $name, $active);
$sth->execute;
$sth->bind_columns(\$id, \$name, \$active);
while($sth->fetch) {
	my $cn = "cat_${id}_active";
	print $cgi->p($cgi->checkbox(-name => $cn, -checked => $active, -label => $name, -value => 1, -override => 1));
}
print $cgi->hidden(-name => 'action', -default => 'update_categories_active', -override => 1);
print $cgi->submit('Update');
print $cgi->end_form;

print MindMeld->footer;

