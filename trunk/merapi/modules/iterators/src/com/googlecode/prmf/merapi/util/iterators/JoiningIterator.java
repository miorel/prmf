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

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator that joins the traversals of a group of iterators into one.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#join(Iterator)
 * @see Iterators#join(Iterator...)
 */
public class JoiningIterator<T> extends AbstractUniversalIterator<T> {
	private final Iterator<? extends Iterator<? extends T>> iterator;

	/**
	 * Unfolds an iterator of iterators by joining the elements of its elements.
	 * 
	 * @param iterator
	 *            iterator of iterators over which to iterate :)
	 */
	public JoiningIterator(Iterator<? extends Iterator<? extends T>> iterator) {
		if(iterator == null)
			throw new NullPointerException("Can't unfold a null iterator.");
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
		this.iterator.current().advance();
		validateState();
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
		return this.iterator.current().current();
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
		return this.iterator.isDone();
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
		if(!this.iterator.isDone()) {
			this.iterator.current().reset();
			validateState();
		}
	}
	
	private void validateState() {
		while(this.iterator.current().isDone()) {
			this.iterator.advance();
			if(this.iterator.isDone())
				break;
			this.iterator.current().reset();
		}	
	}
}
