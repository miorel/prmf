#!/usr/bin/perl

use warnings;
use strict;

use MIME::Lite;
use Net::SMTP::SSL;

my $msg = MIME::Lite->new(
	Subject	=> 'Hello World!',
	From	=> '"Example" <finchlee@example.org>',
	Data	=> qq(Hi there,

Have you lost the game recently?

Sincerely,
Finch Lee
));

$msg->attach(
	Type	=> 'text/plain',
	Path	=> 'file.txt',
	Filename => 'attachment.txt',
	Disposition => 'attachment',
);

my $smtp = Net::SMTP::SSL->new('mail.example.com', Port => 465);
$smtp->auth('username', 's3cr3t') or die "failed auth: " . $smtp->message;
$smtp->mail('finchlee@example.org') or die "failed mail: " . $smtp->message;
$smtp->to('rick@example.com') or die "failed to: " . $smtp->message;
$smtp->cc('al@example.edu') or die "failed cc: " . $smtp->message;
$smtp->data or die "failed data: " . $smtp->message;
$smtp->datasend($msg->as_string) or die "failed datasend: " . $smtp->message;
$smtp->dataend or die "failed dataend: " . $smtp->message;
$smtp->quit or die $smtp->message;
