/*
 * merapi - Multi-purpose Java library
 * Copyright (C) 2009-2010 Miorel-Lucian Palii <mlpalii@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package com.googlecode.prmf.merapi.util;

import static com.googlecode.prmf.merapi.util.Iterators.chars;
import static com.googlecode.prmf.merapi.util.Iterators.iterator;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.prmf.merapi.dp.Iterator;

/**
 * A collection of string-related utilities.
 *
 * @author Miorel-Lucian Palii
 */
public class Strings {
	/**
	 * Maps character sequences to their corresponding literal pattern strings.
	 */
	public final static Mapper<CharSequence,String> PATTERN_QUOTE = new Mapper<CharSequence,String>() {
		@Override
		public String map(CharSequence charSeq) {
			return Pattern.quote(charSeq.toString());
		}
	};

	private static final Pattern NON_WHITESPACE_PATTERN = Pattern.compile("((\\S)(\\S*))");

//	private static final Map<String, String> XML_ESCAPE_MAP;
//	private static final Pattern XML_ESCAPE_PATTERN;
//	static {
//		XML_ESCAPE_MAP = new HashMap<String, String>();
//		XML_ESCAPE_MAP.put("<", "&lt;");
//		XML_ESCAPE_MAP.put(">", "&gt;");
//		XML_ESCAPE_MAP.put("'", "&apos;");
//		XML_ESCAPE_MAP.put("\"", "&quot;");
//		XML_ESCAPE_MAP.put("&", "&amp;");
//		XML_ESCAPE_PATTERN = Pattern.compile(regex(adapt(XML_ESCAPE_MAP.keySet().iterator())));
//	}

	/**
	 * There is no need to instantiate this class.
	 */
	private Strings() {
	}

//	/**
//	 * Prepares a regular expression string that will match any of the literal
//	 * sequences given by the iterator.
//	 *
//	 * @param charSeqs
//	 *            the character sequences to match
//	 * @return a regular expression matching all the given character sequences
//	 * @see #regex(CharSequence...)
//	 */
//	public static String regex(Iterator<? extends CharSequence> charSeqs) {
//		return String.format("(?:%s)", join("|", map(PATTERN_QUOTE, adapt(charSeqs))));
//	}
//
//	/**
//	 * <p>
//	 * Prepares a regular expression string that will match any of the literal
//	 * sequences given.
//	 * </p>
//	 * <p>
//	 * The match will be attempted in the order given, so for example
//	 * <code>regex("the", "the game")</code> will never match
//	 * <code>"the game"</code> because <code>"the"</code> will match first. For
//	 * the lazy, something like
//	 * <code>regex(reverse(sort("the", "the game"))</code> will do the trick.
//	 * (You'll need to
//	 * <code>import static {@linkplain com.googlecode.prmf.merapi.util.Iterators}.*</code>.)
//	 * </p>
//	 *
//	 * @param charSeqs
//	 *            the character sequences to match
//	 * @return a regular expression matching all the given character sequences
//	 * @see #regex(Iterator)
//	 */
//	public static String regex(CharSequence... charSeqs) {
//		return regex(iterator(charSeqs));
//	}

	/**
	 * <p>
	 * Changes the given text to lower case. That is, any upper case character
	 * in the text (e.g. a letter) will be replaced with its lower case
	 * counterpart.
	 * </p>
	 * <p>
	 * This method is primarily here for orthogonality, as the
	 * <code>String</code> class (likely the most commonly-used
	 * <code>CharSequence</code>) already has a {@link String#toLowerCase()
	 * toLowerCase()} method.
	 * </p>
	 *
	 * @param charSeq
	 *            the text to change
	 * @return a lower case copy of the text
	 * @see String#toLowerCase()
	 * @see #toTitleCase(CharSequence)
	 * @see #toUpperCase(CharSequence)
	 */
	public static String toLowerCase(CharSequence charSeq) {
		return charSeq.toString().toLowerCase();
	}

	/**
	 * <p>
	 * Changes the given text to upper case. That is, any lower case character
	 * in the text (e.g. a letter) will be replaced with its upper case
	 * counterpart.
	 * </p>
	 * <p>
	 * This method is primarily here for orthogonality, as the
	 * <code>String</code> class (likely the most commonly-used
	 * <code>CharSequence</code>) already has a {@link String#toUpperCase()
	 * toUpperCase()} method.
	 * </p>
	 *
	 * @param charSeq
	 *            the text to change
	 * @return an upper case copy of the text
	 * @see String#toUpperCase()
	 * @see #toLowerCase(CharSequence)
	 * @see #toTitleCase(CharSequence)
	 */
	public static String toUpperCase(CharSequence charSeq) {
		return charSeq.toString().toUpperCase();
	}

	/**
	 * <p>
	 * Changes the given text to title case. That is, the text will be changed
	 * to lower case, except for the first character of each word, which will be
	 * changed to upper case.
	 * </p>
	 * <p>
	 * Here, a word is not necessarily alphanumeric, but may be any sequence of
	 * non-whitespace characters.
	 * </p>
	 *
	 * @param charSeq
	 *            the text to change
	 * @return a title case copy of the text
	 * @see #toTitleCase(CharSequence, int)
	 * @see #toLowerCase(CharSequence)
	 * @see #toUpperCase(CharSequence)
	 */
	public static String toTitleCase(CharSequence charSeq) {
		return toTitleCase(charSeq, Integer.MAX_VALUE);
	}

	/**
	 * Changes the given text to title case, capitalizing words up to the
	 * specified limit. That is, this method behaves just like the
	 * single-argument version, except that any words beyond the limit will not
	 * be capitalized. They will still be changed to lower case, however.
	 *
	 * @param charSeq
	 *            the text to change
	 * @param limit
	 *            the maximum number of words to capitalize
	 * @return a title case copy of the text
	 * @see #toTitleCase(CharSequence)
	 */
	public static String toTitleCase(CharSequence charSeq, int limit) {
		if(limit < 0)
			throw new IllegalArgumentException("The limit may not be negative.");
		StringBuffer sb = new StringBuffer();
		Matcher m = NON_WHITESPACE_PATTERN.matcher(toLowerCase(charSeq));
		for(int allowedChanges = limit; allowedChanges > 0 && m.find(); --allowedChanges)
			m.appendReplacement(sb, m.group(1).toUpperCase() + m.group(2));
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Returns a string containing the given character repeated a specified
	 * number of times.
	 *
	 * @param character
	 *            the character to "multiply"
	 * @param count
	 *            the number of times to repeat the characters
	 * @return <code>character</code> repeated <code>count</code> times
	 * @see #multiply(CharSequence, int)
	 */
	public static String multiply(char character, int count) {
		return multiply(Character.toString(character), count);
	}

	/**
	 * Returns a string containing the given text repeated a specified number of
	 * times.
	 *
	 * @param charSeq
	 *            the text to "multiply"
	 * @param count
	 *            the number of times to repeat the text
	 * @return <code>charSeq</code> repeated <code>count</code> times
	 * @see #multiply(char, int)
	 */
	public static String multiply(CharSequence charSeq, int count) {
		if(count < 0)
			throw new IllegalArgumentException("Can't repeat a negative number of times.");
		StringBuilder sb = new StringBuilder(charSeq.length() * count);
		for(int repsLeft = count; repsLeft > 0; --repsLeft)
			sb.append(charSeq);
		return sb.toString();
	}

	/**
	 * Reverses the order of the characters in the given text.
	 *
	 * @param charSeq
	 *            the text to reverse
	 * @return a copy of the text with the order of the characters reversed
	 * @see StringBuilder#reverse()
	 */
	public static String reverse(CharSequence charSeq) {
		return new StringBuilder(charSeq).reverse().toString();
	}

	/**
	 * Joins the string representations of a group of objects into a single
	 * string, using the given separator.
	 *
	 * @param separator
	 *            the separator between adjacent joined objects
	 * @param objects
	 *            the objects to join
	 * @return a single string representing all the objects, joined by the
	 *         separator
	 * @see #join(char,Object...)
	 * @see #join(char,Iterator)
	 * @see #join(CharSequence,Object...)
	 */
	public static String join(CharSequence separator, Iterator<?> objects) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(objects.reset(); !objects.isDone(); objects.advance()) {
			if(!first)
				sb.append(separator);
			else
				first = false;
			sb.append(objects.current());
		}
		return sb.toString();
	}

	/**
	 * Joins the string representations of a group of objects into a single
	 * string, using the given separator.
	 *
	 * @param separator
	 *            the separator between adjacent joined objects
	 * @param objects
	 *            the objects to join
	 * @return a single string representing all the objects, joined by the
	 *         separator
	 * @see #join(char,Object...)
	 * @see #join(CharSequence,Object...)
	 * @see #join(CharSequence,Iterator)
	 */
	public static String join(char separator, Iterator<?> objects) {
		return join(Character.toString(separator), objects);
	}

	/**
	 * Joins the string representations of a group of objects into a single
	 * string, using the given separator.
	 *
	 * @param separator
	 *            the separator between adjacent joined objects
	 * @param objects
	 *            the objects to join
	 * @return a single string representing all the objects, joined by the
	 *         separator
	 * @see #join(char,Object...)
	 * @see #join(char,Iterator)
	 * @see #join(CharSequence,Iterator)
	 */
	public static String join(CharSequence separator, Object... objects) {
		return join(separator, iterator(objects));
	}

	/**
	 * Joins the string representations of a group of objects into a single
	 * string, using the given separator.
	 *
	 * @param separator
	 *            the separator between adjacent joined objects
	 * @param objects
	 *            the objects to join
	 * @return a single string representing all the objects, joined by the
	 *         separator
	 * @see #join(char,Iterator)
	 * @see #join(CharSequence,Object...)
	 * @see #join(CharSequence,Iterator)
	 */
	public static String join(char separator, Object... objects) {
		return join(Character.toString(separator), objects);
	}

	/**
	 * <p>
	 * Checks if the given text is a single line. A piece of text is a single
	 * line if it contains no line separator characters.
	 * <p>
	 * </p>
	 * Note that by this definition, a piece of text containing a single line
	 * separator and at the last position will be flagged as multi-line. If this
	 * is undesired, trim the text before passing it to this method.</p>
	 *
	 * @param charSeq
	 *            the text to check
	 * @return whether the text contains any line separator characters
	 */
	public static boolean isSingleLine(CharSequence charSeq) {
		boolean ret = true;
		for(Character c: chars(charSeq))
			if(Character.getType(c.charValue()) == Character.LINE_SEPARATOR) {
				ret = false;
				break;
			}
		return ret;
	}

//	/**
//	 * Escapes any strings in the given text that have a special meaning in XML.
//	 * For example, the quote character (&quot;) is replaced with
//	 * <code>&amp;quot;</code>, and the ampersand (&amp;) is replaced with
//	 * <code>&amp;amp;</code>.
//	 *
//	 * @param charSeq
//	 *            the text to escape
//	 * @return a copy of the text with any special strings escaped
//	 */
//	public static String escapeXml(CharSequence charSeq) {
//		StringBuffer sb = new StringBuffer();
//		Matcher m = XML_ESCAPE_PATTERN.matcher(charSeq);
//		while(m.find())
//			m.appendReplacement(sb, XML_ESCAPE_MAP.get(m.group()));
//		m.appendTail(sb);
//		return sb.toString();
//	}

	/**
	 * Translates a string into <code>application/x-www-form-urlencoded</code>
	 * format using the UTF-8 encoding scheme.
	 *
	 * @param charSeq
	 *            the text to translate
	 * @return a translated string
	 */
	public static String encodeUtf8(CharSequence charSeq) {
		try {
			return URLEncoder.encode(charSeq.toString(), "UTF-8");
		}
		catch(UnsupportedEncodingException e) {
			throw new Error("The UTF-8 encoding is not supported!", e);
		}
	}

	/**
	 * <p>
	 * Convenience method for building URL objects from character sequences that
	 * doesn't throw a <code>MalformedURLException</code>.
	 * </p>
	 * <p>
	 * If the URL is guaranteed to be valid, calling this method is terser than
	 * wrapping in a try-catch block. Avoid this method if you don't have that
	 * guarantee.
	 * </p>
	 *
	 * @param charSeq
	 *            the character sequence representation of the URL
	 * @return an object representation of the URL
	 */
	public static URL toUrl(CharSequence charSeq) {
		try {
			return new URL(charSeq.toString());
		}
		catch(MalformedURLException e) {
			throw new Error("A verified URL generated a MalformedURLException.", e);
		}
	}
}
