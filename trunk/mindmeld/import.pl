use MindMeld::Question;

use XML::Simple;

my $cgi = MindMeld->cgi;

print MindMeld->header;

my $user = MindMeld->user;
if(defined $user) {
	if(lc($cgi->request_method) eq 'post') {
		my $action = $cgi->param('action');
		if(defined($action) && $action eq 'import') {
			my $fh = $cgi->upload('file');
			if(defined($fh)) {
				my $xml = XMLin($fh, KeyAttr => undef);
				if(defined($xml) && ref($xml) eq 'HASH') {
					my @data = ();
					my $message = undef;
					my $items = $xml->{item} || [];
					
					$items = [$items] if ref($items) eq 'HASH';
					undef $items if ref($items) ne 'ARRAY';

					for(@$items) {
						if(ref ne 'HASH') {
							$message = "Unexpected format.";
							last;
						}
						my $cat = $_->{cat};
						my $q = $_->{Q};
						my $a = $_->{A};
						push @data, {
							'cat' => $cat,
							'Q' => $q,
							'A' => $a,
						};
					}
					
					my $count = 0;
					unless(defined $message) {
						for(@data) {
							my $cat_name = $_->{cat};
							my $question = $_->{Q};
							my $answer = $_->{A};
					
							MindMeld::Category->create(name => $cat_name, author => $user);
							my $cat = MindMeld::Category->retrieve(name => $cat_name, author => $user);
							MindMeld::Question->create(
								category => $cat,
								question => $question,
								answer => $answer,
								author => $user,
							) && ++$count;
						}
						print $cgi->p("Successfully uploaded $count questions! Upload more?");
					}
					else {
						print $cgi->p($message);
					}
				}
				else {
					print $cgi->p("Problem parsing uploaded file as XML.");
				}
			}
		}
	}
	
	print $cgi->start_multipart_form(-action => 'import.pl');
	print $cgi->hidden(-name => 'action', -default => 'import', -override => 1);
	print $cgi->p("Select a Mnemosyne XML file: " . $cgi->filefield(-name => 'file', -default => '', -size => 50, -override => 1));
	print $cgi->submit('Upload');
	print $cgi->end_form;
}
else {
	print $cgi->p("This page requires you to be logged in. <a href=\"index.pl\">Register</a> an account.");
}
	
print MindMeld->footer;
