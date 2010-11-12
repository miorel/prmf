#!/usr/bin/perl

@names = ();

# a few characters from "Surf's Up"
push @names, qw(cody lani tank zeke);

# some people that really like penguins
push @names, qw(anhthu becca joe);

# some characters from "Pingu"
# didn't include Ping because of confusion with computer term
push @names, qw(pinga pingi pingo pingu pongi punki);

# Linux \o/
push @names, qw(tux tuxette);

# characters from "Pororo the Little Penguin"
push @names, qw(petty pororo);

# characters from "Madagascar"
# didn't include Private because it's not obviously a name
push @names, qw(kowalski rico skipper);

# various other penguins from comics, games, movies, etc.
push @names, qw(dippy hanako opus pablo pentarou percival pokey sam sparky wheezy);

# penguins that "And Tango Makes Three" was based on
push @names, qw(roy scrappy silo tango tanuzi);

# characters from "Pecola"
push @names, qw(coco pecola pecolius);

# some penguin species that make good names
push @names, qw(adelie humboldt magellan rockhopper adelie);

# sort, uniq
@names = sort keys %{ {map {$_=>1} @names} };

print $names[int(rand(1+$#names))], "\n";
