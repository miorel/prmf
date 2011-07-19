package MindMeld::Question;

use warnings;
use strict;

use DBI;

use MindMeld;
use MindMeld::Category;

=head1 NAME

MindMeld::Question - The great new MindMeld::Question!

=cut

sub _ensure_schema {
	my $dbh = MindMeld->dbh;
	$dbh->do('CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY, answer TEXT, category INTEGER, grade REAL, question TEXT)');
}

sub create {
	my $package = shift;
	my $dbh = MindMeld->dbh;
	$dbh->do('INSERT INTO questions DEFAULT VALUES');
	my $id = MindMeld->_select('SELECT LAST_INSERT_ROWID()');
	return $id != 0 ? $package->retrieve($id) : undef;
}

sub retrieve {
	my($package, $id) = @_;
	return MindMeld->_select('SELECT COUNT(*) FROM questions WHERE id = ?', $id) == 1 ? bless({_id => $id}, $package) : undef;
}

sub answer {
	my($self, $new_answer) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE questions SET answer = ? WHERE id = ?');
		$sth->execute($new_answer, $self->{_id});
		$self->{_answer} = $new_answer;
	}
	elsif(!exists $self->{_answer}) {
		$self->{_answer} = MindMeld->_select('SELECT answer FROM questions WHERE id = ?', $self->{_id});
	}
	return $self->{_answer};
}

sub category {
	my($self, $new_category) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE questions SET category = ? WHERE id = ?');
		$sth->execute($new_category, $self->{_id});
		$self->{_category} = $new_category;
	}
	elsif(!exists $self->{_category}) {
		$self->{_category} = MindMeld::Category->retrieve(MindMeld->_select('SELECT category FROM questions WHERE id = ?', $self->{_id}));
	}
	return $self->{_category};
}

sub grade {
	my($self, $new_grade) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE questions SET grade = ? WHERE id = ?');
		$sth->execute($new_grade, $self->{_id});
		$self->{_grade} = $new_grade;
	}
	elsif(!exists $self->{_grade}) {
		$self->{_grade} = MindMeld->_select('SELECT grade FROM questions WHERE id = ?', $self->{_id});
	}
	return $self->{_grade};
}

sub question {
	my($self, $new_question) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE questions SET question = ? WHERE id = ?');
		$sth->execute($new_question, $self->{_id});
		$self->{_question} = $new_question;
	}
	elsif(!exists $self->{_question}) {
		$self->{_question} = MindMeld->_select('SELECT question FROM questions WHERE id = ?', $self->{_id});
	}
	return $self->{_question};
}

1;

__END__
