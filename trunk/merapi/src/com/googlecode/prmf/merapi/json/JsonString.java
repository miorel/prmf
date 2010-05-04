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
package com.googlecode.prmf.merapi.json;

import static com.googlecode.prmf.merapi.util.Iterators.chars;

/**
 * A JavaScript Object Notation (JSON) character sequence.
 *
 * @author Miorel-Lucian Palii
 */
public class JsonString implements JsonValue {
	private String value;

	/**
	 * Initializes a JSON string with the empty string.
	 */
	public JsonString() {
		this("");
	}

	/**
	 * Initializes a JSON string with the specified value.
	 *
	 * @param value
	 *            this object's initial value
	 */
	public JsonString(CharSequence value) {
		setValue(value);
	}

	/**
	 * Updates this JSON string's value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(CharSequence value) {
		this.value = value.toString();
	}

	/**
	 * Returns the character sequence wrapped by this object.
	 *
	 * @return the character sequence
	 */
	public String getValue() {
		return this.value;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof JsonString && this.value.equals(((JsonString) obj).value));
	}

	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append("\"");
		for(char c: chars(this.value))
			switch(c) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				if(Character.getType(c) == Character.CONTROL)
					sb.append(String.format("\\u%04x", Integer.valueOf(c)));
				else
					sb.append(c);
				break;
			}
		sb.append("\"");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "JSON string: " + toJson();
	}
}
