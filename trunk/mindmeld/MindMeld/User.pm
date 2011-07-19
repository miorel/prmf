package MindMeld::User;

use warnings;
use strict;

use DBI;

use MindMeld;

=head1 NAME

MindMeld::User - The great new MindMeld::User!

=cut

sub _ensure_schema {
	my $dbh = MindMeld->dbh;
	$dbh->do('CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, password TEXT, salt TEXT, username TEXT)');
}

sub create {
	my $package = shift;
	my $dbh = MindMeld->dbh;
	$dbh->do('INSERT INTO users DEFAULT VALUES');
	my $id = MindMeld->_select('SELECT LAST_INSERT_ROWID()');
	return $id != 0 ? $package->retrieve($id) : undef;
}

sub retrieve {
	my($package, $id) = @_;
	return MindMeld->_select('SELECT COUNT(*) FROM users WHERE id = ?', $id) == 1 ? bless({_id => $id}, $package) : undef;
}

sub password {
	my($self, $new_password) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE users SET password = ? WHERE id = ?');
		$sth->execute($new_password, $self->{_id});
		$self->{_password} = $new_password;
	}
	elsif(!exists $self->{_password}) {
		$self->{_password} = MindMeld->_select('SELECT password FROM users WHERE id = ?', $self->{_id});
	}
	return $self->{_password};
}

sub salt {
	my($self, $new_salt) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE users SET salt = ? WHERE id = ?');
		$sth->execute($new_salt, $self->{_id});
		$self->{_salt} = $new_salt;
	}
	elsif(!exists $self->{_salt}) {
		$self->{_salt} = MindMeld->_select('SELECT salt FROM users WHERE id = ?', $self->{_id});
	}
	return $self->{_salt};
}

sub username {
	my($self, $new_username) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE users SET username = ? WHERE id = ?');
		$sth->execute($new_username, $self->{_id});
		$self->{_username} = $new_username;
	}
	elsif(!exists $self->{_username}) {
		$self->{_username} = MindMeld->_select('SELECT username FROM users WHERE id = ?', $self->{_id});
	}
	return $self->{_username};
}

1;

__END__
