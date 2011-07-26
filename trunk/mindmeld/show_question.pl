use MindMeld::Question;

my $cgi = MindMeld->cgi;

my $qid = MindMeld->param('qid');
unless(defined $qid) {
	print $cgi->redirect(-uri => 'study.pl', -status => 302);
}
else {
	print MindMeld->header;

	my $q = MindMeld::Question->retrieve(id => $qid);
	if(defined $q) {
		print $cgi->p("Category: " . $cgi->a({-href => "show_category.pl?cid=" . $q->category->{_id}}, $q->category->name));
		print $cgi->p("Question: " . $q->question);
		print $cgi->p("Answer: " . $q->answer);
		print $cgi->p("Author: " . $q->author->username);
		
		my $user = MindMeld->user;
		if(defined($user)) {
			if($q->author->{_id} eq $user->{_id}) {
				print $cgi->p( qq^<a href="edit_question.pl?qid=$qid">Edit Question</a>^ );
			}
		}
	}
	else {
		print $cgi->p("Question doesn't exist :(");
	}
	
	print MindMeld->footer;
}

