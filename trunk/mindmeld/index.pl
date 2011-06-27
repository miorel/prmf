#!/usr/bin/perl

use warnings;
use strict;

use CGI;

srand(time ^ $$ ^ unpack "%L*", `ps axww | gzip -f`);

my $cgi = CGI->new;

my $info_str = 'mindmeld beta 201106250300';

print $cgi->header, $cgi->start_html($info_str);

my @data = ();
while(<DATA>) {
	chomp;
	@_ = split(/\t+/s, $_);
	push @data, {
		'Q' => "What is the capital of $_[0]?",
		'A' => $_[1],
	};
	push @data, {
		'Q' => "Which state's capital is $_[1]?",
		'A' => $_[0],
	};
}

my $action = $cgi->param('action');
if(defined($action) && $action eq 'showans') {
}
else {
	my $index = int(rand(scalar @data));
	my $q = $data[$index];
	print $cgi->div({-style => 'text-align:center;'},
		$cgi->p({-id => 'q'}, $q->{Q}),
		$cgi->p({-id => 'sa'}, $cgi->a({-href => 'javascript:void(0);', -onclick => q(javascript:document.getElementById('sa').style.display="none";document.getElementById('a').style.display="block";document.getElementById('nq').style.display="block";)}, 'Show Answer')),
		$cgi->p({-id => 'a', -style => 'display:none;'}, $q->{A}),
		$cgi->p({-id => 'nq', -style => 'display:none;'}, $cgi->a({-href => 'index.pl'}, "New Question")),
	);
}

print $cgi->hr, $cgi->p($info_str), $cgi->p('Copyright &copy; 2011 Miorel-Lucian Palii');
print $cgi->end_html;

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
