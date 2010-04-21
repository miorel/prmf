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
 * An iterator that continuously loops over the elements of a traversal.
 * 
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#loop(Iterator)
 */
public class ContinuousIterator<T> extends AbstractUniversalIterator<T> {
	private final Iterator<? extends T> iterator;

	/**
	 * Constructs an iterator that continuously loops over the elements of the
	 * given traversal. If you need to loop an unresettable iterator, make sure
	 * you {@linkplain Iterators#copy(Iterator) copy} it first or you'll
	 * eventually have problems.
	 * 
	 * @param iterator
	 *            the traversal to loop
	 */
	public ContinuousIterator(Iterator<? extends T> iterator) {
		if(iterator == null)
			throw new NullPointerException("Can't loop over null iterator.");
		this.iterator = iterator;
		reset();
	}

	// had to actually copy documentation because of a silly bug in javadoc 
	/**
	 * Advances this iterator to the next element in the traversal. Behavior is
	 * undefined if the iterator {@linkplain #isDone() is done}.
	 */
	@Override
	public void advance() {
		this.iterator.advance();
	}

	// had to actually copy documentation because of a silly bug in javadoc
	/**
	 * Retrieves the current element in the traversal represented by this
	 * iterator.
	 * 
	 * @return the current element in the traversal
	 */
	@Override
	public T current() {
		return this.iterator.current();
	}

	// had to actually copy documentation because of a silly bug in javadoc
	/**
	 * Checks whether this iterator has exhausted all the elements of its
	 * underlying traversal.
	 * 
	 * @return whether all elements have been exhausted
	 */
	@Override
	public boolean isDone() {
		if(this.iterator.isDone())
			reset();
		return false;
	}

	// had to actually copy documentation because of a silly bug in javadoc
	/**
	 * Moves this iterator to the beginning of the traversal. Some iterators
	 * cannot be reset once the iteration started. For example, an iterator over
	 * the lines of an input stream probably can't reread old lines. But all
	 * iterators should allow exception-less calls to this method before any
	 * calls to {@link #advance()}.
	 * 
	 * @throws IllegalStateException
	 *             if resetting this iterator is impossible
	 */
	@Override
	public void reset() {
		this.iterator.reset();
		if(this.iterator.isDone())
			throw new NoSuchElementException("Can't loop over empty iterator.");
	}
}
