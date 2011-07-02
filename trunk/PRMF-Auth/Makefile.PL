use 5.006000;

use warnings;
use strict;

use Config;
use ExtUtils::MakeMaker;

my @warnings = ();

my %args = (
	NAME          => 'PRMF::Auth',
	AUTHOR        => 'Miorel-Lucian Palii <mlpalii@gmail.com>',
	VERSION_FROM  => 'lib/PRMF/Auth.pm',
	ABSTRACT_FROM => 'lib/PRMF/Auth.pm',
	PREREQ_PM     => {
		(map {$_ => 0} qw(
			DBI
		)),
	},
	clean         => { FILES => 'PRMF-Auth-*' },
);

my $eummv = $ExtUtils::MakeMaker::VERSION;
$eummv =~ y/_//d;
my $eummv_min = 6.3002;
if($eummv >= $eummv_min) {
	$args{LICENSE} = 'perl';
}
else {
	push @warnings, "Your ExtUtils::MakeMaker version is $eummv; you could probably benefit from an upgrade to at least version $eummv_min.";
}

my $gzip = $Config::Config{gzip};
if(defined($gzip)) {
	$args{dist} = {
		COMPRESS => "\'$gzip\' --best --force",
		SUFFIX => 'gz',
	};
}
else {
	push @warnings, "Perl doesn't seem to be aware of a gzip command on your system.";
}

WriteMakefile(%args);

if(@warnings) {
	print(STDERR "Things didn't go entirely according to plan... Here are the relevant issues:\n");
	print(STDERR " - $_\n") for @warnings;
}

