package PRMF::Auth;

use warnings;
use strict;

use Digest::SHA qw(sha256_hex);
use DBI;

=head1 NAME

PRMF::Auth - The great new PRMF::Auth!

=head1 VERSION

This is PRMF::Auth version 0.00_01 (June 30, 2011).

=cut

our $VERSION = '0.00_01';

sub new {
	my($pkg, %opts) = @_;
	return bless({
		db => $opts{db},
		
		err => undef,
	}, $pkg);
}

sub _dbh {
	my $self = shift;
	unless(defined($self->{dbh})) {
		$self->{dbh} = DBI->connect('dbi:SQLite:dbname=' . ($self->{db}), '', '');
		$self->{dbh}->do("CREATE TABLE IF NOT EXISTS users (username TEXT UNIQUE NOT NULL, password TEXT NOT NULL, salt TEXT NOT NULL)");
	}
	return $self->{dbh};
}

sub add_user {
	my($self, $username, $password) = @_;
	my $ret = 0;
	if(length($username) < 3 || length($username) > 16) {
		$self->{err} = "Username must be between 3 and 16 characters in length.";
	}
	elsif($username =~ /[^A-Za-z0-9_]/) {
		$self->{err} = "Username may only contain A-Z, a-z, 0-9, or underscore.";
	}
	elsif($username !~ /^[A-Za-z]/) {
		$self->{err} = "Username must start with a letter.";
	}
	elsif(length($password) < 6) {
		$self->{err} = "Password must be at least 6 characters.";
	}
	elsif(length($password) > 32) {
		$self->{err} = "Password may not be longer than 32 characters.";
	}
	else {
		srand(time ^ $$ ^ unpack "%L*", `ps -ef | gzip -f` || `ps axww | gzip -f`);
		my $salt = join('', map {my @arr = ('A'..'Z', 'a'..'z', 0..9, '.', '/'); $arr[int(rand(scalar(@arr)))]} 1..16);
		my $sth = $self->_dbh->prepare("INSERT INTO users (username, password, salt) VALUES (?, ?, ?)");
		$password = sha256_hex($password . $salt);
		if($sth->execute($username, $password, $salt)) {
			$ret = 1;
		}
		elsif($sth->err == 19) { # constraint failed
			$self->{err} = "Username unavailable :(";
		}
		else {
			$self->{err} = "A previously unseen internal error has occurred! Thanks for discovering this for us; we'll try to have it fixed as soon as possible!";
		}
	}
	return $ret;
}

sub login {
	my($self, $username, $password) = @_;
	my $ret = 0;
	my $sth = $self->_dbh->prepare("SELECT salt FROM users WHERE username = ?");
	$sth->execute($username);
	my($salt) = $sth->fetchrow_array;
	if(defined($salt)) {
		$password = sha256_hex($password . $salt);
		$sth = $self->_dbh->prepare("SELECT COUNT(*) FROM users WHERE username = ? AND password = ?");
		$sth->execute($username, $password);
		$ret = ($sth->fetchrow_array)[0] == 1;
	}
	$self->{err} = "Wrong username or password." unless $ret;
	return $ret;
}

sub err {
	my $self = shift;
	return $self->{err};# || $self->_dbh->err;
}

1;

