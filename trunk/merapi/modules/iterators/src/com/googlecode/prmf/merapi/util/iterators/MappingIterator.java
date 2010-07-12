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
import com.googlecode.prmf.merapi.util.Mapper;

/**
 * Applies a mapping function to a traversal.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            domain of the mapping function
 * @param <U>
 *            range of the mapping function
 * @see Iterators#map(Mapper,Iterator)
 */
public class MappingIterator<T,U> extends AbstractUniversalIterator<U> {
	private final Mapper<? super T,? extends U> mapper;
	private final Iterator<? extends T> iterator;

	/**
	 * Constructs an iterator that applies the specified mapping function to the
	 * given traversal.
	 * 
	 * @param mapper
	 *            the mapping function
	 * @param iterator
	 *            the traversal to map
	 */
	public MappingIterator(Mapper<? super T,? extends U> mapper, Iterator<? extends T> iterator) {
		if(iterator == null)
			throw new NullPointerException("Can't map null iterator.");
		if(mapper == null)
			throw new NullPointerException("Can't use null mapper.");
		this.iterator = iterator;
		this.mapper = mapper;
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
	public U current() {
		return this.mapper.map(this.iterator.current());
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
	}
}
