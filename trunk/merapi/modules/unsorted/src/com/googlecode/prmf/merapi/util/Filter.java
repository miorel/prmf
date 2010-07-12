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

/**
 * <p>
 * Defines an operation for deciding whether or not to &quot;keep&quot; objects.
 * </p>
 * <p>
 * It's difficult to describe this interface without using the word
 * &quot;filter&quot; because that's basically what it's intended to do. The
 * single operation takes an object and gives back a boolean verdict on the
 * matter. A filter will typically be applied to each member of a collection to
 * obtain the subset of that collection that has some desired property.
 * </p>
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type to be filtered
 */
public interface Filter<T> {
	/**
	 * A convenience filter that discards any <code>null</code> object.
	 */
	public static final Filter<Object> NON_NULL = new Filter<Object>() {
		@Override
		public boolean keep(Object object) {
			return object != null;
		}
	};

	/**
	 * Checks if the given object passes this filter.
	 * 
	 * @param object
	 *            the object to check
	 * @return whether or not to keep the object
	 */
	public boolean keep(T object);
}
