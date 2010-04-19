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

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over an array.
 * 
 * @param <T>
 *            type of elements in the array
 * @see Iterators#iterator(Object...)
 */
public class ArrayIterator<T> extends ListIterator<T> {
	private final T[] array;

	/**
	 * Constructs an iterator over the specified array.
	 * 
	 * @param array
	 *            array over which to iterate
	 */
	public ArrayIterator(T... array) {
		this(array, false);
	}

	/**
	 * Constructs an iterator over the specified array, possibly in reverse
	 * order.
	 * 
	 * @param array
	 *            array over which to iterate
	 * @param reverse
	 *            whether or not to reverse the traversal order
	 */
	protected ArrayIterator(T[] array, boolean reverse) {
		super(0, array.length, reverse);
		this.array = array;
	}

	@Override
	protected T get(int position) {
		return this.array[position];
	}

	@Override
	public ReversibleIterator<T> reverse() {
		return new ArrayIterator<T>(this.array, true);
	}
}
