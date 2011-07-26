use PRMF::Auth;

print MindMeld->header;

print q`<div><div style="float:left;width:560px;padding-right:50px;border-right:1px dotted black;"><p><strong>MindMeld</strong> will be a system for training your mind like a Vulcan. <span style="color:#bbb;">Not becoming an emotionless automaton is left as an exercise for the user.</span></p><iframe width="560" height="349" src="https://www.youtube.com/embed/ds7dBoWLlrc?rel=0" frameborder="0" allowfullscreen></iframe><p style="font-size:80%;">Scene from <em><a href="http://www.amazon.com/s/?url=search-alias%3Dmovies-tv&amp;field-keywords=Star+Trek+IV+The+Voyage+Home" target="_blank">Star Trek IV: The Voyage Home</a></em>, distributed by <a href="http://www.paramount.com/" target="_blank">Paramount Pictures</a>.</p></div><div style="float:left;width:300px;padding-left:50px;">`;

my $message;
my $success = 0;

my $cgi = MindMeld->cgi;
if(lc($cgi->request_method) eq 'post') {
	my $action = $cgi->param('action');
	if($action eq 'register') {
		my $agree = $cgi->param('agree');
		if(defined($agree) && $agree eq '1') {
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
		else {
			$message = q`You're forgetting something.`;
		}
	}
}

if($success) {
	print $cgi->p('Successfully registered!');
	print $cgi->p(q`<span style="color:red;">Important!</span> Please note that the database currently gets reset on every update (so you'll probably have to re-register when I push a change).`);
}
else {
	print $cgi->start_form(-action => 'index.pl');
	print $cgi->hidden(-name => 'action', -default => 'register', -override => 1);
	print $cgi->p($message) if defined $message;
	print $cgi->p("Username: " . $cgi->textfield(-name => 'username', -value => '', -size => 16, -maxlength => 32));
	print $cgi->p("Password: " . $cgi->password_field(-name => 'password', -value => '', -size => 16, -maxlength => 32, -override => 1));
	print $cgi->p(q`<label><input type="checkbox" name="agree" value="1" />I understand this is experimental software, and I promise not to get too mad <span style="text-decoration:line-through;">if</span> when something breaks.</label>`);
	print $cgi->submit('Sign up');
	print $cgi->end_form;
}

print q`</div></div>`;

print MindMeld->footer;

