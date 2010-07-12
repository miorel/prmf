#!/usr/bin/perl
#
# Script to grab periodic table info from gElemental
#
# There's probably a nicer way to do it, but this works.
# 

use warnings;
use strict;

### BEGIN CONFIG ###############################################################
my $cmd_format = 'gelemental -p %s 2>&1';
my @fields = qw(symbol name atomic_number atomic_radius covalent_radius);
my $field_format = "%s=%s".(",%s"x($#fields-1))."\n";
my $out_file = 'res/com/googlecode/prmf/merapi/chem/PeriodicTableInfo.properties';
### END CONFIG #################################################################

open my $fh, ">$out_file" or die "Failed to open $out_file for writing";

eval 'use File::Tee qw(tee)';
if($@) {
	print STDERR "Get the File::Tee module to see script output on screen\n";
	open STDOUT, ">&", $fh;
}
else {
	tee(*STDOUT, '>', $out_file);
}

print "#\n# Output generated ",`date`;
print "# Execute $0 to regenerate\n";
print "#\n# Field format:\n";
printf("# $field_format#\n\n", map {
	(my $s = $_) =~ s/_/ /g;
	$s =~ s/\b(.)/\U$1/g;
$s} @fields);

for(my $atomic_number = 1;; ++$atomic_number) {
	my $cmd = sprintf($cmd_format, $atomic_number);
	my @info = `$cmd`;
	last if $info[0] =~ /^unknown element/i;
	local %_ = ();
	for(@info) {
		if(/\s*([^:]+?):\s+(.+)/) {
			my $key = lc $1;
			my $val = $2;
			$key =~ s/\s+/_/g;
			$_{$key} = $val;
		}
	}
	$_{$_} ||= '' for @fields;
	$_{$_} = $_{$_} =~ /(\d+) pm/ ? $1 : 'NaN' for qw(atomic_radius covalent_radius);
	printf($field_format, map {$_{$_}} @fields);
}

close $fh;
