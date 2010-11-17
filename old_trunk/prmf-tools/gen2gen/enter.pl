#!/usr/bin/perl

use warnings;
use strict;

use BSD::Resource;
use IPC::Open3;

{
	no warnings;
	no strict;
	use Inline C;
}

my($loc, $user, $pid);

# force loading these functions now
# (otherwise BSD::Resource will try to get them inside chroot and probably fail)
eval { &getrlimit; };
eval { &setrlimit; };

defined($loc = shift) or die "no location specified";
die "$loc is not a directory" unless -d $loc;

defined($user = shift) or $user = 'root';

my $shell_pref = '/bin/bash';

run(qw(mount -t proc none), "$loc/proc");
run(qw(mount -o bind /dev), "$loc/dev");
run(qw(mount -t sysfs none), "$loc/sys");

run(qw(cp -L /etc/resolv.conf), "$loc/etc/resolv.conf");

defined($pid = fork()) or die "forking failed";
if($pid == 0) {
#	my($out, $err);
#	open $out, qw(> out.txt);
#	open $err, qw(> err.txt);

	chdir $loc;
	chroot '.';

	my($uid, $gid, $home, $shell) = (getpwnam($user))[2,3,7,8];
	if($shell ne $shell_pref) {
		print "Warning: user shell was $shell, not $shell_pref as preferred!\nWill use $shell_pref anyway because that's what we know and love.\n";
		$shell = $shell_pref;
	}

	local @_;
	while(@_ = getgrent()) {
		next if $_[2] == 0 && $user ne 'root';
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

		limit(RLIMIT_NPROC, 32);

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

#		open STDOUT, ">&", $out;
#		open STDERR, ">&", $err;
	}

#	close $out;
#	close $err;

	if($pid == 0) {
#		close STDIN;
		traceme();
		kill "STOP", $$;
		my @cmd = ($shell, qw(-l -i));
		exec { $cmd[0] } @cmd;
	}
	else {
		mon();
		print "Child exiting, status = ".($? >> 8)."\n";
	}	
}
else {
	waitpid($pid, 0);
	print "Parent reaped child\n";

	unlink "$loc/etc/resolv.conf";

	run('umount', "$loc/$_") for qw(proc dev sys);

	print "Parent exiting\n";
}

sub run {
	printf("Running: %s\n", join(" ", @_));
	system {$_[0]} @_;
}

sub limit {
	my($resource, $new_limit) = @_;
	my $old_limit = (getrlimit($resource))[0];
	$new_limit = $old_limit if $new_limit > $old_limit;
	return setrlimit($resource, $new_limit, $new_limit);
}

__END__
__C__

#include <signal.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h>

void traceme() {
	ptrace(PTRACE_TRACEME, 0, 0, 0);
}

void mon() {
	int status, kids = 1;
	siginfo_t sig;
	pid_t pid;
	while(kids > 0) {
		pid = wait(&status);
		ptrace(PTRACE_GETSIGINFO, pid, 0, &sig);

		if(sig.si_signo == SIGTRAP) {
			switch(sig.si_code >> 8) {
			case PTRACE_EVENT_FORK:
			case PTRACE_EVENT_VFORK:
			case PTRACE_EVENT_CLONE:
				++kids;
				break;
			}
		}		

		if(WIFSTOPPED(status)) {
			ptrace(PTRACE_SETOPTIONS, pid, 0, PTRACE_O_TRACEFORK | PTRACE_O_TRACEVFORK | PTRACE_O_TRACECLONE);
			ptrace(PTRACE_SYSCALL, pid, 0, 0);
		}
		else {
			--kids;
		}
	}
}
