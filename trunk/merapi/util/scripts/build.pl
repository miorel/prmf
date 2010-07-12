#!/usr/bin/perl

use warnings;
use strict;

my $out_file = 'build.xml';
my $project = 'merapi';
my $version = '0.0.1';

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
add_child($xml, xml_obj('description', undef, undef, 'Ant build file for the Merapi project'));

my $target;

$target = xml_obj('target', {name => 'bin', description => 'compiles the project source'});
for(@modules) {
	my $module = $_->{name};
	my @deps = @{$_->{deps} || []};
	add_child($target, xml_obj('mkdir', {dir => "modules/$module/bin"}));
	my $javac = xml_obj('javac', {srcdir => "modules/$module/src", destdir => "modules/$module/bin", includeAntRuntime => 'no'});
	add_child($javac, xml_obj('include', {name => '**/*.java'}));
	add_child($javac, xml_obj('exclude', {name => '**/package-info.java'}));
	if(@deps) {
		my $cp = xml_obj('classpath');
		add_child($cp, $_) for map {xml_obj('pathelement', {path => "modules/$_/bin"})} @deps;
		add_child($javac, $cp);
	}
	add_child($target, $javac);
}
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-bin', description => 'removes the compiled project source'});
for(@modules) {
	my $module = $_->{name};
	add_child($target, xml_obj('delete', {dir => "modules/$module/bin"}));
}
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean-dist', description => 'removes the distribution directory'}, [
	xml_obj('delete', {dir => 'dist'}),
]);
add_child($xml, $target);

$target = xml_obj('target', {name => 'clean', description => 'removes all rebuildable files', depends => 'clean-bin,clean-dist'});
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist-bin', description => 'creates binary distribution files', depends => 'bin'});
add_child($target, xml_obj('mkdir', {dir => 'dist'}));
for(@modules) {
	my $module = $_->{name};
	my $jar = xml_obj('jar', {destfile => "dist/$project-$module-$version.jar"});
	add_child($jar, xml_obj('fileset', {dir => "modules/$module/bin", includes => '**/*.class'}));
	add_child($jar, xml_obj('fileset', {dir => "modules/$module/res"})) if -d "modules/$module/res";
	add_child($jar, manifest("$project-$module", $version));
	add_child($target, $jar);
}
my $jar = xml_obj('jar', {destfile => "dist/$project-$version.jar"}, [manifest($project, $version)]);
for(@modules) {
	my $module = $_->{name};
	add_child($jar, xml_obj('zipfileset', {src => "dist/$project-$module-$version.jar", excludes => 'META-INF/**'}));
}
add_child($target, $jar);
add_child($xml, $target);

$target = xml_obj('target', {name => 'dist', description => 'creates all distribution files', depends => 'dist-bin'});
add_child($xml, $target);

print_xml($fh, $xml);

close $fh;

sub manifest {
	my($title, $version) = @_;
	return xml_obj('manifest', {}, [
		xml_obj('attribute', {name => 'Implementation-Title', value => $title}),
		xml_obj('attribute', {name => 'Implementation-Version', value => $version}),
	]);
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
	
	<fileset id="merapi.src.fileset" dir="${basedir}" excludes="${merapi.bin.dir}/**,${merapi.dist.dir}/**,${merapi.doc.dir}/**"/>
	
	<target name="clean-doc" description="removes the documentation directory">
		<delete dir="${merapi.doc.dir}"/>
	</target>
	
	<target name="clean" description="removes all rebuildable files"
		depends="clean-bin,clean-doc,clean-dist"/>
	
	<target name="dist-src-tgz" description="creates a gzipped tarball source distribution">
		<mkdir dir="${merapi.dist.dir}"/>
		<tar destfile="${merapi.dist.dir}/${merapi.dist.name}-src.tar.gz" compression="gzip">
			<tarfileset refid="merapi.src.fileset" prefix="${merapi.dist.name}"/>
		</tar>
	</target>

	<target name="dist-src-tbz2" description="creates a bzip2-compressed tarball source distribution">
		<mkdir dir="${merapi.dist.dir}"/>
		<tar destfile="${merapi.dist.dir}/${merapi.dist.name}-src.tar.bz2" compression="bzip2">
			<tarfileset refid="merapi.src.fileset" prefix="${merapi.dist.name}"/>
		</tar>
	</target>
	
	<target name="dist-src-zip" description="creates a zipped source distribution">	
		<mkdir dir="${merapi.dist.dir}"/>
		<zip destfile="${merapi.dist.dir}/${merapi.dist.name}-src.zip">
			<zipfileset refid="merapi.src.fileset" prefix="merapi.dist.name}"/>
		</zip>
	</target>
	
	<target name="dist-src" description="creates all the source distribution bundles"
		depends="dist-src-tgz,dist-src-tbz2,dist-src-zip"/> 
	
	<target name="doc" description="generates project documentation">
		<javadoc
			access="package"
			author="true"
			destdir="doc"
			doctitle="Documentation for the merapi project, version ${merapi.version}"
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
