#!perl -T

use warnings;
use strict;

use Test::More tests => 1;

BEGIN {
	use_ok('App::ShellPack') || print "Bailing out!\n";
}

diag("Testing App::ShellPack $App::ShellPack::VERSION, Perl $], $^X");
