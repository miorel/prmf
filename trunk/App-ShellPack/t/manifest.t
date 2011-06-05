#!perl -T

use warnings;
use strict;

use Test::More;

use App::ShellPack;

my $v;

$v = 0.9;
eval "use Test::CheckManifest $v";
plan skip_all => "Test::CheckManifest $v required" if $@;

ok_manifest({
	exclude => [
		'/Makefile.old',
		"/App-ShellPack-$App::ShellPack::VERSION.tar.gz",
	],
	filter => [
		qr(/\.svn/),
	],
});

