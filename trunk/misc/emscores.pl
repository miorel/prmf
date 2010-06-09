#!/usr/bin/perl
#
# Script to check EpicMafia scores and report them in an IRC channel
#
# Copyright (C) 2010 Miorel-Lucian Palii
# This code is licensed under the GPLv3 or any later version.
# <http://www.gnu.org/licenses/gpl.html>
#

use warnings;
use strict;

use IO::Socket;
use LWP::UserAgent;
use Switch;

my $sock = new IO::Socket::INET(
	PeerAddr => 'irc.esper.net',
	PeerPort => '6667',
	Proto => 'tcp')
or die "Could not create socket: $!\n";

my $nick = "imabot42";
my $username = $nick;
my $realname = "I'm a bot!";

my @channels = ("#epicmafia");

irc_cmd("NICK", $nick);
irc_cmd("USER", $username, 12, "*", $realname);

while(<$sock>) {
	s/\r?\n?\z//;
	print "<<< $_\n";
	
	/(?::([^ ]+) )?([^ ]+) ?(.*)/;
	my($entity, $cmd) = ($1, $2);
	my @param = $3 =~ /:.*|[^ ]+/g;
	$param[-1] =~ s/^://;
	
	switch($cmd) {
		case "PRIVMSG" {
			my($medium, $msg) = @param;
			if($msg =~ /^\s*\Q!\E\s*(\w+)\s*(.*)/) {
				process_command($medium, $1, $2);
			}
		}
		case "PING" {
			irc_cmd("PONG", @param);
		}
		case 376 {
			for my $channel (@channels) {
				irc_cmd("JOIN", $channel);
				
				my $pid = fork;
				if($pid == 0) {
					sleep 15;
					my $old_scores = "";
					for(;;) {
						my $new_scores = score_string();
						if(defined($new_scores) && $new_scores ne $old_scores) {
							irc_cmd("PRIVMSG", $channel, $new_scores);
							$old_scores = $new_scores;
						}
						sleep 300;
					}
					exit;
				}
			}
		}
	}
}

sub irc_cmd {
	my $cmd = uc(shift);
	my @param = @_;
	$param[-1] = ":$param[-1]" if $param[-1] =~ / /;
	irc_send(join(" ", $cmd, @param));
}

sub irc_send {
	for(@_) {
		print $sock "$_\r\n";
		print ">>> $_\n";
	}
}

sub score_string {
	my $medium = shift;

	my $ret = undef;
	
	my $ua = LWP::UserAgent->new;
	$ua->timeout(10);
	$ua->env_proxy;

	my $response = $ua->get('http://www.epicmafia.com/score');

	if($response->is_success) {
		my @scores = ();
		$response->decoded_content =~ /\Q<table class='gametable'>\E(.*?)<\/table>/s;
		for($1 =~ /<tr>(.*?)<\/tr>/sg) {
			s/<[^>]+>/ /g;
			my($user, $lives, $score) = /\S+/g;
			push @scores, "$user - $score";
		}
		if(@scores) {
			$ret = "EpicMafia score update! " . join(", ", @scores[0..4]);
		}
	}

	return $ret;
}
