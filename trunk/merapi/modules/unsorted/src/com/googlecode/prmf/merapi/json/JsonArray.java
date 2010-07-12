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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.DelegatingList;
import com.googlecode.prmf.merapi.util.Iterators;
import com.googlecode.prmf.merapi.util.Strings;

/**
 * An ordered collection of values in JavaScript Object Notation (JSON).
 * 
 * @author Miorel-Lucian Palii
 */
public class JsonArray extends DelegatingList<JsonValue> implements JsonValue {
	private final List<JsonValue> list;
	
	/**
	 * Initializes an empty JSON array.
	 */
	public JsonArray() {
		this.list = new ArrayList<JsonValue>();
	}

	/**
	 * Initializes a JSON array containing the elements of the specified
	 * collection, in the order returned by the collection's iterator.
	 * 
	 * @param c
	 *            the collection whose elements are to be placed in this JSON
	 *            array
	 */
	public JsonArray(Collection<? extends JsonValue> c) {
		this.list = new ArrayList<JsonValue>(c);
	}

	/**
	 * Initializes a JSON array containing the specified elements, in the given
	 * order.
	 * 
	 * @param values
	 *            the elements that are to be placed in this JSON array
	 */
	public JsonArray(JsonValue... values) {
		this(Iterators.iterator(values));
	}

	/**
	 * Initializes a JSON array containing the same elements as the given
	 * iterator, in the same order.
	 * 
	 * @param values
	 *            an iterator over the elements that are to be placed in this
	 *            JSON array
	 */
	public JsonArray(Iterator<? extends JsonValue> values) {
		this.list = Iterators.list(values);
	}

	@Override
	protected List<JsonValue> getDelegate() {
		return this.list;
	}

	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');

		// map the values given by this object's iterator to strings in JSON format
		sb.append(Strings.join(",", Iterators.map(TO_JSON, iterator())));

		sb.append(']');
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "JSON array: " + toJson();
	}
}
