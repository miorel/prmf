/*
 * Copyright (C) 2010 Miorel-Lucian Palii This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */
package com.googlecode.prmf.merapi.net.www.spoj;

/**
 * Possible submission languages on the Sphere Online Judge (SPOJ).
 *
 * @author Miorel-Lucian Palii
 * @see <a href="http://www.spoj.pl/">Sphere Online Judge (SPOJ)</a>
 */
public enum Language {
	/**
	 * Ada, a statically-typed multi-paradigm language based on Pascal.
	 */
	ADA("Ada"),

	/**
	 * Assembly language for the x86 family of processors.
	 */
	ASSEMBLY("ASM", "x86 assembly language"),

	/**
	 * Bourne-again shell (Bash) scripting language.
	 *
	 * @see <a href="http://www.gnu.org/software/bash/">Bash - GNU Project</a>
	 */
	BASH("BAS", "Bash"),

	/**
	 * brainfuck, an esoteric, minimalist programming language.
	 *
	 * @see <a href="http://www.muppetlabs.com/~breadbox/bf/">The Brainfuck
	 *      Programming Language</a>
	 */
	BRAINFUCK("BF", "brainfuck"),

	/**
	 * C, an imperative language often used for systems programming.
	 */
	C,

	/**
	 * C#, a multi-paradigm programming language originally developed by
	 * Microsoft.
	 */
	C_SHARP("C#", "C#"),

	/**
	 * C++, a statically typed multi-paradigm compiled programming language.
	 */
	C_PLUS_PLUS("C++", "C++"),

	/**
	 * The C99 dialect of the C programming language.
	 */
	C99,

	/**
	 * Objective Caml (OCaml), the main implementation of the functional
	 * language Caml, which adds object-oriented features.
	 *
	 * @see <a href="http://caml.inria.fr/">The Caml Language</a>
	 */
	OCAML("CAM", "OCaml"),

	/**
	 * CLIPS, a language for writing expert systems.
	 *
	 * @see <a href="http://clipsrules.sourceforge.net/">CLIPS on
	 *      SourceForge</a>
	 */
	CLIPS("CLP", "Clips"),

	/**
	 * D, an object-oriented system programming language developed to be an
	 * alternative to C++.
	 *
	 * @see <a href="http://www.digitalmars.com/d/">D Programming Language</a>
	 */
	D,

	/**
	 * Erlang, a concurrent/functional programming language that uses the actor
	 * model.
	 *
	 * @see <a href="http://www.erlang.org/">Erlang Programming Language,
	 *      Official Website</a>
	 */
	ERLANG("ERL", "Erlang"),

	/**
	 * Fortran, a procedural imperative programming language particularly
	 * well-suited for scientific computing.
	 */
	FORTRAN("FOR", "Fortran"),

	/**
	 * Haskell, a general purpose purely functional programming language.
	 *
	 * @see <a href="http://haskell.org/">Haskell - HaskellWiki</a>
	 */
	HASKELL("HAS", "Haskell"),

	/**
	 * INTERCAL, an esoteric programming language intended to be as different as
	 * possible from all other languages.
	 *
	 * @see <a href="http://www.catb.org/~esr/intercal/">The INTERCAL Resources
	 *      Page</a>
	 */
	INTERCAL("ICK", "INTERCAL"),

	/**
	 * @see <a href="http://www.cs.arizona.edu/icon/">The Icon Programming
	 *      Language</a>
	 */
	ICON("ICO", "Icon"),

	/**
	 * A packaged archive of Java bytecode.
	 */
	JAR("jar format"),

	/**
	 * Java, an object-oriented language that runs in a virtual machine (the
	 * JVM).
	 */
	JAVA("JAV", "Java"),

	/**
	 * JavaScript, an object-oriented scripting language primarily used
	 * client-side in web applications.
	 */
	JAVASCRIPT("JS", "JavaScript"),

	/**
	 * Common Lisp, the primary dialect of Lisp, a functional language with an
	 * (in)famous parenthesized syntax.
	 */
	LISP("LIS", "Common Lisp"),

	/**
	 * Lua, a reflective imperative/functional programming language.
	 *
	 * @see <a href="http://www.lua.org/">The Programming Language Lua</a>
	 */
	LUA("Lua"),

	/**
	 * A high-level statically-typed language for the .NET platform that mixes
	 * an object-oriented program structure with functional features.
	 *
	 * @see <a href="http://nemerle.org/">Nemerle Homepage</a>
	 */
	NEMERLE("NEM", "Nemerle"),

	/**
	 * Nice, a feature-rich object-oriented language for the Java Virtual
	 * Machine which makes compile-time safety a priority.
	 *
	 * @see <a href="http://nice.sourceforge.net/">The Nice Programming
	 *      language</a>
	 */
	NICE("NIC", "Nice"),

	/**
	 * Pascal, an imperative procedural programming language.
	 */
	PASCAL("PAS", "Pascal"),

	/**
	 * Larry Wall's Practical Extraction and Report Language (Perl), a
	 * high-level, dynamic, interpreted language.
	 *
	 * @see <a href="http://www.perl.org/">The Perl Programming Language</a>
	 */
	PERL("PER", "Perl"),

	/**
	 * PHP, a scripting language originally designed for web development.
	 *
	 * @see <a href="http://php.net/">PHP: Hypertext Preprocessor</a>
	 */
	PHP,

	/**
	 * Pike, a multi-paradigm, high-level interpreted language that features
	 * both static and dynamic typing.
	 *
	 * @see <a href="http://pike.ida.liu.se/">pike.ida.liu.se</a>
	 */
	PIKE("PIK", "Pike"),

	/**
	 * Prolog, a declarative logic programming language associated with
	 * artificial intelligence.
	 */
	PROLOG("PRL", "Prolog"),

	/**
	 * Python, a multi-paradigm, dynamic, high-level programming language.
	 *
	 * @see <a href="http://www.python.org/">Python Programming Language</a>
	 */
	PYTHON("PYT", "Python"),

	/**
	 * Ruby, a dynamic, reflective, object-oriented language inspired by Perl
	 * and Smalltalk.
	 *
	 * @see <a href="http://ruby-lang.org/">Ruby Programming Language</a>
	 */
	RUBY("RUB", "Ruby"),

	/**
	 * Scala, a functional and object-oriented language for the Java Virtual
	 * Machine.
	 *
	 * @see <a href="http://www.scala-lang.org/">The Scala Programming
	 *      Language</a>
	 */
	SCALA("SCA", "Scala"),

	/**
	 * Scheme, the "other" Lisp dialect, a functional programming language with
	 * a minimalist, extensible design.
	 */
	SCHEME("SCM", "Scheme"),

	/**
	 * Smalltalk, a dynamically-typed object-oriented language.
	 *
	 * @see <a href="http://www.smalltalk.org/">Smalltalk.org (TM)</a>
	 */
	SMALLTALK("ST", "Smalltalk"),

	/**
	 * Tcl, a multi-paradigm scripting language.
	 *
	 * @see <a href="http://www.tcl.tk/">Tcl Developer Site</a>
	 */
	TCL("Tcl"),

	// TECS("TECS"),

	/**
	 * What you see is what you get plain text.
	 */
	PLAIN_TEXT("TEX", "plain text"),

	/**
	 * Whitespace, an esoteric programming language whose syntax consists
	 * entirely of spaces, tabs, and newlines.
	 *
	 * @see <a href="http://compsoc.dur.ac.uk/whitespace/">Whitespace
	 *      homepage</a>
	 */
	WHITESPACE("WSP", "Whitespace");

	private final String spojName;
	private final String realName;

	private Language() {
		this.spojName = toString();
		this.realName = toString();
	}

	private Language(String realName) {
		this.spojName = toString();
		this.realName = realName;
	}

	private Language(String spojName, String realName) {
		this.spojName = spojName;
		this.realName = realName;
	}

	/**
	 * Returns the "real" (common) name of this language, e.g.&nbsp;
	 * <code>"Common Lisp"</code>.
	 *
	 * @return the real name
	 */
	public String getRealName() {
		return this.realName;
	}

	/**
	 * Returns the name used on SPOJ for this language, typically an
	 * abbreviation of the real name, e.g.&nbsp;<code>"PYT"</code> for Python.
	 *
	 * @return the SPOJ name
	 */
	public String getSpojName() {
		return this.spojName;
	}

	/**
	 * Obtains the language corresponding to the given SPOJ name.
	 *
	 * @param spojName
	 *            the SPOJ name of the language
	 * @return a result object (or <code>null</code> if there isn't one with the
	 *         specified name)
	 */
	public static Language forSpojName(String spojName) {
		Language ret = null;
		for(Language lang : Language.values())
			if(lang.spojName.equals(spojName)) {
				ret = lang;
				break;
			}
		return ret;
	}
}
