#!/usr/bin/perl

use warnings;
use strict;

print "random_antarctic_island () {\ncat << 'EOF' | random_line | chomp\n";
while(<DATA>) {
	chomp;
	$_ = lc;
	s/ island$//;
	next if /[^a-z]/;
	print "$_\n";
}
print "EOF\n}\n";

# List from "List of largest Antarctic islands south of 60° S by size" at
# http://en.wikipedia.org/wiki/List_of_Antarctic_and_subantarctic_islands
__DATA__
Alexander Island
Berkner Island
Thurston Island
Carney Island
Roosevelt Island
Siple Island
Adelaide Island
Spaatz Island
Bear Island
Guest Island
James Ross Island
Ross Island
Anvers Island
Joinville Island
Charcot Island
King George Island
Mill Island
Sherman Island
Smyley Island
Brabant Island
Livingston Island
Grant Island
Latady Island
Drygalski Island
Renaud Island
Masson Island
Elephant Island
Rothschild Island
Hearst Island
D'Urville Island
Coronation Island
Sturge Island
