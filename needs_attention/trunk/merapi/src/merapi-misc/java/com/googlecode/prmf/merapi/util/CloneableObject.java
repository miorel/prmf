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

/**
 * <p>
 * An alternative to implementing {@link Cloneable} for cloneable objects.
 * </p>
 * <p>
 * Often, the default mechanism of copying an instance of a class is sufficient.
 * But <code>CloneNotSupportedException</code> remains a checked exception that
 * must be caught. Extending this class saves you the trouble of writing code
 * to catch an exception that will never be thrown. 
 * </p>
 * 
 * @author Miorel-Lucian Palii
 * @see Cloneable
 */
public class CloneableObject implements Cloneable {
	/**
	 * Default constructor, does nothing.
	 */
	public CloneableObject() {
	}

	/**
	 * Creates and returns a copy of the object.
	 */
	@Override
	protected CloneableObject clone() {
		CloneableObject ret = null;
		try {
			ret = (CloneableObject) super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error("A cloneable object threw a CNSE!", e);
		}
		return ret;
	}
}
