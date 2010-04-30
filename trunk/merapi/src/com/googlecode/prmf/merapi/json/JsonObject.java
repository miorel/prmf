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

import java.util.HashMap;
import java.util.Map;

import com.googlecode.prmf.merapi.util.DelegatingMap;
import com.googlecode.prmf.merapi.util.Iterators;
import com.googlecode.prmf.merapi.util.Mapper;
import com.googlecode.prmf.merapi.util.Strings;

/**
 * An unordered set of name/value pairs in JavaScript Object Notation (JSON).
 * 
 * @author Miorel-Lucian Palii
 */
public class JsonObject extends DelegatingMap<String,JsonValue> implements JsonValue {
	private final Map<String,JsonValue> map;

	/**
	 * Constructs an empty JSON object with a default initial capacity and load
	 * factor.
	 */
	public JsonObject() {
		this.map = new HashMap<String,JsonValue>();
	}

	/**
	 * Initializes a JSON object with the same mapping as the specified map.
	 * 
	 * @param m
	 *            the map whose mappings are to be placed in this JSON object
	 */
	public JsonObject(Map<? extends String,? extends JsonValue> m) {
		this.map = new HashMap<String,JsonValue>(m);
	}

	/**
	 * Descends into this JSON structure retrieving the value that corresponds
	 * to the specified keys relative to this object.
	 * <code>obj.get(keyA, keyB)</code> is basically syntactic sugar for
	 * <code>((JsonObject) obj.get(keyA)).get(keyB)</code>.
	 * 
	 * @param keys
	 *            the sequence of keys whose associated value is to be returned
	 * @return the value corresponding to the specified sequence of keys
	 */
	public JsonValue get(String... keys) {
		JsonValue ret = this;
		for(String key: keys)
			ret = ((JsonObject) ret).get(key);
		return ret;
	}
	
	@Override
	protected Map<String,JsonValue> getDelegate() {
		return this.map;
	}

	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append(Strings.join(",", Iterators.map(new Mapper<Entry<String,JsonValue>,CharSequence>() {
			@Override
			public CharSequence map(Entry<String,JsonValue> entry) {
				StringBuilder entryBuf = new StringBuilder();
				entryBuf.append(new JsonString(entry.getKey()).toJson());
				entryBuf.append(':');
				entryBuf.append(entry.getValue().toJson());
				return entryBuf;
			}
		}, iterator())));
		sb.append('}');
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return "JSON object: " + toJson();
	}
}
