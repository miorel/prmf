/*
 * merapi - Multi-purpose Java library
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

import java.io.File;
import java.util.Stack;

import org.w3c.dom.Node;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * <p>
 * Abstract, template method-based iterator over a tree or tree-like structure.
 * </p>
 * <p>
 * The traversal is currently done depth-first (though this is not set in
 * stone). This class works by managing a stack of iterators.
 * </p>
 * <p>
 * Tree surgeries during the traversal are likely to produce unexpected results
 * and should therefore be avoided. "Trees" containing cycles will produce
 * infinite traversals unless you find a clever way to avoid visiting elements
 * multiple times.
 * </p>
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            type over which the iteration takes place
 * @see Iterators#tree(File)
 * @see Iterators#tree(Node)
 */
public abstract class TreeIterator<T> extends AbstractUniversalIterator<T> {
	private final Stack<Iterator<T>> iterators = new Stack<Iterator<T>>();

	/**
	 * Default constructor, does nothing.
	 */
	public TreeIterator() {
	}

	/**
	 * Advances this iterator to the next tree element.
	 */
	@Override
	public void advance() {
		if(!isDone()) {
			T current = current();
			this.iterators.peek().advance();
			this.iterators.push(childIterator(current));
			while(!isDone() && this.iterators.peek().isDone())
				this.iterators.pop();
		}
	}
	
	/**
	 * Defines the mechanism for obtaining the children of a particular element.
	 * 
	 * @param element
	 *            parent of the elements to iterate over
	 * @return an iterator over the element's children
	 */
	protected abstract Iterator<T> childIterator(T element);

	@Override
	public T current() {
		return this.iterators.peek().current();
	}

	@Override
	public boolean isDone() {
		return this.iterators.isEmpty();
	}

	/**
	 * Moves this iterator to the root of the tree traversal.
	 */
	@Override
	public void reset() {
		this.iterators.clear();
		this.iterators.add(getInitialIterator());
	}
	
	/**
	 * Defines the mechanism for getting the initial element iterator. This will
	 * most likely just be a single element iterator, containing the root of the
	 * traversal.
	 * 
	 * @return the initial element iterator
	 */
	protected abstract Iterator<T> getInitialIterator();
}
