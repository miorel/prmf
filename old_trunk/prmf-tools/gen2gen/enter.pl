#!/usr/bin/perl

use warnings;
use strict;

use IPC::Open3;
use Symbol qw(gensym);

my $loc = shift;
die "no location specified" unless defined $loc;
die "$loc is not a directory" unless -d $loc;

my $user = shift;

run("mount", "-t", "proc", "none", "$loc/proc");
run("mount", "-o", "bind", "/dev", "$loc/dev");
run("mount", "-t", "sysfs", "none", "$loc/sys");

run("cp", "-L", "/etc/resolv.conf", "$loc/etc/resolv.conf");

my $pid = fork();
die "forking failed" unless defined $pid;

if($pid == 0) {
	# I am child

	my($out, $err);
	open $out, "> out.txt";
	open $err, "> err.txt";

	chdir $loc;
	chroot '.';

	$user = "root" unless defined $user;

	my($uid, $gid, $home, $shell) = (getpwnam($user))[2,3,7,8];
	if($shell ne '/bin/bash') {
		$shell = '/bin/bash';
	}

	local @_;
	while(@_ = getgrent()) {
		next if $_[2] == 0 && $user ne "root";
		for(split(/\s+/, $_[3])) {
			$gid .= " $_[2]" and last if $_ eq $user;
		}
	}

	$( = $) = $gid;
	$< = $> = $uid;

	$ENV{USER} = $user;
	$ENV{HOME} = $home;
	$ENV{SHELL} = $shell;

	%ENV = map { $_ => $ENV{$_} } (grep {defined $ENV{$_}} qw(
		USER
		HOME
		SHELL
		TERM
	));
	
	chdir $ENV{HOME};

	my($p_in, $p_out, $p_err);
	use Symbol 'gensym'; $p_err = gensym;

	$pid = open3($p_in, $p_out, $p_err, $shell, "-i", "-l");

	close $p_in;

	waitpid($pid, 0);

	print "Exit status = ".($? >> 8)."\n";

	print $out $_ for <$p_out>;
	print $err $_ for <$p_err>;

	close $out;
	close $err;

	print "Child exiting\n";
}
else {
	# I am parent

	waitpid($pid, 0);
	print "Parent reaped child\n";

	run("rm", "$loc/etc/resolv.conf");
	run("umount", "$loc/$_") for qw(proc dev sys);

	print "Parent exiting\n";
}

sub run {
	printf("Running: %s\n", join(" ", @_));
	system(@_);
}
