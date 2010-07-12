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

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over the nodes of a {@link NodeList}.
 * 
 * @author Miorel-Lucian Palii
 * @see Iterators#iterator(NodeList)
 */
public class NodeListIterator extends ListIterator<Node> {
	private final NodeList list;

	/**
	 * Constructs an iterator over the nodes of the specified list.
	 * 
	 * @param list
	 *            list over which to iterate
	 */
	public NodeListIterator(NodeList list) {
		this(list, false);
	}

	/**
	 * Constructs an iterator over the nodes of the specified list, possibly in
	 * reverse order.
	 * 
	 * @param list
	 *            list over which to iterate
	 * @param reverse
	 *            whether or not to reverse the traversal order
	 */
	protected NodeListIterator(NodeList list, boolean reverse) {
		super(0, list.getLength(), reverse);
		this.list = list;
	}
	
	@Override
	protected Node get(int position) {
		return this.list.item(position);
	}

	@Override
	public ReversibleIterator<Node> reverse() {
		return new NodeListIterator(this.list, true);
	}
}
