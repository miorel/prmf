#!/usr/bin/perl

use warnings;
use strict;

use IPC::Open3;

my $loc = shift;
die "no location specified" unless defined $loc;
die "$loc is not a directory" unless -d $loc;

my $user = shift;
my $shell_pref = "/bin/bash";

run("mount", "-t", "proc", "none", "$loc/proc");
run("mount", "-o", "bind", "/dev", "$loc/dev");
run("mount", "-t", "sysfs", "none", "$loc/sys");

run("cp", "-L", "/etc/resolv.conf", "$loc/etc/resolv.conf");

my $pid;

defined($pid = fork()) or die "forking failed";

if($pid == 0) {
	my($out, $err);
	open $out, ">", "out.txt";
	open $err, ">", "err.txt";

	chdir $loc;
	chroot '.';

	$user = "root" unless defined $user;

	my($uid, $gid, $home, $shell) = (getpwnam($user))[2,3,7,8];
	if($shell ne $shell_pref) {
		print "Warning: user shell was $shell, not $shell_pref as preferred, will use $shell_pref anyway.\n";
		$shell = $shell_pref;
	}

	local @_;
	while(@_ = getgrent()) {
		next if $_[2] == 0 && $user ne "root";
		for(split(/\s+/, $_[3])) {
			$gid .= " $_[2]" and last if $_ eq $user;
		}
	}

	chdir $home;

	defined($pid = fork()) or die "forking failed";
	if($pid == 0) {
		for(keys %ENV) {
			delete $ENV{$_} unless $_ eq 'TERM';
		}
		$ENV{USER} = $user;
		$ENV{HOME} = $home;
		$ENV{SHELL} = $shell;

		$( = $) = $gid;
		$< = $> = $uid;

		my($p_in, $p_out);
		my $pid = open3($p_in, $p_out, '/dev/null', $shell, "-i", "-l");
		print $p_in q(perl -MData::Dumper -e 'print Dumper(\%ENV), "#"')."\n";
		close $p_in;
		waitpid($pid, 0);
		{
			no warnings;
			no strict;
			undef $/;
			my $str = <$p_out>;
			$str =~ s/[^ -~\s]//g;
			$str =~ s/[\r\n]//g;
			eval $str;
			%ENV = %$VAR1;
			delete $ENV{_};
			if(defined($ENV{SHLVL})) {
				--$ENV{SHLVL};
				delete $ENV{SHLVL} if $ENV{SHLVL} <= 0;
			}
		}

#		system { $shell } ($shell, "-l", "-i");

		open STDOUT, ">&", $out;
		open STDERR, ">&", $err;

		open my $fh, ">", "cmd.sh";
		print $fh <<'EOF';
#cd prmf
#svn update >& /dev/null
#cd
#tar cjf prmf.tbz2 prmf >& /dev/null
#perl prmf/old_trunk/consigliere/main.pl prmf
#rm -rf prmf
#tar xjf prmf.tbz2 >& /dev/null
#rm -rf prmf.tbz2
EOF
		close $fh;
	}

	close $out;
	close $err;

	if($pid == 0) {
		close STDIN;
		my @cmd = ($shell, "cmd.sh");
		exec { $cmd[0] } @cmd;
	}
	else {
		waitpid($pid, 0);
		print "Child exiting, status = ".($? >> 8)."\n";
	}	
}
else {
	waitpid($pid, 0);
	print "Parent reaped child\n";

	unlink "$loc/etc/resolv.conf";

	run("umount", "$loc/$_") for qw(proc dev sys);

	print "Parent exiting\n";
}

sub run {
	printf("Running: %s\n", join(" ", @_));
	system {$_[0]} @_;
}
