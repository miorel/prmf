my $cgi = MindMeld->cgi;

my $session = MindMeld->session;
$session->destroy if defined $session;

print $cgi->redirect(-uri => 'index.pl', -status => 302);

