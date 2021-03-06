#!/usr/bin/perl

use warnings;
use strict;

my $repo;
die "Forking failed" unless defined($repo = fork);

my $out_file = $repo ? 'build.xml' : 'src/repo-ant/build.xml';
my $project = 'merapi';
my $version = "\${$project.version}";

sub module_root { return "src/$project-$_[0]"; }
sub module_bin { return "build/bin/$project-$_[0]"; }
sub module_res { return module_root($_[0])."/res"; }
sub module_src { return module_root($_[0])."/java"; }

sub module_deps { return @{$_[0]->{deps} || []}; }

my @src_includes = qw(.classpath .project .settings/** COPYING NEWS README TODO lib/** src/**);
my @src_excludes = qw();

if($repo) {
	push @src_excludes, ('src/scripts/build.pl', 'src/repo-ant/**', 'lib/svnkit/**');
}
else {
	push @src_includes, ('build.xml', 'build.properties');
}

my @modules = (
	{
		name => 'util',
	},
	{
		name => 'event',
	},
	{
		name => 'iterators',
		deps => [qw(util)],
	},
	{
		name => 'misc',
		deps => [qw(iterators)]
	},
	{
		name => 'strings',
		deps => [qw(util iterators)],
	},
	{
		name => 'threads',
		deps => [qw(util)],
	},
	{
		name => 'io',
		deps => [qw(threads), qw(util)],
	},
	{
		name => 'math',
		deps => [qw(iterators), qw(util)],
	},
	{
		name => 'net',
		deps => [qw(event io threads), qw(util)],
	},
	{
		name => 'www',
		deps => [qw(io), qw(threads util)],
	},
	{
		name => 'spoj',
		deps => [qw(www), qw(net event io threads util)],
	},
	{
		name => 'irc',
		deps => [qw(event iterators strings net io), qw(util threads)],
	},
	{
		name => 'json',
		deps => [qw(util iterators strings misc)],
	},
	{
		name => 'shorten',
		deps => [qw(strings misc json www), qw(util iterators io threads)],
	},
	{
		name => 'tinysong',
		deps => [qw(iterators strings misc json www), qw(util io threads)],
	},
	{
		name => 'chem',
		deps => [qw(strings), qw(util iterators)],
	},
);

open my $fh, ">$out_file" or die "Failed to open $out_file for writing";

my $xml = xml_obj('project', {name => $project, default => 'dist-bin'});
add_child($xml, xml_obj('description', undef, undef, "\n\tAnt build file for the Merapi project\n\n"));

my $src_fileset = {dir => '.', (@src_includes ? (includes => join(',', sort @src_includes)) : ()), (@src_excludes ? (excludes => join(',', sort @src_excludes)) : ()), prefix => "$project-$version-src"};

my $target;

if($repo) {
	add_child($xml, xml_obj('property', {name => 'project.libdir', value => 'lib'}));
	add_child($xml, xml_obj('import', {file => 'prmf-lib.xml'}));

	$target = xml_obj('target', {name => 'download-dependencies', description => 'downloads library files required by the project'}, [
		xml_obj('prmf-download-lib-svnkit'),
		xml_obj('prmf-download-lib-junit'),
	]);
	add_child($xml, $target);
	
	$target = xml_obj('target', {name => 'find-version', description => 'checks the revision number to determine project version', depends => 'download-dependencies', 'unless' => 'merapi.version'});
	add_child($target, mkdir_obj('build/repo-ant'));
	my $cp = join(':', map {"lib/svnkit/$_"} qw(antlr-runtime-3.1.3.jar jna.jar sqljet.1.0.2.jar svnkit-cli.jar svnkit.jar trilead.jar));
	add_child($target, xml_obj('javac', {srcdir => 'src/repo-ant', destdir => 'build/repo-ant', includeAntRuntime => 'false', target => '1.5', classpath => $cp}));
	add_child($target, xml_obj('java', {classpath => "$cp:build/repo-ant", output => 'build/repo-ant/build.properties', classname => 'Version', failonerror => 'true', 'fork' => 'true'}));
	add_child($target, xml_obj('property', {file => 'build/repo-ant/build.properties'}));
	add_child($target, xml_obj('echo', {level => 'info', message => "Project version: $version"}));
	add_child($xml, $target);
}
else {
	add_child($xml, xml_obj('property', {file => 'build.properties'}));
}

$target = xml_obj('target', {name => 'compile-project', description => 'compiles the project source', ($repo ? (depends => 'download-dependencies') : ())});
for(@modules) {
	my $module = $_->{name};
	my @deps = module_deps($_);
	add_child($target, mkdir_obj(module_bin($module)));
	my $javac = xml_obj('javac', {srcdir => module_src($module), destdir => module_bin($module), includeAntRuntime => 'false', target => '1.5'});
	add_child($javac, xml_obj('include', {name => '**/*.java'}));
	if(@deps) {
		my $cp = xml_obj('classpath');
		add_child($cp, $_) for map {xml_obj('pathelement', {path => module_bin($_)})} @deps;
		add_child($javac, $cp);
	}
	add_child($target, $javac);
}
add_child($xml, $target);

$target = xml_obj('target', {name => 'compile-tests', description => 'compiles project unit tests', depends => ($repo ? 'download-dependencies,' : '').'compile-project'}, [mkdir_obj('build/test-bin'), xml_obj('javac', {srcdir => 'src/test/java', destdir => 'build/test-bin', includeAntRuntime => 'false', target => '1.5'}, 
[xml_obj('classpath', undef, [map {xml_obj('pathelement', {path => $_})} ((map {module_bin($_->{name})} @modules), 'lib/junit/junit-4.8.2.jar')])])]);
add_child($xml, $target);

#$target = xml_obj('target', {name => 'test', description => 'compiles project unit tests', depends => ($repo ? 'download-dependencies,' : '').'compile-project'}, [mkdir_obj('build/test-bin'), xml_obj('javac', {srcdir => 'src/test/java', destdir => 'build/test-bin', includeAntRuntime => 'false', target => '1.5'}, 
#[xml_obj('classpath', undef, [map {xml_obj('pathelement', {path => $_})} ((map {module_bin($_->{name})} @modules), 'lib/junit/junit-4.8.2.jar')])])]);
#add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-bin', description => 'removes the compiled project source'}, [
	xml_obj('delete', {dir => 'build/bin'}),
]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-dist', description => 'removes the distribution directory'}, [
	xml_obj('delete', {dir => 'build/dist'}),
]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-all', description => 'removes all rebuildable files'}, [
	xml_obj('delete', {dir => 'build'}),
]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'package-source-tgz', description => 'creates a gzipped tarball source distribution', ($repo ? (depends => 'download-dependencies,find-version') : ())});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('tar', {destfile => "build/dist/$project-$version-src.tar.gz", compression => 'gzip', longfile => 'gnu'}, [
	xml_obj('tarfileset', $src_fileset),
	($repo ? (xml_obj('tarfileset', {fullpath => "$project-$version-src/build.xml", file => 'src/repo-ant/build.xml'})) : ()),
	($repo ? (xml_obj('tarfileset', {fullpath => "$project-$version-src/build.properties", file => 'build/repo-ant/build.properties'})) : ()),
]));

add_child($xml, $target);

$target = xml_obj('target', {name => 'package-source-tbz2', description => 'creates a bzip2-compressed tarball source distribution', ($repo ? (depends => 'download-dependencies,find-version') : ())});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('tar', {destfile => "build/dist/$project-$version-src.tar.bz2", compression => 'bzip2', longfile => 'gnu'}, [
	xml_obj('tarfileset', $src_fileset),
	($repo ? (xml_obj('tarfileset', {fullpath => "$project-$version-src/build.xml", file => 'src/repo-ant/build.xml'})) : ()),
	($repo ? (xml_obj('tarfileset', {fullpath => "$project-$version-src/build.properties", file => 'build/repo-ant/build.properties'})) : ()),
]));
add_child($xml, $target);

$target = xml_obj('target', {name => 'package-source-zip', description => 'creates a zipped source distribution', ($repo ? (depends => 'download-dependencies,find-version') : ())});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('zip', {destfile => "build/dist/$project-$version-src.zip"}, [
	xml_obj('zipfileset', $src_fileset),
	($repo ? (xml_obj('zipfileset', {fullpath => "$project-$version-src/build.xml", file => 'src/repo-ant/build.xml'})) : ()),
	($repo ? (xml_obj('zipfileset', {fullpath => "$project-$version-src/build.properties", file => 'build/repo-ant/build.properties'})) : ()),
]));
add_child($xml, $target);

$target = xml_obj('target', {name => 'package-source-all', description => 'creates all the source distribution bundles', depends => 'package-source-tgz,package-source-tbz2,package-source-zip'});
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-bin', description => 'creates binary distribution files', depends => ($repo ? 'find-version,' : '').'compile-project'});
add_child($target, mkdir_obj('build/dist'));
for(@modules) {
	my $module = $_->{name};
	my $jar = jar_obj("build/dist/$project-$module-$version.jar", "$project-$module", $version);
	add_child($jar, xml_obj('fileset', {dir => module_bin($module), includes => '**/*.class'}));
	add_child($jar, xml_obj('fileset', {dir => module_res($module)})) if -d module_res($module);
	add_child($target, $jar);
}
my $jar = jar_obj("build/dist/$project-$version.jar", $project, $version);
for(@modules) {
	my $module = $_->{name};
	add_child($jar, xml_obj('zipfileset', {src => "build/dist/$project-$module-$version.jar", excludes => 'META-INF/**'}));
}
add_child($target, $jar);
add_child($xml, $target);

$target = xml_obj('target', {name => 'generate-javadoc', description => 'generates project documentation', ($repo ? (depends => 'find-version') : ())}, [xml_obj('javadoc', {
	access => 'package',
	author => 'true',
	destdir => 'build/doc',
	doctitle => "Documentation for the Merapi project, version $version",
	nodeprecated => 'false',
	nodeprecatedlist => 'false',
	noindex => 'false',
	nonavbar => 'false',
	notree => 'false',
	overview => 'src/package-info/overview.html',
	packagenames => 'com.googlecode.prmf.merapi.*',
	source => '1.6',
	splitindex => 'true',
	'use' => 'true',
	version => 'false',
}, [
	xml_obj('sourcepath', undef, [map {xml_obj('pathelement', {path => $_})} ((map {module_src($_->{name})} @modules), 'src/package-info')]),
	xml_obj('link', {href => 'http://java.sun.com/javase/6/docs/api/'}),
])]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'package-javadoc', description => 'creates a zip archive of the project documentation', depends => 'generate-javadoc'});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('zip', {destfile => "build/dist/$project-$version-doc.zip"}, [
	xml_obj('zipfileset', {dir => 'build/doc', prefix => "$project-$version-doc"}),
]));
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist', description => 'creates all distribution files', depends => 'package-source-all,dist-bin,package-javadoc'});
add_child($xml, $target);

print_xml($fh, $xml);

close $fh;

sub jar_obj {
	my($file, $title, $version) = @_;
	return xml_obj('jar', {destfile => $file}, [manifest_obj($title, $version)]);
}

sub manifest_obj {
	my($title, $version) = @_;
	return xml_obj('manifest', {}, [
		xml_obj('attribute', {name => 'Implementation-Title', value => $title}),
		xml_obj('attribute', {name => 'Implementation-Version', value => $version}),
	]);
}

sub mkdir_obj {
	my $dir = shift;
	return xml_obj('mkdir', {dir => $dir})
}

sub xml_obj {
	return {
		tag => shift,
		attributes => shift || {},
		children => shift || [],
		text => shift || undef,
	};
}

sub add_attribute {
	my($obj, $name, $value) = @_;
	$obj->{attributes}->{$name} = $value;
}

sub add_child {
	my($obj, $child) = @_;
	push @{$obj->{children}}, $child;
}

sub set_text {
	my($obj, $text) = @_;
	$obj->{text} = $text;
}

sub print_xml {
	my($fh, $obj, $tabc) = (@_, 0);
	my $tag = $obj->{tag};
	my %attributes = %{$obj->{attributes}};
	my @children = @{$obj->{children}};
	my $text = $obj->{text};
	my $tabs = "\t" x $tabc;
	print $fh qq{<?xml version="1.0" encoding="UTF-8"?>\n} unless $tabc;
	printf($fh "%s<%s", $tabs, $tag);
	printf($fh " %s=\"%s\"", $_, $attributes{$_}) for keys %attributes;
	if(@children || defined $text) {
		print $fh ">";
		print $fh defined $text ? $text : "\n";
		print_xml($fh, $_, $tabc + 1) for @children;
		print $fh $tabs if @children;
		printf($fh "</%s>\n", $tag);
	}
	else {
		print $fh "/>\n";
	}
}

__DATA__
		<junit haltonerror="true" haltonfailure="true" printsummary="withOutAndErr">
			<classpath>
				<pathelement path="build/bin/merapi-util"/>
				<pathelement path="build/bin/merapi-event"/>
				<pathelement path="build/bin/merapi-iterators"/>
				<pathelement path="build/bin/merapi-misc"/>
				<pathelement path="build/bin/merapi-strings"/>
				<pathelement path="build/bin/merapi-threads"/>
				<pathelement path="build/bin/merapi-io"/>
				<pathelement path="build/bin/merapi-math"/>
				<pathelement path="build/bin/merapi-net"/>
				<pathelement path="build/bin/merapi-www"/>
				<pathelement path="build/bin/merapi-spoj"/>
				<pathelement path="build/bin/merapi-irc"/>
				<pathelement path="build/bin/merapi-json"/>
				<pathelement path="build/bin/merapi-shorten"/>
				<pathelement path="build/bin/merapi-tinysong"/>
				<pathelement path="build/bin/merapi-chem"/>
				<pathelement path="build/test-bin"/>
				<pathelement path="lib/junit/junit-4.8.2.jar"/>
			</classpath>
			<batchtest>
				<fileset dir="src/test/java">
					<include name="**/*Test*.java"/>
				</fileset>
			</batchtest>
		</junit>
