#!/usr/bin/perl

use warnings;
use strict;

chdir "$ENV{HOME}/.excise";

my $cmd = shift;

if(defined($cmd) && $cmd =~ /^(install|remove)$/) {
	my $pkg = shift;
	die "Must specify a package!" unless $pkg;
	my $fh;
	open $fh, "<var/excise/package-info.txt" or die "Failed to read package information.";
	my %vars = ();
	my @pkg = ();
	my @fh = <$fh>;
	while(@fh) {
		$_ = shift @fh;
		chomp;
		s/^\s+//;
		next if /^#/ || !/\S/;
		my($var, $val) = split(/\s+/, $_, 2);
		my $hash = ($var eq 'package') ? $pkg[scalar @pkg] = {} : (@pkg ? $pkg[-1] : \%vars);
		unless(defined($val) && length($val) > 0) {
			$val = '';
			while(@fh) {
				$_ = shift @fh;
				unless(s/^\t//) {
					unshift @fh, $_;
					last;
				}
				chomp;
				$val .= "$_\n";
			}
		}
		$hash->{$var} = $val;
	}
	close $fh;
	my $pkg_obj;
	for(@pkg) {
		if($_->{package} eq $pkg) {
			$pkg_obj = $_;
			last;
		}
	}
	die "There is no package \"$pkg\"." unless $pkg_obj;
	my %installed = ();
	if(-e "var/excise/installed.txt") {
		open $fh, "<var/excise/installed.txt" or die "Failed to read list of installed packages.";
		while(<$fh>) {
			chomp;
			s/^\s+//;
			s/\s*(?:#.*)?$//;
			$installed{$_} = 1;
		}
		close $fh;
	}
	if($cmd eq 'install') {
		die "Package $pkg is already installed!" if $installed{$pkg}; 
	}
	if($cmd eq 'remove') {
		die "Package $pkg is not installed, can't be removed." unless $installed{$pkg};
	}
	open $fh, ">script.sh" or die "Failed to open script for writing.";
	print $fh << 'EOF';
#!/bin/bash
tld="$PWD"
umask 0077
EOF
	print $fh $pkg_obj->{$cmd};
	close $fh;
	system("bash", "script.sh") == 0 or die "\u$cmd failed!";
	unlink("script.sh");
	print "\u$cmd of package $pkg successful!\n";
	if($cmd eq 'install') {
		$installed{$pkg} = 1;
	}
	elsif($cmd eq 'remove') {
		delete $installed{$pkg};
	}
	open $fh, ">var/excise/installed.txt" or die "Failed to update list of installed packages.";
	print $fh "$_\n" for sort keys %installed;
	close $fh;
	exit 0;
}

print STDERR <<'EOF';
excise 0.0.1-pre (July 15, 2011) 
Usage: excise install|remove PACKAGE

excise is a command-line interface for managing packages in one's home
directory on systems like CISE.
EOF

exit 1;
