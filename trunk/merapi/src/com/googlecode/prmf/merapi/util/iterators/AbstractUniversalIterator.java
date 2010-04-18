/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package com.googlecode.prmf.merapi.util.iterators;

import java.util.NoSuchElementException;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * Skeleton implementation of a {@link UniversalIterator}. Subclasses need only
 * implement the {@linkplain Iterator Gang of Four iterator} methods.
 * 
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators
 */
public abstract class AbstractUniversalIterator<T> implements UniversalIterator<T> {
	/**
	 * Default constructor, does nothing.
	 */
	public AbstractUniversalIterator() {
	}

	@Override
	public final boolean hasNext() {
		return !isDone();
	}

	@Override
	public final T next() {
		if(isDone())
			throw new NoSuchElementException("The iterator is done.");
		T ret = current();
		advance();
		return ret;
	}

	/**
	 * Always throws an <code>UnsupportedOperationException</code>. I adhere to
	 * the Dave Small philosophy that this method is an abomination. The ability
	 * to remove an element is not inherent to iterators, nor does it make sense
	 * for all iterators.
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This operation is an abomination.");
	}

	/**
	 * Returns the object it's called on, since it will already be an iterator.
	 * 
	 * @return <code>this</code>
	 */
	@Override
	public UniversalIterator<T> iterator() {
		return this;
	}

	@Override
	public final boolean hasMoreElements() {
		return hasNext();
	}

	@Override
	public final T nextElement() {
		return next();
	}
}
