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
package com.googlecode.prmf.merapi.util.iterators;

import java.util.Enumeration;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * Adapter for an {@link Enumeration}. Because <code>Enumeration</code>s do not
 * provide a {@link #reset()}-like operation, the returned iterator will not be
 * resettable.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#adapt(Enumeration)
 */
public class JEnumerationAdapter<T> extends IteratorAdapter<T> {
	private final Enumeration<T> enumeration;

	/**
	 * Constructs an iterator that adapts the specified
	 * <code>Enumeration</code>.
	 * 
	 * @param enumeration
	 *            the adaptee
	 */
	public JEnumerationAdapter(Enumeration<T> enumeration) {
		if(enumeration == null)
			throw new NullPointerException("Can't adapt null enumeration.");
		this.enumeration = enumeration;
		init();
	}

	@Override
	protected void doAdvance() {
		if(this.enumeration.hasMoreElements())
			setCurrent(this.enumeration.nextElement());
		else
			markAsDone();
	}
}
