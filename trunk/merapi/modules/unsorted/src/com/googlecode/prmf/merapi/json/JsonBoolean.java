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

import java.util.Locale;

/**
 * A true or false value in JavaScript Object Notation (JSON).
 *
 * @author Miorel-Lucian Palii
 */
public class JsonBoolean implements JsonValue {
	private boolean value;

	/**
	 * Creates a new JSON boolean with a default value.
	 */
	public JsonBoolean() {
		this(false);
	}

	/**
	 * Initializes a JSON boolean with the specified value.
	 *
	 * @param value
	 *            this object's initial value
	 */
	public JsonBoolean(boolean value) {
		setValue(value);
	}

	/**
	 * Initializes a JSON boolean from the specified character sequence.
	 *
	 * @param value
	 *            this object's initial value
	 */
	public JsonBoolean(CharSequence value) {
		setValue(value);
	}

	/**
	 * Returns the <code>boolean</code> wrapped by this object.
	 *
	 * @return the boolean value
	 */
	public boolean getValue() {
		return this.value;
	}

	/**
	 * Updates this JSON boolean's value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(boolean value) {
		this.value = value;
	}

	/**
	 * Updates this JSON boolean's value.
	 *
	 * @param value
	 *            character sequence representation of the new value
	 * @throws IllegalArgumentException
	 *             if the argument is not <code>"true"</code> or
	 *             <code>"false"</code>
	 */
	public void setValue(CharSequence value) {
		doSetValue(value.toString().toLowerCase(Locale.ENGLISH));
	}

	private void doSetValue(String newValue) {
		if(newValue.equals("true"))
			this.value = true;
		else if(newValue.equals("false"))
			this.value = false;
		else
			throw new IllegalArgumentException("The argument was not \"true\" or \"false\".");
	}

	@Override
	public String toJson() {
		return this.value ? "true" : "false";
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (obj instanceof JsonBoolean && this.value == ((JsonBoolean) obj).value);
	}

	@Override
	public int hashCode() {
		return this.value ? 3 : 2;
	}

	@Override
	public String toString() {
		return "JSON boolean: " + toJson();
	}
}
