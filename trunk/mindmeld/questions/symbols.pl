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
	$_[1] = lc $_[1];
	push @data, {
		'cat' => 'Chemistry - Atomic Numbers',
		'Q' => "What is the atomic number of $_[1]?",
		'A' => $_[0],
	};
	push @data, {
		'cat' => 'Chemistry - Atomic Numbers',
		'Q' => "Which element has atomic number $_[0]?",
		'A' => $_[1],
	};
	push @data, {
		'cat' => 'Chemistry - Element Symbols',
		'Q' => "What is the chemical symbol for $_[1]?",
		'A' => $_[2],
	};
	push @data, {
		'cat' => 'Chemistry - Element Symbols',
		'Q' => "Which element has chemical symbol $_[2]?",
		'A' => $_[1],
	};
}

MindMeld->dbh->begin_work;

my %cat = ();

for(@data) {
	my $cat = $_->{cat};
	$cat{$cat} = MindMeld::Category->create(name => $cat)
		unless exists $cat{$cat};
	MindMeld::Question->create(
		category => $cat{$_->{cat}},
		question => $_->{Q},
		answer => $_->{A},
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
1	hydrogen	H
2	helium	He
3	lithium	Li
4	beryllium	Be
5	boron	B
6	carbon	C
7	nitrogen	N
8	oxygen	O
9	fluorine	F
10	neon	Ne
11	sodium	Na
12	magnesium	Mg
13	aluminium / aluminum	Al
14	silicon	Si
15	phosphorus	P
16	sulfur / sulphur	S
17	chlorine	Cl
18	argon	Ar
19	potassium	K
20	calcium	Ca
21	scandium	Sc
22	titanium	Ti
23	vanadium	V
24	chromium	Cr
25	manganese	Mn
26	iron	Fe
27	cobalt	Co
28	nickel	Ni
29	copper	Cu
30	zinc	Zn
31	gallium	Ga
32	germanium	Ge
33	arsenic	As
34	selenium	Se
35	bromine	Br
36	krypton	Kr
37	rubidium	Rb
38	strontium	Sr
39	yttrium	Y
40	zirconium	Zr
41	niobium	Nb
42	molybdenum	Mo
43	technetium	Tc
44	ruthenium	Ru
45	rhodium	Rh
46	palladium	Pd
47	silver	Ag
48	cadmium	Cd
49	indium	In
50	tin	Sn
51	antimony	Sb
52	tellurium	Te
53	iodine	I
54	xenon	Xe
55	caesium/cesium	Cs
56	barium	Ba
57	lanthanum	La
58	cerium	Ce
59	praseodymium	Pr
60	neodymium	Nd
61	promethium	Pm
62	samarium	Sm
63	europium	Eu
64	gadolinium	Gd
65	terbium	Tb
66	dysprosium	Dy
67	holmium	Ho
68	erbium	Er
69	thulium	Tm
70	ytterbium	Yb
71	lutetium	Lu
72	hafnium	Hf
73	tantalum	Ta
74	tungsten	W
75	rhenium	Re
76	osmium	Os
77	iridium	Ir
78	platinum	Pt
79	gold	Au
80	mercury	Hg
81	thallium	Tl
82	lead	Pb
83	bismuth	Bi
84	polonium	Po
85	astatine	At
86	radon	Rn
87	francium	Fr
88	radium	Ra
89	actinium	Ac
90	thorium	Th
91	protactinium	Pa
92	uranium	U
93	neptunium	Np
94	plutonium	Pu
95	americium	Am
96	curium	Cm
97	berkelium	Bk
98	californium	Cf
99	einsteinium	Es
100	fermium	Fm
101	mendelevium	Md
102	nobelium	No
103	lawrencium	Lr
104	rutherfordium	Rf
105	dubnium	Db
106	seaborgium	Sg
107	bohrium	Bh
108	hassium	Hs
109	meitnerium	Mt
110	darmstadtium	Ds
111	roentgenium	Rg
112	copernicium	Cn
