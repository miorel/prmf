package App::ShellPack;

use warnings;
use strict;

use Archive::Tar;
use File::Slurp;
use File::Temp;

our $VERSION = '0.00_01';

=head1 NAME

App::ShellPack - The great new App::ShellPack!

=cut

sub _work {
	my $package = shift;
	my $tar = Archive::Tar->new;
	for(@_) {
		$tar->add_files($_);
	}
	my $tmpfile = File::Temp->new(UNLINK => 0);
	$tar->write($tmpfile);
	$tmpfile->close;
	my $data = read_file($tmpfile->filename, binmode => ':raw');
	unlink $tmpfile->filename;
	my $hex_data = unpack("H*", $data);
	my $ret = '';
	$ret .= <<'SCRIPT';
function _extract {
	destdir=.
	[[ $# -ge 1 ]] && destdir=$1
	fold -w2 << 'HEX' | while read hex; do printf "\x$hex"; done | tar xvf - -C $destdir
SCRIPT
	$ret .= "$_\n" for $hex_data =~ /.{1,80}/g;
	$ret .= "HEX\n}\n";
	return $ret;
}

1;

__END__
#package App::ShellPack;
#
#use warnings;
#use strict;
#
#=head1 NAME
#
#App::ShellPack - The great new App::ShellPack!
#
#=head1 VERSION
#
#Version 0.01
#
#=cut
#
#our $VERSION = '0.01';
#
#
#=head1 SYNOPSIS
#
#Quick summary of what the module does.
#
#Perhaps a little code snippet.
#
#    use App::ShellPack;
#
#    my $foo = App::ShellPack->new();
#    ...
#
#=head1 EXPORT
#
#A list of functions that can be exported.  You can delete this section
#if you don't export anything, such as for a purely object-oriented module.
#
#=head1 SUBROUTINES/METHODS
#
#=head2 function1
#
#=cut
#
#sub function1 {
#}
#
#=head2 function2
#
#=cut
#
#sub function2 {
#}
#
#=head1 AUTHOR
#
#Miorel-Lucian Palii, C<< <mlpalii at gmail.com> >>
#
#=head1 BUGS
#
#Please report any bugs or feature requests to C<bug-app-shellpack at rt.cpan.org>, or through
#the web interface at L<http://rt.cpan.org/NoAuth/ReportBug.html?Queue=App-ShellPack>.  I will be notified, and then you'll
#automatically be notified of progress on your bug as I make changes.
#
#
#
#
#=head1 SUPPORT
#
#You can find documentation for this module with the perldoc command.
#
#    perldoc App::ShellPack
#
#
#You can also look for information at:
#
#=over 4
#
#=item * RT: CPAN's request tracker
#
#L<http://rt.cpan.org/NoAuth/Bugs.html?Dist=App-ShellPack>
#
#=item * AnnoCPAN: Annotated CPAN documentation
#
#L<http://annocpan.org/dist/App-ShellPack>
#
#=item * CPAN Ratings
#
#L<http://cpanratings.perl.org/d/App-ShellPack>
#
#=item * Search CPAN
#
#L<http://search.cpan.org/dist/App-ShellPack/>
#
#=back
#
#
#=head1 ACKNOWLEDGEMENTS
#
#
#=head1 LICENSE AND COPYRIGHT
#
#Copyright 2010 Miorel-Lucian Palii.
#
#This program is free software; you can redistribute it and/or modify it
#under the terms of either: the GNU General Public License as published
#by the Free Software Foundation; or the Artistic License.
#
#See http://dev.perl.org/licenses/ for more information.
#
#
#=cut
#
#1; # End of App::ShellPack
