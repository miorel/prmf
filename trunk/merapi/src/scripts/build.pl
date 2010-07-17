#!/usr/bin/perl

use warnings;
use strict;

my $out_file = 'build.xml';
my $project = 'merapi';
my $version = '0.0.1';

sub module_root { return "src/$project-$_[0]"; }
sub module_bin { return "build/bin/$project-$_[0]"; }
sub module_res { return module_root($_[0])."/res"; }
sub module_src { return module_root($_[0])."/java"; }

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
		name => 'threads',
		deps => [qw(util)],
	},
	{
		name => 'math',
		deps => [qw(iterators)],
	},
	{
		name => 'unsorted',
		deps => [qw(util event iterators threads)],
	},
	{
		name => 'chem',
		deps => [qw(unsorted)],
	},
);

open my $fh, ">$out_file" or die "Failed to open $out_file for writing";

my $xml = xml_obj('project', {name => $project, default => 'dist-bin'});
add_child($xml, xml_obj('description', undef, undef, "\n\tAnt build file for the Merapi project\n\n"));

my $src_fileset = {dir => '${basedir}', excludes => 'build/**', prefix => "$project-$version-src"};

my $target;

$target = xml_obj('target', {name => 'compile-project', description => 'compiles the project source'});
for(@modules) {
	my $module = $_->{name};
	my @deps = @{$_->{deps} || []};
	add_child($target, mkdir_obj(module_bin($module)));
	my $javac = xml_obj('javac', {srcdir => module_src($module), destdir => module_bin($module), includeAntRuntime => 'no', target => '1.5'});
	add_child($javac, xml_obj('include', {name => '**/*.java'}));
	if(@deps) {
		my $cp = xml_obj('classpath');
		add_child($cp, $_) for map {xml_obj('pathelement', {path => module_bin($_)})} @deps;
		add_child($javac, $cp);
	}
	add_child($target, $javac);
}
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-bin', description => 'removes the compiled project source'}, [
	xml_obj('delete', {dir => 'build/bin'}),
]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-dist', description => 'removes the distribution directory'}, [
	xml_obj('delete', {dir => 'build/dist'}),
]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-all', description => 'removes all rebuildable files'});
	xml_obj('delete', {dir => 'build'}),
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-src-tgz', description => 'creates a gzipped tarball source distribution'});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('tar', {destfile => "build/dist/$project-$version-src.tar.gz", compression => 'gzip', longfile => 'gnu'}, [xml_obj('tarfileset', $src_fileset)]));
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-src-tbz2', description => 'creates a bzip2-compressed tarball source distribution'});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('tar', {destfile => "build/dist/$project-$version-src.tar.bz2", compression => 'bzip2', longfile => 'gnu'}, [xml_obj('tarfileset', $src_fileset)]));
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-src-zip', description => 'creates a zipped source distribution'});
add_child($target, mkdir_obj('build/dist'));
add_child($target, xml_obj('zip', {destfile => "build/dist/$project-$version-src.zip"}, [xml_obj('zipfileset', $src_fileset)]));
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-src', description => 'creates all the source distribution bundles', depends => 'dist-src-tgz,dist-src-tbz2,dist-src-zip'});
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-bin', description => 'creates binary distribution files', depends => 'compile-project'});
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

$target = xml_obj('target', {name => 'dist', description => 'creates all distribution files', depends => 'dist-src,dist-bin'});
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
<project name="merapi"> <!--default="dist"-->
<!--	<property file="merapi.properties"/>
	
	<target name="clean-doc" description="removes the documentation directory">
		<delete dir="${merapi.doc.dir}"/>
	</target>
	
	<target name="doc" description="generates project documentation">
		<javadoc
			access="package"
			author="true"
			destdir="doc"
			doctitle="Documentation for the Merapi project, version ${merapi.version}"
			nodeprecated="false"
			nodeprecatedlist="false"
			noindex="false"
			nonavbar="false"
			notree="false"
			overview="${merapi.src.dir}/overview.html"
			packagenames="com.googlecode.prmf.merapi.*"
			source="1.6"
			sourcepath="${merapi.src.dir}"
			splitindex="true"
			use="true"
			version="false">
			<link href="http://java.sun.com/javase/6/docs/api/"/>
		</javadoc>
		--><!-- :${merapi.lib.dir}/openjdk/jdk/src/share/classes --><!--
	</target>
	
	<target name="dist-doc" description="creates a zip archive of the project documentation" depends="doc">	
		<mkdir dir="${merapi.dist.dir}"/>
		<zip destfile="${merapi.dist.dir}/${merapi.dist.name}-doc.zip">
			<zipfileset dir="${merapi.doc.dir}" prefix="${merapi.dist.name}-doc"/>
		</zip>
	</target>
	
	<target name="dist" description="creates all distribution files"
		depends="dist-src,dist-doc,dist-bin"/>-->
</project>
