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

import java.util.Iterator;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * Adapter for a {@linkplain Iterator Java iterator}. Because Java's
 * <code>Iterator</code>s do not provide a {@link #reset()}-like operation, the
 * returned iterator will not be resettable.
 * 
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#adapt(Iterable)
 * @see Iterators#adapt(Iterator)
 */
public class JIteratorAdapter<T> extends IteratorAdapter<T> {
	private final Iterator<T> iterator;

	/**
	 * Constructs an iterator that adapts the specified Java
	 * <code>Iterator</code>.
	 * 
	 * @param iterator
	 *            the adaptee
	 */
	public JIteratorAdapter(Iterator<T> iterator) {
		if(iterator == null)
			throw new NullPointerException("Can't adapt null iterator.");
		this.iterator = iterator;
		init();
	}

	/**
	 * Constructs an iterator that adapts the specified <code>Iterable</code>.
	 * 
	 * @param iterable
	 *            the adaptee
	 */
	public JIteratorAdapter(Iterable<T> iterable) {
		this(iterable.iterator());
	}

	@Override
	protected void doAdvance() {
		if(this.iterator.hasNext())
			setCurrent(this.iterator.next());
		else
			markAsDone();
	}
}
