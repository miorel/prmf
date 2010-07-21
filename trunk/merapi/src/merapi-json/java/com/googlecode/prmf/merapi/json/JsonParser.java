/*
 * Merapi - Multi-purpose Java library
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
package com.googlecode.prmf.merapi.json;

import java.math.BigDecimal;
import java.util.Locale;

import com.googlecode.prmf.merapi.util.Pair;
import com.googlecode.prmf.merapi.util.Triple;

/**
 * Parses text in JavaScript Object Notation (JSON) format as Java objects.
 *
 * @author Miorel-Lucian Palii
 */
public class JsonParser {
	/**
	 * Constructs a new JSON parser.
	 */
	public JsonParser() {
	}

	/**
	 * Parses text in JSON format.
	 *
	 * @param json
	 *            text in JSON format
	 * @return a Java object representation of the input
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	public JsonValue parse(CharSequence json) throws JsonException {
		Pair<? extends JsonValue,Integer> parseResult = parseValue(json, 0);

		// Make sure anything left over is whitespace.
		int parseLen = skipWhitespace(json, parseResult.getSecond().intValue());
		if(parseLen != json.length())
			throw new RuntimeException("The input is not a single JSON value!");

		return parseResult.getFirst();
	}

	private abstract class JsonListValueParser<T extends JsonValue> {
		private final char opening;
		private final char separator;
		private final char closing;

		public JsonListValueParser(char opening, char separator, char closing) {
			this.opening = opening;
			this.separator = separator;
			this.closing = closing;
		}

		protected abstract T getValue();

		protected abstract int readElement(CharSequence json, int start) throws JsonException;

		public Pair<T,Integer> parseList(CharSequence json, int start) throws JsonException {
			int end = start;

			for(boolean startedReading = false;;) {
				end = skipWhitespace(json, end);

				if(!startedReading) { // Expect an opening character;
					assertChar(json, end++, this.opening, "Couldn't find opening list character.");
					end = skipWhitespace(json, end);
					startedReading = true;
				}

				if(!(end < json.length()))
					throw new RuntimeException("Reached end of input while parsing.");

				char c = json.charAt(end);
				if(c == this.closing) { // Found closing character, stop reading.
					++end;
					break;
				}
				else if(c == this.separator) { // Found separator, keep reading elements.
					// Incidentally, this means we're tolerating consecutive separator characters,
					// e.g. "[,,,]" will be successfully parsed as an empty array.
					++end;
					continue;
				}
				else { // Expecting an element! Don't disappoint me.
					end = readElement(json, end);
					continue;
				}
			}

			return new Pair<T,Integer>(getValue(), Integer.valueOf(end - start));
		}
	}

	private Pair<JsonObject,Integer> parseObject(CharSequence json, int start) throws JsonException {
		final JsonObject object = new JsonObject();
		return new JsonListValueParser<JsonObject>('{', ',', '}') {
			@Override
			protected JsonObject getValue() {
				return object;
			}

			@Override // The different names are to distinguish the variables.
			protected int readElement(CharSequence jsonInner, int startInner) throws JsonException {
				Triple<String,? extends JsonValue,Integer> parseResult = parsePair(jsonInner, startInner);
				object.put(parseResult.getFirst(), parseResult.getSecond());
				return startInner + parseResult.getThird().intValue();
			}
		}.parseList(json, start);
	}

	/**
	 * Parses a JSON array starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a JSON array and the number of characters parsed, as a pair
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Pair<JsonArray,Integer> parseArray(CharSequence json, int start) throws JsonException {
		final JsonArray array = new JsonArray();
		return new JsonListValueParser<JsonArray>('[', ',', ']') {
			@Override
			protected JsonArray getValue() {
				return array;
			}

			@Override // The different names are to distinguish the variables.
			protected int readElement(CharSequence jsonInner, int startInner) throws JsonException {
				Pair<? extends JsonValue,Integer> parseResult = parseValue(jsonInner, startInner);
				array.add(parseResult.getFirst());
				return startInner + parseResult.getSecond().intValue();
			}
		}.parseList(json, start);
	}

	/**
	 * Parses a JSON name/value pair starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a name, a JSON value, and the number of characters parsed, as a triple
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Triple<String,? extends JsonValue,Integer> parsePair(CharSequence json, int start) throws JsonException {
		// Read the name.
		Pair<JsonString,Integer> key = parseString(json, start);

		// Look for the colon.
		int end = skipWhitespace(json, start + key.getSecond().intValue());
		assertChar(json, end++, ':', "Couldn't find colon separator while reading JSON name/value pair.");

		// Read the value.
		Pair<? extends JsonValue,Integer> value = parseValue(json, end);
		end += value.getSecond().intValue();

		return new Triple<String,JsonValue,Integer>(key.getFirst().getValue(), value.getFirst(), Integer.valueOf(end - start));
	}

	/**
	 * Parses a JSON number starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a JSON number and the number of characters parsed, as a pair
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Pair<JsonNumber,Integer> parseNumber(CharSequence json, int start) throws JsonException {
		int numStart = skipWhitespace(json, start);

		// Find the continuous sequence of "numeric" characters.
		int end = numStart;
		for(; end < json.length(); ++end)
			if(!isNumeric(json.charAt(end)))
				break;

		// Try to parse the number.
		BigDecimal value = null;
		try {
			value = new BigDecimal(json.subSequence(numStart, end).toString());
		}
		catch(NumberFormatException e) { // It wasn't a number. Sadness.
			throw new JsonException("Failed while reading JSON number.", e);
		}

		return new Pair<JsonNumber,Integer>(new JsonNumber(value), Integer.valueOf(end - start));
	}

	/**
	 * Parses a JSON null starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a JSON null and the number of characters parsed, as a pair
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Pair<JsonNull,Integer> parseNull(CharSequence json, int start) throws JsonException {
		int end = skipWhitespace(json, start);
		String goal = "null";

		// No need to check bounds since the method we're calling already claims to do so.
		if(!subSequenceEqualsIgnoreCase(json, end, goal))
			throw new JsonException("Failed while reading JSON null.");

		// Advance the cursor.
		end += goal.length();

		return new Pair<JsonNull,Integer>(JsonNull.getInstance(), Integer.valueOf(end - start));
	}

	/**
	 * Parses a JSON boolean starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a JSON boolean and the number of characters parsed, as a pair
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Pair<JsonBoolean,Integer> parseBoolean(CharSequence json, int start) throws JsonException {
		int end = skipWhitespace(json, start);
		Pair<JsonBoolean,Integer> ret = null;

		// Gotta check bounds since we need to look at the first character.
		if(end < json.length()) {
			boolean value = json.charAt(end) == 't' || json.charAt(end) == 'T';
			String goal = value ? "true" : "false";
			if(subSequenceEqualsIgnoreCase(json, end, goal)) {
				end += goal.length(); // Advance the cursor.
				ret = new Pair<JsonBoolean,Integer>(new JsonBoolean(value), Integer.valueOf(end - start));
			}
		}

		// Something went wrong if we didn't read anything.
		if(ret == null)
			throw new JsonException("Failed while reading JSON boolean.");

		return ret;
	}

	/**
	 * Parses a JSON string starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a JSON string and the number of characters parsed, as a pair
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Pair<JsonString,Integer> parseString(CharSequence json, int start) throws JsonException {
		int end = skipWhitespace(json, start);

		// Ask for a quote.
		assertChar(json, end++, '"', "Couldn't find opening quote while parsing JSON string.");

		// Build the string(builder).
		StringBuilder sb = new StringBuilder();
		for(char c; end < json.length() && (c = json.charAt(end)) != '"'; ++end) {
			if(c == '\\') { // escape
				++end;
				if(end < json.length()) {
					switch(c = json.charAt(end)) {
					case '"':
					case '\\':
					case '/':
						sb.append(c);
						break;
					case 'b':
						sb.append('\b');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'n':
						sb.append('\n');
						break;
					case 'r':
						sb.append('\r');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'u': // Unicode escape
						end += 4; // Moves cursor to last character in escape.
						if(end < json.length())
							sb.append(Character.toChars(Integer.parseInt(json.subSequence(end - 3, end + 1).toString().toLowerCase(Locale.ENGLISH), 16)));
						else
							throw new JsonException("Reached end of input while parsing JSON string.");
						break;
					default:
						throw new JsonException("Reached end of input while parsing JSON string.");
					}
				}
				else
					throw new JsonException("Reached end of input while parsing JSON string.");
			}
			else // literal character
				sb.append(c);
		}

		// Another quote!
		assertChar(json, end++, '"', "Couldn't find ending quote while parsing JSON string.");

		return new Pair<JsonString,Integer>(new JsonString(sb), Integer.valueOf(end - start));
	}

	/**
	 * Finds and returns the smallest index in the text not lesser than the one
	 * given containing a non-whitespace character.
	 *
	 * @param text
	 *            the text to check
	 * @param index
	 *            the starting index
	 * @return the first index in the text no lesser than the given one
	 *         containing a non-whitespace character, or the maximum of the
	 *         input and the length of the text, if there are no non-whitespace
	 *         characters at or past the given index
	 */
	protected int skipWhitespace(CharSequence text, int index) {
		int ret = index;
		while(ret < text.length() && Character.isWhitespace(text.charAt(ret)))
			++ret;
		return ret;
	}

	/**
	 * Checks if the text matches the goal string case-insensitively starting at
	 * the specified position.
	 *
	 * @param text
	 *            the text to check
	 * @param start
	 *            the starting position in the text
	 * @param goal
	 *            the goal string to match against
	 * @return <code>true</code> if there is a match, <code>false</code> otherwise
	 */
	protected boolean subSequenceEqualsIgnoreCase(CharSequence text, int start, String goal) {
		int end = start + goal.length();
		return start >= 0 && end <= text.length() && text.subSequence(start, end).toString().equalsIgnoreCase(goal);
	}

	/**
	 * Asserts that the character in the given text at the specified index
	 * matches the expected value and throws an exception with the specified
	 * detail message if it does not.
	 *
	 * @param text
	 *            the text to check
	 * @param index
	 *            the index of the character to check
	 * @param expected
	 *            the expected value of the character
	 * @param errMsg
	 *            detail message of the exception which will be thrown if the
	 *            character does not match the expected value
	 * @throws JsonException
	 *             if the index is outside the bounds of the text, or the
	 *             character at that index doesn't match the expected value
	 */
	protected void assertChar(CharSequence text, int index, char expected, String errMsg) throws JsonException {
		if(!(index >= 0 && index < text.length() && text.charAt(index) == expected))
			throw new JsonException(errMsg);
	}

	/**
	 * Check if the given character can occur in a string which is the JSON
	 * format representation of a number. Therefore here "numeric" has a broader
	 * definition because the plus and minus sign, exponent indicators, and the
	 * decimal point will pass, in addition to the digits 0 through 9.
	 *
	 * @param c
	 *            the character to check
	 * @return <code>true</code> if the given character is "numeric",
	 *         <code>false</code> otherwise
	 */
	protected boolean isNumeric(char c) {
		return ('0' <= c && c <= '9') || c == '+' || c == '-' || c == 'e' || c == 'E' || c == '.';
	}

	/**
	 * Parses a JSON value starting at the specified position.
	 *
	 * @param json
	 *            a character sequence in JSON format
	 * @param start
	 *            the index at which parsing should start
	 * @return a JSON value and the number of characters parsed, as a pair
	 * @throws JsonException
	 *             if a problem occurs during parsing
	 */
	protected Pair<? extends JsonValue,Integer> parseValue(CharSequence json, int start) throws JsonException {
		Pair<? extends JsonValue,Integer> ret;

		int end = skipWhitespace(json, start);
		if(end < json.length()) {
			char c = json.charAt(end); // No, I don't want to worry about locale, so no lowercasing here.
			switch(c) {
			case '{':
				ret = parseObject(json, end);
				break;
			case '[':
				ret = parseArray(json, end);
				break;
			case '"':
				ret = parseString(json, end);
				break;
			case 't':
			case 'f':
			case 'T': // Let's be case-insensitive, why not?
			case 'F':
				ret = parseBoolean(json, end);
				break;
			case 'n':
			case 'N': // Case-insensitive. I'm so nice.
				ret = parseNull(json, end);
				break;
			default: // It better be a number if we got here.
				if(isNumeric(c)) // Technically only have to support digits and minus, but this is no effort.
					ret = parseNumber(json, end);
				else
					throw new JsonException("Encountered unexpected character while parsing.");
				break;
			}
		}
		else
			throw new JsonException("Reached end of input while parsing.");

		// Advance the cursor.
		end += ret.getSecond().intValue();

		return new Pair<JsonValue,Integer>(ret.getFirst(), Integer.valueOf(end - start));
	}
}
