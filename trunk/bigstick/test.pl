#!/usr/bin/perl

use warnings;
use strict;

use Carp;
use File::Slurp;
use IPC::Open3;
use Symbol qw(gensym);
use Test::More;

filter_ok("line_count", "", 0, "empty input");
filter_ok("line_count", "\n", 1, "single empty line");
filter_ok("line_count", "hello world", 1, "single line, no trailing newline");
filter_ok("line_count", "hello world\n", 1, "single line, trailing newline");
filter_ok("line_count", "hello\rworld\n", 1, "single line, return in the middle");
filter_ok("line_count", "\r\r\r\r\r\r\r", 1, "a bunch of returns");
filter_ok("line_count", "\n\n\n\n\n\n\n", 7, "a bunch of newlines");
filter_ok("line_count", "hello\nworld", 2, "multi-line, no trailing newline");
filter_ok("line_count", "hello\nworld\n", 2, "multi-line, trailing newline");
filter_ok("line_count", join("", map {"$_\n"} 1..1000), 1000, "a thousand lines of numbers");
filter_ok("line_count", scalar read_file("COPYING"), 674, "text of GPLv3");

filter_ok("first_line", "", "", "empty input");
filter_ok("first_line", "\n", "\n", "single empty line");
filter_ok("first_line", "hello world", "hello world", "single line, no trailing newline");
filter_ok("first_line", "hello world\n", "hello world\n", "single line, trailing newline");
filter_ok("first_line", "hello\rworld\n", "hello\rworld\n", "single line, return in the middle");
filter_ok("first_line", "\r\r\r\r\r\r\r", "\r\r\r\r\r\r\r", "a bunch of returns");
filter_ok("first_line", "\n\n\n\n\n\n\n", "\n", "a bunch of newlines");
filter_ok("first_line", "hello\nworld", "hello\n", "multi-line text");
filter_ok("first_line", "hello\nworld\n", "hello\n", "multi-line text");
filter_ok("first_line", join("", map {"$_\n"} 1..1000), "1\n", "a thousand lines of numbers");

filter_ok("last_line", "", "", "empty input");
filter_ok("last_line", "\n", "\n", "single empty line");
filter_ok("last_line", "hello world", "hello world", "single line, no trailing newline");
filter_ok("last_line", "hello world\n", "hello world\n", "single line, trailing newline");
filter_ok("last_line", "hello\rworld\n", "hello\rworld\n", "single line, return in the middle");
filter_ok("last_line", "\r\r\r\r\r\r\r", "\r\r\r\r\r\r\r", "a bunch of returns");
filter_ok("last_line", "\n\n\n\n\n\n\n", "\n", "a bunch of newlines");
filter_ok("last_line", "hello\nworld", "world", "multi-line text, no trailing newline");
filter_ok("last_line", "hello\nworld\n", "world\n", "multi-line text, trailing new line");
filter_ok("last_line", join("", map {"$_\n"} 1..1000), "1000\n", "a thousand lines of numbers");

filter_ok("chomp", "", "", "empty input");
filter_ok("chomp", "\n", "", "single empty line");
filter_ok("chomp", "hello world", "hello world", "single line, no trailing newline");
filter_ok("chomp", "hello world\n", "hello world", "single line, trailing newline");
filter_ok("chomp", "hello\rworld\n", "helloworld", "single line, return in the middle");
filter_ok("chomp", "\r\r\r\r\r\r\r", "", "a bunch of returns");
filter_ok("chomp", "\n\n\n\n\n\n\n", "", "a bunch of newlines");
filter_ok("chomp", "hello\nworld", "helloworld", "multi-line text, no trailing newline");
filter_ok("chomp", "hello\nworld\n", "helloworld", "multi-line text, trailing new line");
filter_ok("chomp", join("", map {"$_\n"} 1..1000), join("", 1..1000), "a thousand lines of numbers");




# need to test first_word
# need to test last_word
# need to test trim
# need to test collapse
# need to test strip_empties

done_testing();

sub filter_ok {
	my($filter_name, $input, $correct_output, $desc) = @_;
	$desc = "$filter_name: $desc";

	my @problems = ();

	my($in, $out, $err);
	$err = gensym;

	# give input
	my $pid = open3($in, $out, $err, qw(bash -c), "source bs-main.sh; $filter_name");
	print $in $input;
	close $in;

	# wait for output
	waitpid($pid, 0);

	# check exit code
	my $ec = $? >> 8;
	if($ec != 0) {
		diag("Function exited with code $ec.\n");
		fail($desc);
	}
	else {
		local $/ = undef;
		my $output = <$out>;
		is($output, $correct_output, $desc);
	}

	# close streams
	close $out;
	close $err;
}
