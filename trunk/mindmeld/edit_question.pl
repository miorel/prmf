use MindMeld::Question;

my $cgi = MindMeld->cgi;

my $qid = MindMeld->param('qid');
unless(defined $qid) {
	print $cgi->redirect(-uri => 'study.pl', -status => 302);
}
else {
	print MindMeld->header;

	my $user = MindMeld->user;
	if(defined $user) {
		my $q = MindMeld::Question->retrieve(id => $qid);
		if(defined($q) && $q->author->{_id} eq $user->{_id}) {
		
			if(lc($cgi->request_method) eq 'post') {
				my $action = $cgi->param('action');
				if($action eq 'edit_question') {
					my $question = $cgi->param('question');
					my $answer = $cgi->param('answer');
					my $sth = MindMeld->dbh->prepare_cached("UPDATE questions SET question = ?, answer = ? WHERE id = ?");
					if($sth->execute($question, $answer, $qid)) {
						print $cgi->p("Question updated successfully!");
					}
				}
			}
		
			print $cgi->start_form(-action => 'edit_question.pl');
			print $cgi->hidden(-name => 'action', -default => 'edit_question', -override => 1);
			print $cgi->hidden(-name => 'qid', -default => $qid, -override => 1);
			print $cgi->p("Category: " . $cgi->a({-href => "show_category.pl?cid=" . $q->category->{_id}}, $q->category->name));
			print $cgi->p("Question: <br />" . $cgi->textarea(-name => 'question', -value => $q->question, -rows => 10, -columns => 50, -override => 1));
			print $cgi->p("Answer: <br />" . $cgi->textarea(-name => 'answer', -value => $q->answer, -rows => 10, -columns => 50, -override => 1));
			print $cgi->submit('Update');
			print $cgi->end_form;
		}
		else {
			print $cgi->p("Question doesn't exist or you don't have permission to edit it :(");
		}
	}
	else {
		print $cgi->p("This page requires you to be logged in. <a href=\"index.pl\">Register</a> an account.");
	}
	
	print MindMeld->footer;
}

