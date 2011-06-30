#!/usr/bin/perl

use warnings;
use strict;

use DBI;

my @data = ();
while(<DATA>) {
	chomp;
	@_ = split(/\t+/s, $_);
	push @data, {
		'cat' => 'Biochemistry - Amino Acid Abbreviations',
		'Q' => "What is the three-letter code for $_[0]?",
		'A' => $_[1],
	};
	push @data, {
		'cat' => 'Biochemistry - Amino Acid Abbreviations',
		'Q' => "What is the one-letter code for $_[0]?",
		'A' => $_[2],
	};
	push @data, {
		'cat' => 'Biochemistry - Amino Acid Abbreviations',
		'Q' => "What does the amino acid code $_[1] represent?",
		'A' => $_[0],
	};
	push @data, {
		'cat' => 'Biochemistry - Amino Acid Abbreviations',
		'Q' => "What does the amino acid code $_[2] represent?",
		'A' => $_[0],
	};
}

#unlink('mm.db');
my $dbh = DBI->connect("dbi:SQLite:dbname=mm.db", "", "");
$dbh->do("CREATE TABLE IF NOT EXISTS questions (q TEXT, a TEXT, cat TEXT, grade REAL DEFAULT 0)");
$dbh->do("CREATE TABLE IF NOT EXISTS categories (name TEXT UNIQUE NOT NULL, active BOOLEAN DEFAULT 1 NOT NULL)");
my $sth;
$sth = $dbh->prepare("INSERT INTO categories (name) VALUES (?)");
$sth->execute($_) for sort keys(%{{map {$_->{cat} => 1} @data}});
$sth = $dbh->prepare("INSERT INTO questions (q, a, cat) VALUES (?, ?, (SELECT rowid FROM categories WHERE name = ?))");
for(@data) {
	$sth->execute($_->{Q}, $_->{A}, $_->{cat});
}

#mnemosyne_output(\@data);

sub mnemosyne_output {
	my @data = @{shift()};
	print qq(<?xml version="1.0" encoding="UTF-8"?>\n<mnemosyne core_version="1">\n);
	print qq(<category active="1"><name>$_</name></category>\n) for sort keys(%{{map {$_->{cat} => 1} @data}});
	print qq(<item id="$_"><cat>$data[$_]->{cat}</cat><Q>$data[$_]->{Q}</Q><A>$data[$_]->{A}</A></item>\n) for 0..$#data;
	print "</mnemosyne>\n";
}

__DATA__
alanine	Ala	A
arginine	Arg	R
asparagine	Asn	N
aspartic acid	Asp	D
cysteine	Cys	C
glutamic acid	Glu	E
glutamine	Gln	Q
glycine	Gly	G
histidine	His	H
isoleucine	Ile	I
leucine	Leu	L
lysine	Lys	K
methionine	Met	M
phenylalanine	Phe	F
proline	Pro	P
serine	Ser	S
threonine	Thr	T
tryptophan	Trp	W
tyrosine	Tyr	Y
valine	Val	V
selenocysteine	Sec	U
pyrrolysine	Pyl	O
asparagine or aspartic acid	Asx	B
glutamine or glutamic acid	Glx	Z
leucine or isoleucine	Xle	J
an unspecified or unknown amino acid	Xaa	X
