#!/usr/bin/perl -pl
#
# This script gets the algorithm packet's table of contents in Google Code wiki
# format. First generate the table of contents using `pdflatex algo.tex` then
# run: `./get_toc.pl < algo.toc`
#

/^\\contentsline\s*\{((?:sub)*)section\}\{\\numberline\s*\{[^\}]+\}([^\}]+)\}/;
$_ = " " x (2 + length($1) / 3) . "# $2";
