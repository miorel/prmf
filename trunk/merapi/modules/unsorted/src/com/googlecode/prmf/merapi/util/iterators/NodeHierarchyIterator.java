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

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over a Document Object Model node hierarchy.
 * 
 * @author Miorel-Lucian Palii
 * @see Iterators#tree(Node)
 */
public class NodeHierarchyIterator extends TreeIterator<Node> {
	private final Node root;
	
	/**
	 * Prepares an iterator that will do a depth-first traversal of the tree
	 * that has the given node as root.
	 * 
	 * @param root root of the node hierarchy
	 */
	public NodeHierarchyIterator(Node root) {
		if(root == null)
			throw new NullPointerException("Can't iterate over a null node hierarchy.");
		this.root = root;
		reset();
	}

	/**
	 * Returns an iterator over the children of the given node.
	 * 
	 * @param node
	 *            parent of the nodes to iterate over
	 * @return an iterator over the children of the node
	 */
	@Override
	protected Iterator<Node> childIterator(Node node) {
		return Iterators.iterator(node.getChildNodes());
	}

	@Override
	protected Iterator<Node> getInitialIterator() {
		return Iterators.iterator(this.root);
	}
}
