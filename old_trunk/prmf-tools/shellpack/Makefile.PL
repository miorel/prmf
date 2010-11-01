use strict;
use warnings;
use ExtUtils::MakeMaker;

WriteMakefile(
    NAME                => 'App::ShellPack',
    AUTHOR              => q{Miorel-Lucian Palii <mlpalii@gmail.com>},
    VERSION_FROM        => 'lib/App/ShellPack.pm',
    ABSTRACT_FROM       => 'lib/App/ShellPack.pm',
    ($ExtUtils::MakeMaker::VERSION >= 6.3002
      ? ('LICENSE'=> 'perl')
      : ()),
    PL_FILES            => {},
    PREREQ_PM => {
        'Test::More' => 0,
    },
    dist                => { COMPRESS => 'gzip -9f', SUFFIX => 'gz', },
    clean               => { FILES => 'App-ShellPack-*' },
);
