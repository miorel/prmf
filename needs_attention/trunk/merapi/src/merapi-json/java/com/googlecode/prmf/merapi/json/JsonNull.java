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

/**
 * Represents a null entity in JavaScript Object Notation (JSON).
 * 
 * @author Miorel-Lucian Palii
 */
public class JsonNull implements JsonValue {
	private static final JsonNull instance = new JsonNull(); 
	
	/**
	 * This shall be a singleton.
	 */
	private JsonNull() {
	}

	/**
	 * Returns the JSON null singleton.
	 * 
	 * @return a JSON null
	 */
	public static JsonNull getInstance() {
		return instance;
	}

	@Override
	public String toJson() {
		return "null";
	}
	
	@Override
	public String toString() {
		return "JSON null";
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof JsonNull;
	}
}
