#!perl -T

use Test::More tests => 1;

BEGIN {
    use_ok( 'App::ShellPack' ) || print "Bail out!
";
}

diag( "Testing App::ShellPack $App::ShellPack::VERSION, Perl $], $^X" );
