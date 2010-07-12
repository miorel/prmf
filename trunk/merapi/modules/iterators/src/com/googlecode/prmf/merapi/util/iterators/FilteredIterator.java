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
import com.googlecode.prmf.merapi.util.Filter;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * Applies a filter to a traversal.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#filter(Filter,Iterator)
 * @see Iterators#grep(Filter,Iterator)
 */
public class FilteredIterator<T> extends AbstractUniversalIterator<T> {
	private final Filter<? super T> filter;
	private final Iterator<? extends T> iterator;
	private boolean beginning;

	/**
	 * Constructs an iterator that applies the specified filter to the given
	 * traversal.
	 * 
	 * @param filter
	 *            the filter to apply
	 * @param iterator
	 *            the traversal to filter
	 */
	public FilteredIterator(Filter<? super T> filter, Iterator<? extends T> iterator) {
		if(iterator == null)
			throw new NullPointerException("Can't filter null iterator.");
		if(filter == null)
			throw new NullPointerException("Can't use null filter.");
		this.filter = filter;
		this.iterator = iterator;
		this.beginning = false;
		reset();
	}

	// had to actually copy documentation because of a silly bug in javadoc 
	/**
	 * Advances this iterator to the next element in the traversal. Behavior is
	 * undefined if the iterator {@linkplain #isDone() is done}.
	 */
	@Override
	public void advance() {
		this.beginning = false;
		do
			this.iterator.advance();
		while(!this.iterator.isDone() && !this.filter.keep(this.iterator.current()));
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
		if(!this.beginning) {
			this.iterator.reset();
			if(!this.filter.keep(this.iterator.current()))
				advance();
			this.beginning = true;
		}
	}
}
