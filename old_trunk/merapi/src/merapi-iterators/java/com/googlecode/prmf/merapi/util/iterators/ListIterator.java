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

/**
 * <p>
 * Abstract, template method-based iterator over a list or list-like structure.
 * </p>
 * <p>
 * Subclasses need only define the starting and ending positions in the list, as
 * well as the mechanism for accessing the element at a given position.
 * </p>
 * <p>
 * In general, there is no defense mechanism preventing modification of any
 * underlying list while this iterator is in use. Any changes will therefore
 * propagate through to users of this iterator. Taking advantage of this is
 * discouraged.
 * </p>
 *
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type of elements in the list
 */
public abstract class ListIterator<T> extends AbstractReversibleIterator<T> {
	private final int begin;
	private final int end;
	private final int increment;

	private int pointer;

	/**
	 * Constructs an iterator that will traverse the list values in the
	 * specified range of positions, possibly in reverse order. The first
	 * position is inclusive, the last position is not inclusive. This
	 * constructor makes it straightforward for subclasses to implement the
	 * {@link #reverse()} method, as in this example:
	 *
	 * <pre>
	 * public class ExampleListIterator&lt;T&gt; extends ListIterator&lt;T&gt; {
	 * 	private final ExampleList&lt;T&gt; list;
	 *
	 * 	public ExampleListIterator(ExampleList&lt;T&gt; list) {
	 * 		this(list, false);
	 * 	}
	 *
	 * 	protected ExampleListIterator(ExampleList&lt;T&gt; list, boolean reverse) {
	 * 		super(list.begin(), list.end(), reverse);
	 * 		this.list = list;
	 * 	}
	 *
	 * 	public ReversibleIterator&lt;T&gt; reverse() {
	 * 		return new ExampleListIterator&lt;T&gt;(list, true);
	 * 	}
	 * }
	 * </pre>
	 *
	 * @param begin
	 *            the first position in the range
	 * @param end
	 *            the position immediately after the last position in the range
	 * @param reverse
	 *            whether or not to reverse the traversal order
	 */
	protected ListIterator(int begin, int end, boolean reverse) {
		if(!reverse) {
			this.begin = begin;
			this.end = end;
		}
		else {
			this.begin = end;
			this.end = begin;
		}
		this.increment = begin < end ? 1 : -1;
		reset();
	}

	/**
	 * Advances this iterator to the next position in the underlying list or
	 * list-like structure.
	 */
	@Override
	public void advance() {
		if(!isDone())
			this.pointer += this.increment;
	}

	/**
	 * Defines how this iterator accesses the element at the specified position
	 * in the underlying list or list-like structure.
	 *
	 * @param position
	 *            index of the element to return
	 * @return the element at the specified position in the list
	 */
	protected abstract T get(int position);

	/**
	 * Retrieves the current element in the underlying list or list-like
	 * structure.
	 *
	 * @return the current list element
	 */
	@Override
	public T current() {
		return get(this.pointer);
	}

	/**
	 * Checks whether this iterator has reached the end of the list traversal.
	 *
	 * @return whether the end of the list traversal has been reached
	 */
	@Override
	public boolean isDone() {
		return this.pointer == this.end;
	}

	/**
	 * Moves this iterator to the beginning of its list traversal.
	 */
	@Override
	public void reset() {
		this.pointer = this.begin;
	}
}
