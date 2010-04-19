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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Scanner;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * <p>
 * Abstract definition of an iterator that gets lines from an input source.
 * </p>
 * <p>
 * A subclass should override {@link #getNextLine()} to define how the lines are
 * read and call {@link #init()} from within the constructor.
 * </p>
 * 
 * @see Iterators#lines(BufferedReader)
 * @see Iterators#lines(File)
 * @see Iterators#lines(FileDescriptor)
 * @see Iterators#lines(InputStream)
 * @see Iterators#lines(Reader)
 * @see Iterators#lines(Scanner)
 * @see Iterators#lines(URL)
 */
public abstract class LineIterator extends IteratorAdapter<String> {
	/**
	 * Default constructor, does nothing.
	 */
	public LineIterator() {
	}

	/**
	 * Reads the next line and prepares it to be returned by calls to
	 * {@link #current()}. If there is an I/O error, it's silently trapped, but
	 * the iterator is set to done.
	 */
	@Override
	protected void doAdvance() {
		if(!isDone()) {
			try {
				String line = getNextLine();
				setCurrent(line);
				if(line == null)
					markAsDone();
			}
			catch(IOException e) {
				markAsDone();
			}
		}
	}

	/**
	 * Defines how this iterator reads lines. Implementations should signal that
	 * there are no more lines by returning <code>null</code>.
	 * 
	 * @return the contents of the next line, or <code>null</code> if there are
	 *         no more lines
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected abstract String getNextLine() throws IOException;
}
