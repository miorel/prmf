#!/usr/bin/perl

use warnings;
use strict;

use lib qw(.);

use MindMeld;

use MindMeld::Category;
use MindMeld::Question;

my @data = ();
while(<DATA>) {
	chomp;
	@_ = split(/\t+/s, $_);
	push @data, {
		'cat' => 'Geography - USA State Capitals',
		'Q' => "What is the capital of $_[0]?",
		'A' => $_[1],
	};
	push @data, {
		'cat' => 'Geography - USA State Capitals',
		'Q' => "Which state's capital is $_[1]?",
		'A' => $_[0],
	};
}

MindMeld->dbh->begin_work;

my %cat = ();

for(@data) {
	my $cat = $_->{cat};
	$cat{$cat} = MindMeld::Category->create(name => $cat, active => 1)
		unless exists $cat{$cat};
	MindMeld::Question->create(
		category => $cat{$_->{cat}},
		question => $_->{Q},
		answer => $_->{A},
		grade => 0,
	);
}

MindMeld->dbh->commit;

#mnemosyne_output(\@data);

sub mnemosyne_output {
	my @data = @{shift()};
	print qq(<?xml version="1.0" encoding="UTF-8"?>\n<mnemosyne core_version="1">\n);
	print qq(<category active="1"><name>$_</name></category>\n) for sort keys(%{{map {$_->{cat} => 1} @data}});
	print qq(<item id="$_"><cat>$data[$_]->{cat}</cat><Q>$data[$_]->{Q}</Q><A>$data[$_]->{A}</A></item>\n) for 0..$#data;
	print "</mnemosyne>\n";
}

__DATA__
Alabama	Montgomery
Alaska	Juneau
Arizona	Phoenix
Arkansas	Little Rock
California	Sacramento
Colorado	Denver
Connecticut	Hartford
Delaware	Dover
Florida	Tallahassee
Georgia	Atlanta
Hawaii	Honolulu
Idaho	Boise
Illinois	Springfield
Indiana	Indianapolis
Iowa	Des Moines
Kansas	Topeka
Kentucky	Frankfort
Louisiana	Baton Rouge
Maine	Augusta
Maryland	Annapolis
Massachusetts	Boston
Michigan	Lansing
Minnesota	Saint Paul
Mississippi	Jackson
Missouri	Jefferson City
Montana	Helena
Nebraska	Lincoln
Nevada	Carson City
New Hampshire	Concord
New Jersey	Trenton
New Mexico	Santa Fe
New York	Albany
North Carolina	Raleigh
North Dakota	Bismarck
Ohio	Columbus
Oklahoma	Oklahoma City
Oregon	Salem
Pennsylvania	Harrisburg
Rhode Island	Providence
South Carolina	Columbia
South Dakota	Pierre
Tennessee	Nashville
Texas	Austin
Utah	Salt Lake City
Vermont	Montpelier
Virginia	Richmond
Washington	Olympia
West Virginia	Charleston
Wisconsin	Madison
Wyoming	Cheyenne
