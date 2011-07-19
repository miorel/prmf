#!/usr/bin/perl

use warnings;
use strict;

generate_code(
	types => [
		{name => 'question', attr => {
			question => {type => '_text'},
			answer => {type => '_text'},
			category => {type => 'category'},
			grade => {type => '_real'},
		}},
		{name => 'category', attr => {
			name => {type => '_text'},
			active => {type => '_boolean'},
		}},
		{name => 'user', attr => {
			username => {type => '_text'},
			password => {type => '_text'},
			salt => {type => '_text'},
		}},
	],
);

sub plural {
	my $noun = shift;
	my $ret = $noun . 's';
	$ret =~ s/ys$/ies/;
	$ret =~ s/ss$/ses/;
	return $ret;
}

sub package_name {
	my $type_name = shift;
	return sprintf('MindMeld::%s', ucfirst(lc($type_name)));
}

sub generate_code {
	local %_ = @_;
	my $db = $_{database};
	my @types = @{$_{types}};
	for(@types) {
		my $name = $_->{name};
		
		my $pkg = package_name($name);
		
		my @uses = ('DBI', '', 'MindMeld');
		
		my %attr = %{$_->{attr}};
		for my $attr_name (keys %attr) {
			my $attr = $attr{$attr_name};
			my $attr_type = $attr->{type};
			my $sql = lc($attr_name) . ' ';
			if($attr_type =~ s/^_//) {
				$sql .= uc($attr_type);
			}
			else {
				$sql .= 'INTEGER';
				push @uses, package_name($attr_type);
			}
			$attr->{sql} = $sql;
		}
		
		# package name
		print "package $pkg;\n\n";

		# uses
		for(qw(warnings strict), '', @uses, '') {
			printf("%s\n", $_ ? "use $_;" : '');
		}

		# a bit of POD
		printf q^=head1 NAME

%1$s - The great new %1$s!

=cut

^, $pkg;

		# schema generation
		printf q^sub _ensure_schema {
	my $dbh = MindMeld->dbh;
	$dbh->do('CREATE TABLE IF NOT EXISTS %s (%s)');
^, plural($name), join(', ', 'id INTEGER PRIMARY KEY', (grep {defined $_} map {$attr{$_}->{sql}} sort keys %attr));
		print "}\n\n";

		# creation
		printf q^sub create {
	my $package = shift;
	my $dbh = MindMeld->dbh;
	$dbh->do('INSERT INTO %s DEFAULT VALUES');
	my $id = MindMeld->_select('SELECT LAST_INSERT_ROWID()');
	return $id != 0 ? $package->retrieve($id) : undef;
}

^, plural($name);

		# retrieval
		printf q^sub retrieve {
	my($package, $id) = @_;
	return MindMeld->_select('SELECT COUNT(*) FROM %s WHERE id = ?', $id) == 1 ? bless({_id => $id}, $package) : undef;
}

^, plural($name);

		# accessors
		for my $attr_name (sort keys %attr) {
			my $attr = $attr{$attr_name};
			my $fetch_code = sprintf(q`MindMeld->_select('SELECT %1$s FROM %2$s WHERE id = ?', $self->{_id})`, $attr_name, plural($name));
			$fetch_code = sprintf("%s->retrieve(%s)", package_name($attr->{type}), $fetch_code) if $attr->{type} !~ /^_/;
			printf q^sub %1$s {
	my($self, $new_%1$s) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE %2$s SET %1$s = ? WHERE id = ?');
		$sth->execute($new_%1$s, $self->{_id});
		$self->{_%1$s} = $new_%1$s;
	}
	elsif(!exists $self->{_%1$s}) {
		$self->{_%1$s} = %3$s;
	}
	return $self->{_%1$s};
}

^, $attr_name, plural($name), $fetch_code;
		}

		# end
		print "1;\n\n__END__\n";
	}
}
