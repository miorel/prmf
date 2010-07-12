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

import java.io.BufferedReader;

import com.googlecode.prmf.merapi.dp.Iterator;

/**
 * <p>
 * Abstract adapter for iterators and other objects that are conceptually
 * iterable.
 * </p>
 * <p>
 * {@linkplain java.util.Iterator Java's iterator} combines into one the
 * {@linkplain Iterator Gang of Four iterator}'s {@linkplain #current()
 * retrieve current element} and {@linkplain #advance() advance} operations.
 * Other structures over which it may be desirable to iterate have analogous
 * methods, such as {@link BufferedReader}'s {@link BufferedReader#readLine()
 * readLine()}. This class aims to reduce code duplication in adapters for all
 * these structures by providing a flexible common skeleton.
 * </p>
 * <p>
 * Subclasses should implement {@link #doAdvance()}, using
 * {@link #setCurrent(Object)} and {@link #markAsDone()} for state changes.
 * There is no need to check if the iterator is done from within
 * {@link #doAdvance()}, as this check is already made in this class.
 * </p>
 * <p>
 * They should also call {@link #init()} from within their constructors so the
 * iterator is valid. This requirement of the contract is not explicitly
 * enforced at this time, so run your tests.
 * </p>
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 */
public abstract class IteratorAdapter<T> extends AbstractUniversalIterator<T> {
	private boolean done;
	private boolean fresh;
	private T current;

	/**
	 * Default constructor, does nothing.
	 */
	public IteratorAdapter() {
	}

	/**
	 * Initializes the adapter. May retrieve one element from the adaptee as a
	 * side effect.
	 */
	protected void init() {
		this.done = false;
		advance();
		this.fresh = true;
	}

	/**
	 * Retrieves the next element and prepares it to be returned by calls to
	 * {@link #current()}.
	 */
	@Override
	public final void advance() {
		doAdvance();
		this.fresh = false;
	}

	/**
	 * Does the actual work of advancing this iterator.
	 */
	protected abstract void doAdvance();

	/**
	 * Marks this iterator as done. In other words, marks it such that
	 * subsequent calls to {@link #isDone()} will return <code>true</code>.
	 */
	protected void markAsDone() {
		this.done = true;
	}

	/**
	 * Sets the current element to the specified value.
	 * 
	 * @param element
	 *            the new current element
	 */
	protected void setCurrent(T element) {
		this.current = element;
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
		return this.current;
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
		return this.done;
	}

	/**
	 * Checks whether this is a "fresh" iterator. Calls to {@link #advance()}
	 * will set this to <code>false</code>. Newly-created
	 * iterators are expected to return <code>true</code> from this method.
	 * 
	 * @return whether this iterator is "fresh"
	 */
	protected boolean isFresh() {
		return this.fresh;
	}

	/**
	 * Does nothing in a newly-created iterator, otherwise throws an
	 * <code>IllegalStateException</code>.
	 * 
	 * @throws IllegalStateException
	 *             if the iterator isn't "fresh"
	 */
	@Override
	public void reset() throws IllegalStateException {
		if(!isFresh())
			throw new IllegalStateException("Can't go back to old elements.");
	}
}
