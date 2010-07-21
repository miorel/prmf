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
package com.googlecode.prmf.merapi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps objects onto characters. Used as default mapper by
 * {@link CharSequenceList}.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            the type of objects to map
 */
public class CharacterMapper<T> implements Mapper<T,Character> {
	private final Map<T,Character> map;
	
	private char max = Character.MIN_VALUE;
	
	/**
	 * Constructs a new character mapper.
	 */
	public CharacterMapper() {
		this.map = new HashMap<T,Character>();
	}

	@Override
	public Character map(T element) {
		Character ret;
		if(element == null)
			ret = Character.valueOf(Character.MIN_VALUE);
		else
			synchronized(this.map) {
				ret = this.map.get(element);
				if(ret == null) {
					if(this.max == Character.MAX_VALUE)
						throw new Error("Including this element will break the mapper's one-to-one requirement.");
					ret = Character.valueOf(++this.max);
					this.map.put(element, ret);
				}
			}
		return ret;
	}
}
