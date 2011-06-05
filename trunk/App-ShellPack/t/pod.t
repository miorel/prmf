#!perl -T

use warnings;
use strict;

use Test::More;

my $v;

$v = 1.22;
eval "use Test::Pod $v";
plan skip_all => "Test::Pod $v required for testing POD" if $@;

all_pod_files_ok();
