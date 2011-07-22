#!/usr/bin/perl

use warnings;
use strict;

generate_code(
	types => [
		{name => 'question', attr => {
			question => {type => '_text', constraint => 'NOT NULL'},
			answer => {type => '_text', constraint => 'NOT NULL'},
			category => {type => 'category', constraint => 'NOT NULL'},
			grade => {type => '_real', constraint => 'NOT NULL'},
		}},
		{name => 'category', attr => {
			name => {type => '_text', constraint => 'UNIQUE NOT NULL'},
			active => {type => '_boolean', constraint => 'NOT NULL'},
		}},
		{name => 'user', attr => {
			username => {type => '_text', constraint => 'UNIQUE NOT NULL'},
			password => {type => '_text', constraint => 'NOT NULL'},
			salt => {type => '_text', constraint => 'NOT NULL'},
		}},
		{name => 'session', attr => {
			user => {type => 'user', constraint => 'NOT NULL'},
#			location => {type => '_text', constraint => 'NOT NULL'},
			text => {type => '_text', constraint => 'UNIQUE NOT NULL'},
			expires => {type => '_integer'}, #, constraint => 'NOT NULL DEFAULT'},
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
		
		my @foreign_keys = ();
		
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
				push @foreign_keys, sprintf('FOREIGN KEY(%s) REFERENCES %s(%s)', $attr_name, plural($attr_type), 'id');
			}
			
			$sql .= ' ' . $attr->{constraint} if defined $attr->{constraint};
			
			$attr->{sql} = $sql;
		}
		
		# package name
		print "package $pkg;\n\n";

		# uses
		for(qw(warnings strict), '', @uses, '') {
			printf("%s\n", $_ ? "use $_;" : '');
		}

		# overload
		print q`use overload
	'""' => sub {
		return shift->{_id};
	},
;

`;

		# a bit of POD
		printf q^=head1 NAME

%1$s - The great new %1$s!

=cut

^, $pkg;

		# schema generation
		printf q^sub _ensure_schema {
	my $dbh = MindMeld->dbh;
	$dbh->do('CREATE TABLE IF NOT EXISTS %s (%s)');
^, plural($name), join(', ', 'id INTEGER PRIMARY KEY', (grep {defined $_} map {$attr{$_}->{sql}} sort keys %attr), @foreign_keys);
		print "}\n\n";

		# creation
		printf q^sub create {
	my($package, %%args) = @_;
	my $dbh = MindMeld->dbh;
	my $ret = undef;
	my @cols = ();
	my @vals = ();
	for(%2$s) {
		next if !exists($args{$_});
		push @cols, $_;
		push @vals, $args{$_};
	}
	my $sql = 'INSERT INTO %1$s ';
	$sql .= @cols ? '(' . join(', ', @cols) . ') VALUES (' . join(', ', map {'?'} @cols) . ')' : 'DEFAULT VALUES';
	if($dbh->prepare_cached($sql)->execute(@vals)) {
		my $id = MindMeld->_select('SELECT LAST_INSERT_ROWID()');
		if($id != 0 && defined($ret = $package->retrieve(id => $id))) {
			for(0..$#cols) {
				$ret->{'_' . $cols[$_]} = $vals[$_];
			}
		}
	}
	return $ret;
}

^, plural($name), join(', ', map {"'$_'"} sort keys %attr);

		# deletion
		printf q^sub destroy {
	my $self = shift;
	my $ret = MindMeld->dbh->prepare_cached('DELETE FROM %1$s WHERE id = ?')->execute($self->{_id});
	%%$self = () if $ret;
	return $ret;
}

^, plural($name);

		# retrieval
		printf q^sub retrieve {
	my($package, %%args) = @_;
	my @cols = ();
	my @vals = ();
	for(%2$s) {
		next if !exists($args{$_});
		push @cols, $_;
		push @vals, $args{$_};
	}
	my $ret = undef;
	if(@cols) {
		my $id = MindMeld->_select('SELECT id FROM %s WHERE ' . join(' AND ', map {"$_ = ?"} @cols), @vals);
		if($id != 0) {
			$ret = bless({_id => $id}, $package);
			for(0..$#cols) {
				$ret->{'_' . $cols[$_]} = $vals[$_];
			}
		}
	}
	return $ret;
}

^, plural($name), join(', ', map {"'$_'"} ('id', sort keys %attr));

		# accessors
		for my $attr_name (sort keys %attr) {
			my $attr = $attr{$attr_name};
			my $fetch_code = sprintf(q`MindMeld->_select('SELECT %1$s FROM %2$s WHERE id = ?', $self->{_id})`, $attr_name, plural($name));
			$fetch_code = sprintf("%s->retrieve(id => %s)", package_name($attr->{type}), $fetch_code) if $attr->{type} !~ /^_/;
			printf q^sub %1$s {
	my($self, $new_%1$s) = @_;
	if(exists $_[1]) {
		# needs some error checking!
		my $sth = MindMeld->dbh->prepare_cached('UPDATE %2$s SET %1$s = ? WHERE id = ?');
		if($sth->execute($new_%1$s, $self->{_id})) {
			$self->{_%1$s} = $new_%1$s;
		}
	}
	if(!exists $self->{_%1$s}) {
		$self->{_%1$s} = %3$s;
	}
	return $self->{_%1$s};
}

^, $attr_name, plural($name), $fetch_code;
		}

		# include external code
		my $inc_file = "include_$name.pl";
		if(-f $inc_file) {
			open my $fh, "<$inc_file" or die "Failed to open $inc_file for reading";
			print while <$fh>;
			close $fh;
		}

		# end
		print "1;\n";
	}
}
