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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A numeric value in JavaScript Object Notation (JSON).
 *
 * @author Miorel-Lucian Palii
 */
public class JsonNumber implements JsonValue {
	private BigDecimal value;

	/**
	 * Initializes a JSON number with a value of zero.
	 */
	public JsonNumber() {
		this(BigDecimal.ZERO);
	}

	/**
	 * Initializes a JSON number with the specified value.
	 *
	 * @param value
	 *            this object's initial value
	 */
	public JsonNumber(double value) {
		setValue(value);
	}

	/**
	 * Initializes a JSON number with the specified value.
	 *
	 * @param value
	 *            this object's initial value
	 */
	public JsonNumber(BigInteger value) {
		setValue(value);
	}

	/**
	 * Initializes a JSON number with the specified value.
	 *
	 * @param value
	 *            this object's initial value
	 */
	public JsonNumber(BigDecimal value) {
		setValue(value);
	}

	/**
	 * Updates this JSON boolean's value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(BigDecimal value) {
		if(value == null)
			throw new NullPointerException("The value may not be null.");
		this.value = value;
	}

	/**
	 * Updates this JSON boolean's value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(BigInteger value) {
		setValue(new BigDecimal(value));
	}

	/**
	 * Updates this JSON boolean's value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(long value) {
		setValue(BigInteger.valueOf(value));
	}

	/**
	 * Updates this JSON boolean's value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(double value) {
		setValue(BigDecimal.valueOf(value));
	}

	/**
	 * Returns this JSON number's value, converted to an <code>int</code>. This
	 * may result in a loss of precision if the value cannot be exactly
	 * represented as a <code>int</code> e.g. if it has a non-zero fractional
	 * part or its magnitude is too great.
	 *
	 * @return the numeric value
	 */
	public int intValue() {
		return this.value.intValue();
	}

	/**
	 * Returns this JSON number's value, converted to a <code>long</code>. This
	 * may result in a loss of precision if the value cannot be exactly
	 * represented as a <code>long</code> e.g. if it has a non-zero fractional
	 * part or its magnitude is too great.
	 *
	 * @return the numeric value
	 */
	public long longValue() {
		return this.value.longValue();
	}

	/**
	 * Returns this JSON number's value, converted to a <code>double</code>.
	 * This may result in a loss of precision if the value cannot be exactly
	 * represented as a <code>double</code> e.g. if its magnitude is too great.
	 *
	 * @return the numeric value
	 */
	public double doubleValue() {
		return this.value.doubleValue();
	}

	/**
	 * Returns this JSON number's value, converted to a <code>BigInteger</code>.
	 * This may result in a loss of precision if the value cannot be exactly
	 * represented as a <code>BigInteger</code> e.g. if it has a non-zero
	 * fractional part.
	 *
	 * @return the numeric value
	 */
	public BigInteger bigIntegerValue() {
		return this.value.toBigInteger();
	}

	/**
	 * Returns this JSON number's value.
	 *
	 * @return the numeric value
	 */
	public BigDecimal bigDecimalValue() {
		return this.value;
	}

	@Override
	public String toJson() {
		return this.value.stripTrailingZeros().toString().replaceFirst("[Ee]\\+?", "e");
	}

	@Override
	public int hashCode() {
		return toJson().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(obj == this)
			ret = true;
		else if(obj instanceof JsonNumber) {
			JsonNumber num = (JsonNumber) obj;
			ret = this.toJson().equals(num.toJson());
		}
		return ret;
	}

	@Override
	public String toString() {
		return "JSON number: " + toJson();
	}
}
