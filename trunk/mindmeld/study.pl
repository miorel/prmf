my $cgi = MindMeld->cgi;
my $dbh = MindMeld->dbh;

print MindMeld->header;

my $user = MindMeld->user;

my $qid = MindMeld->param('qid');
unless(defined($qid)) {
	my $sql;
	my @args = ();
	if(defined($user)) {
		my $max_new = 5;
	
		my $cat_is_active = '(SELECT active FROM user_category_rels WHERE user = ? AND category = %s) = 1';
		my $cat_is_active_from_uqr = sprintf($cat_is_active, '(SELECT category FROM questions WHERE id = user_question_rels.question)');
		my $cat_is_active_from_q = sprintf($cat_is_active, 'questions.category');

		my $reviewed = "SELECT question AS id, 1, RANDOM() FROM user_question_rels WHERE user = ? AND grade < 2 AND $cat_is_active_from_uqr";
		my $random_new = "SELECT id, 2, RANDOM() FROM questions WHERE $cat_is_active_from_q ORDER BY 2, 3 LIMIT $max_new";
		$sql = "SELECT id FROM ($reviewed UNION $random_new) ORDER BY RANDOM() LIMIT 1";
		push @args, $user, $user, $user;
	}
	else {
		$sql = "SELECT id FROM questions ORDER BY RANDOM() LIMIT 1";
	}
	$qid = MindMeld->_select($sql, @args);
}

if(defined $qid) {
	my $q = MindMeld::Question->retrieve(id => $qid);
	if(defined($q)) {
		my $question = $q->question;
		my $answer = $q->answer;
		my $category = $q->category->name;
		my $cid = $q->category;
		print $cgi->p("Category: " . $cgi->a({-href => "show_category.pl?cid=$cid"}, $category));
		print $cgi->div({-style => 'text-align:center;'}, 
			qq^<p id="q">$question</p>^ .
			$cgi->p({-id => 'a', -style => 'visibility:hidden;'}, $answer) .
			$cgi->p({-id => 'sa'}, $cgi->a({-href => 'javascript:void(0);', -onclick => q{javascript:document.getElementById('sa').style.display='none';document.getElementById('a').style.visibility='visible';document.getElementById('gr').style.display='block';;document.getElementById('help').style.display='block';}}, 'Show Answer')) .
			$cgi->p({-id => 'gr', -style => 'display:none;'}, qq^<a href="set_grade.pl?qid=$qid&amp;gc="><img src="pixel.png" width="400" height="50" ismap alt="target" /></a>^) .
			'<p id="help" style="display:none;">Click the box to indicate how well you knew the answer. Left is no knowledge, right is perfect knowledge.</p>'
		);
	}
	else {
		print $cgi->p({-style => 'text-align:center;'}, "No such question :/");
	}
}
else {
	print $cgi->p({-style => 'text-align:center;'}, sprintf("There are no active categories. Perhaps you'd like to enable some in the %s?", $cgi->a({-href => 'options.pl'}, 'options')));
}

print MindMeld->footer;

