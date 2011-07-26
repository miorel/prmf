#!/usr/bin/perl

use warnings;
use strict;

sub write_svg_file {
	my($obj, $fh) = @_;
	$fh ||= \*STDOUT;
	print $fh q(<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
);
	$obj = {%$obj};
	$obj->{tag} = 'svg';
	$obj->{attr} ||= {};
	$obj->{attr}->{xmlns} = "http://www.w3.org/2000/svg";
	$obj->{attr}->{version} = "1.1";
	write_xml($obj, $fh);
}

sub write_xml {
	my($obj, $fh, $nl) = @_;
	$fh ||= \*STDOUT;
	$nl = 1 unless defined($nl);
	if(ref($obj) eq 'HASH') {
		my $tag = $obj->{tag};
		my $attr = $obj->{attr} || {};
		printf($fh "<%s%s", $tag, join('', map {sprintf(" %s=\"%s\"", $_, $attr->{$_})} keys %$attr));
		my $children = $obj->{children} || [];
		if(@$children) {
			print $fh ">";
			write_xml($_, $fh, 0) for @$children;
			print $fh "</$tag>";
		}
		else {
			print $fh "/>";
		}
	}
	else {
		print $fh $obj;
	}
	print $fh "\n" if $nl;
}

1;
