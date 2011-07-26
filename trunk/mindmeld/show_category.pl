use MindMeld::Category;
use MindMeld::Question;

my $cgi = MindMeld->cgi;

my $cid = MindMeld->param('cid');
unless(defined $cid) {
	print $cgi->redirect(-uri => 'study.pl', -status => 302);
}
else {
	my $dbh = MindMeld->dbh;
	my $user = MindMeld->user;

	print MindMeld->header;
	
	my $c = MindMeld::Category->retrieve(id => $cid);
	if(defined $c) {
		my $sth = $dbh->prepare('SELECT id FROM questions WHERE category = ?');
		$sth->execute($cid);
	
		my $qid;
		$sth->bind_columns(\$qid);
		my @q;
		push @q, $qid while $sth->fetch;

		my $count = 0;
		print "<table>";
		for $qid (@q) {
			my $q = MindMeld::Question->retrieve(id => $qid);
			if(defined $q) {
				printf("<tr>" . join('', map {"<td>$_</td>"} (
					++$count. ".",
					$cgi->escapeHTML($q->question),
					$cgi->escapeHTML($q->answer),
					qq^<a href="show_question.pl?qid=$qid">View</a>^,
					((defined($user) && $user->{_id} eq $q->author->{_id}) ? qq^<a href="edit_question.pl?qid=$qid">Edit</a>^ : ''),
				)));
			}
		}
		print "</table>";
	}
	else {
		print $cgi->p("Category doesn't exist :(");
	}
	
	print MindMeld->footer;
}

