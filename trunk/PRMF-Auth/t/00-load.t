#!perl -T

use Test::More tests => 1;

BEGIN {
    use_ok( 'PRMF::Auth' ) || print "Bail out!
";
}

diag( "Testing PRMF::Auth $PRMF::Auth::VERSION, Perl $], $^X" );
