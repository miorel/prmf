#!perl -T

use warnings;
use strict;

use Test::More;

my $v;

$v = 1.08;
eval "use Test::Pod::Coverage $v";
plan skip_all => "Test::Pod::Coverage $v required for testing POD coverage" if $@;

$v = 0.18;
eval "use Pod::Coverage $v";
plan skip_all => "Pod::Coverage $v required for testing POD coverage" if $@;

all_pod_coverage_ok();
