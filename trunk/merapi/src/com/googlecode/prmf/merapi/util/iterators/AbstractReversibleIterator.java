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
package com.googlecode.prmf.merapi.util.iterators;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * Subclass of {@link AbstractUniversalIterator} with a more specific return
 * type for the {@link #iterator() iterator()} method.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#reverse(Iterator)
 */
public abstract class AbstractReversibleIterator<T> extends AbstractUniversalIterator<T> implements ReversibleIterator<T> {
	/**
	 * Default constructor, does nothing.
	 */
	public AbstractReversibleIterator() {
	}
	
	/**
	 * Returns the object it's called on, since it will already be an iterator.
	 * 
	 * @return the object it's called on
	 */
	@Override
	public ReversibleIterator<T> iterator() {
		return this;
	}
}
