#!/usr/bin/perl

use warnings;
use strict;

my $out_file = 'build.xml';
my @modules = qw(core iterators unsorted);

open my $fh, ">$out_file" or die "Failed to open $out_file for writing";

print $fh <<"HEADER";
<?xml version="1.0" encoding="UTF-8"?>
<!--
	Don't edit this file, edit the script that generates it: $0
-->
<project name="merapi" default="bin">
	<description>Ant build file for the Merapi project</description>
HEADER

print $fh <<"TARGET-BEGIN";
	<target name="bin" description="compiles the project">
TARGET-BEGIN
for my $i (0..$#modules) {
	my $module = $modules[$i];
	print $fh <<"TARGET";
		<mkdir dir="modules/$module/bin"/>
		<javac destdir="modules/$module/bin" includeAntRuntime="no">
			<include name="**/*.java"/>
			<exclude name="**/package-info.java"/>
			<src>
				<pathelement path="modules/$module/src"/>
			</src>
TARGET
	if($i > 0) {
		print $fh <<"TARGET";
			<classpath>
TARGET
print $fh <<"TARGET" for @modules[0..$i-1];
				<pathelement path="modules/$_/bin"/>
TARGET
		print $fh <<"TARGET";
			</classpath>
TARGET
	}
	print $fh <<"TARGET";		
		</javac>
TARGET
}
print $fh <<"TARGET-END";
	</target>
TARGET-END

print $fh <<"TARGET-BEGIN";
	<target name="clean" description="removes the compiled project directory">
TARGET-BEGIN
print $fh <<"TARGET" for @modules;
		<delete dir="modules/$_/bin"/>
TARGET
print $fh <<"TARGET-END";
	</target>
TARGET-END

print $fh <<"FOOTER";
</project>
FOOTER

close $fh;

__DATA__
<?xml version="1.0" encoding="UTF-8"?>
<project name="merapi"> <!--default="dist"-->
	<description>Ant build file for the Merapi project</description>
	
<!--	<property file="merapi.properties"/>
	
	<fileset id="merapi.src.fileset" dir="${basedir}" excludes="${merapi.bin.dir}/**,${merapi.dist.dir}/**,${merapi.doc.dir}/**"/>
	
	<target name="clean-bin" description="removes the compiled project directory">
		<delete dir="${merapi.bin.dir}"/>
	</target>
	
	<target name="clean-dist" description="removes the distribution directory">
		<delete dir="${merapi.dist.dir}"/>
	</target>

	<target name="clean-doc" description="removes the documentation directory">
		<delete dir="${merapi.doc.dir}"/>
	</target>
	
	<target name="clean" description="removes all rebuildable files"
		depends="clean-bin,clean-doc,clean-dist"/>
	
	<target name="bin" description="compiles the project">
		<mkdir dir="${merapi.bin.dir}"/>
		<javac destdir="${merapi.bin.dir}" includeAntRuntime="no">
			<include name="**/*.java"/>
			<exclude name="**/package-info.java"/>
			<src>
				<pathelement path="${merapi.src.dir}"/>
			</src>
		</javac>
	</target>	
	
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
	
	<target name="dist-bin" description="creates a binary distribution" depends="bin">
		<mkdir dir="${merapi.dist.dir}"/>
		<jar destfile="${merapi.dist.dir}/${merapi.dist.name}.jar">
			<manifest>
				<attribute name="Implementation-Title" value="merapi"/>
				<attribute name="Implementation-Version" value="${merapi.version}"/>
			</manifest>
			<fileset dir="${merapi.bin.dir}" includes="**/*.class"/>
			<fileset dir="${merapi.res.dir}"/>
		</jar>
	</target>
	
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
