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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * A line iterator that can read from an {@link InputStream} and related
 * objects.
 * 
 * @see Iterators#lines(BufferedReader)
 * @see Iterators#lines(File)
 * @see Iterators#lines(FileDescriptor)
 * @see Iterators#lines(InputStream)
 * @see Iterators#lines(Reader)
 * @see Iterators#lines(URL)
 */
public class StreamIterator extends LineIterator {
	private final BufferedReader reader;

	/**
	 * Constructs an iterator that reads from the specified
	 * <code>BufferedReader</code>.
	 * 
	 * @param reader
	 *            the input source
	 */
	public StreamIterator(BufferedReader reader) {
		if(reader == null)
			throw new NullPointerException("Can't read lines from null reader.");
		this.reader = reader;
		init();
	}

	/**
	 * Constructs an iterator that reads from the specified <code>Reader</code>.
	 * 
	 * @param reader
	 *            the input source
	 */
	public StreamIterator(Reader reader) {
		this(new BufferedReader(reader));
	}
	
	/**
	 * Constructs an iterator that reads from the file with the specified name.
	 * 
	 * @param file
	 *            name of the file to use as input source
	 * @throws FileNotFoundException
	 *             if the file can't be opened for reading
	 */
	public StreamIterator(String file) throws FileNotFoundException {
		this(new FileReader(file));
	}

	/**
	 * Constructs an iterator that reads from the specified stream.
	 * 
	 * @param stream
	 *            the input source
	 */
	public StreamIterator(InputStream stream) {
		this(new InputStreamReader(stream));
	}

	/**
	 * Constructs an iterator that reads from the specified file.
	 * 
	 * @param file
	 *            the input source
	 * @throws FileNotFoundException
	 *             if the file can't be opened for reading
	 */
	public StreamIterator(File file) throws FileNotFoundException {
		this(new FileReader(file));
	}
	
	/**
	 * Constructs an iterator that reads from the specified file descriptor.
	 * 
	 * @param fd
	 *            the input source
	 */
	public StreamIterator(FileDescriptor fd) {
		this(new FileReader(fd));
	}

	/**
	 * Constructs an iterator that reads from the specified <code>URL</code>.
	 * 
	 * @param url
	 *            the input source
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public StreamIterator(URL url) throws IOException {
		this(url.openStream());
	}

	/**
	 * Reads and returns the contents of the next line, or <code>null</code> if
	 * there are no more lines.
	 * 
	 * @return the contents of the next line, or <code>null</code> if there are
	 *         no more lines
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected String getNextLine() throws IOException {
		return this.reader.readLine();
	}
}
