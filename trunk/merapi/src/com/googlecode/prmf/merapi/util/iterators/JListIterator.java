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

import java.util.List;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over a {@linkplain List Java list}.
 * 
 * @param <T>
 *            type of elements in the list
 * @see Iterators#iterator(List)
 */
public class JListIterator<T> extends ListIterator<T> {
	private final List<T> list;

	/**
	 * Constructs an iterator over the specified list.
	 * 
	 * @param list
	 *            list over which to iterate
	 */
	public JListIterator(List<T> list) {
		this(list, false);
	}

	/**
	 * Constructs an iterator over the specified list, possibly in reverse
	 * order.
	 * 
	 * @param list
	 *            list over which to iterate
	 * @param reverse
	 *            whether or not to reverse the traversal order
	 */
	protected JListIterator(List<T> list, boolean reverse) {
		super(0, list.size(), reverse);
		this.list = list;
	}

	@Override
	protected T get(int position) {
		return this.list.get(position);
	}

	@Override
	public ReversibleIterator<T> reverse() {
		return new JListIterator<T>(this.list, true);
	}
}
