#!/usr/bin/perl

use warnings;
use strict;

use Archive::Extract;
use Cwd qw(abs_path);
use File::Copy;
use File::Temp qw(tempdir);

my $make_cmd = "make";
my $distfile = "algo.zip";
my $clean_target = "clean";

make_clean();
make_dist();

my $wd = abs_path();

my $tmpdir = File::Temp->newdir(undef, CLEANUP => 1);
unless(defined($tmpdir) && -d $tmpdir) {
	print STDERR "Failed to create temporary directory.\n";
	exit 1;
}

unless(copy($distfile, $tmpdir)) {
	print STDERR "Failed to copy source archive to temporary directory:\n$!\n";
	exit 1;
}

chdir $tmpdir;

print "Extracting source archive in temporary directory $tmpdir ...\n";
my $ae = Archive::Extract->new(archive => $distfile);
if(!defined($ae) || $ae->error) {
	print STDERR "Problem building archive object, bailing.\n";
	exit 1;
}
unless($ae->extract) {
	print STDERR "Fatal error while extracting archive.\n";
	exit 1;
}
print "\n";

make_clean();
print "Compiling the packet...\n";
make("Non-zero exit code while compiling the packet!");

make_clean();
make_dist();

chdir $wd;
make_clean();

print "Everything seems to work! \\o/\n";
exit 0;

sub make_dist {
	print "Preparing a source archive...\n";
	make($distfile, "Non-zero exit code while preparing source archive!");
}

sub make_clean {
	print "Cleaning...\n";
	make($clean_target, "Non-zero exit code while cleaning build.");
}

sub make {
	my $err_msg = pop;
	unless(system($make_cmd, @_) == 0) {
		print STDERR "$err_msg\n";
		exit 1;
	}
	print "\n";
}
