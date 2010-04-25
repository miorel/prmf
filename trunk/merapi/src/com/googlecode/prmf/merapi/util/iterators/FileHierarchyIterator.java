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

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over a file hierarchy.
 * 
 * @author Miorel-Lucian Palii
 * @see Iterators#tree(File)
 */
public class FileHierarchyIterator extends TreeIterator<File> {
	private final File root;

	/**
	 * Prepares an iterator that will do a depth-first traversal of the file
	 * hierarchy that has the given root.
	 * 
	 * @param root
	 *            root of the file hierarchy
	 */
	public FileHierarchyIterator(File root) {
		if(root == null)
			throw new NullPointerException("Can't iterate over a null file hierarchy.");
		this.root = root;
		reset();
	}

	/**
	 * Returns an iterator over the subfiles of the given file. If the file is
	 * not a directory, this will be an iterator with no elements.
	 * 
	 * @param file
	 *            parent of the files to iterate over
	 * @return an iterator over the file's subfiles
	 */
	@Override
	protected Iterator<File> childIterator(File file) {
		return Iterators.children(file);
	}

	@Override
	protected Iterator<File> getInitialIterator() {
		return Iterators.iterator(this.root);
	}
}
