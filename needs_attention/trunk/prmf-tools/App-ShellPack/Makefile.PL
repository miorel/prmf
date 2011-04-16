use warnings;
use strict;

use ExtUtils::MakeMaker;

WriteMakefile(
    NAME                => 'App::ShellPack',
    AUTHOR              => 'Miorel-Lucian Palii <mlpalii@gmail.com>',
    VERSION_FROM        => 'lib/App/ShellPack.pm',
    ABSTRACT_FROM       => 'lib/App/ShellPack.pm',
    ($ExtUtils::MakeMaker::VERSION >= 6.3002
      ? (LICENSE => 'perl')
      : ()),
    EXE_FILES           => ['script/shellpack'],
    PL_FILES            => {},
    PREREQ_PM => {
        'Test::More' => 0,
    },
    dist                => { COMPRESS => 'gzip -9f', SUFFIX => 'gz', },
    clean               => { FILES => 'App-ShellPack-*' },
);
