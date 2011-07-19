package MindMeld::Category;

use warnings;
use strict;

use DBI;

use MindMeld;

=head1 NAME

MindMeld::Category - The great new MindMeld::Category!

=cut

sub _ensure_schema {
	my $dbh = MindMeld->dbh;
	$dbh->do('CREATE TABLE IF NOT EXISTS categories (id INTEGER PRIMARY KEY, active BOOLEAN, name TEXT)');
}

sub create {
	my $package = shift;
	my $dbh = MindMeld->dbh;
	$dbh->do('INSERT INTO categories DEFAULT VALUES');
	my $id = MindMeld->_select('SELECT LAST_INSERT_ROWID()');
	return $id != 0 ? $package->retrieve($id) : undef;
}

sub retrieve {
	my($package, $id) = @_;
	return MindMeld->_select('SELECT COUNT(*) FROM categories WHERE id = ?', $id) == 1 ? bless({_id => $id}, $package) : undef;
}

sub active {
	my($self, $new_active) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE categories SET active = ? WHERE id = ?');
		$sth->execute($new_active, $self->{_id});
		$self->{_active} = $new_active;
	}
	elsif(!exists $self->{_active}) {
		$self->{_active} = MindMeld->_select('SELECT active FROM categories WHERE id = ?', $self->{_id});
	}
	return $self->{_active};
}

sub name {
	my($self, $new_name) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE categories SET name = ? WHERE id = ?');
		$sth->execute($new_name, $self->{_id});
		$self->{_name} = $new_name;
	}
	elsif(!exists $self->{_name}) {
		$self->{_name} = MindMeld->_select('SELECT name FROM categories WHERE id = ?', $self->{_id});
	}
	return $self->{_name};
}

1;

__END__
