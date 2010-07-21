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
package com.googlecode.prmf.merapi.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Stack;

/**
 * Utility methods for manipulating streams and "streamable" objects.
 * 
 * @author Miorel-Lucian Palii
 */
public class Streams {
	private static final int BUFFER_SIZE = 1 << 12;
	
	private static final Stack<char[]> bufferStore = new Stack<char[]>();
	private static final int MAX_BUFFERS_TO_STORE = 4;
	
	/**
	 * There is no need to instantiate this class.
	 */
	private Streams() {
	}

	/**
	 * Copies a file.
	 * 
	 * @param source
	 *            the file to copy
	 * @param destination
	 *            where to copy to
	 * @throws FileNotFoundException
	 *             if the source cannot be opened for reading or the destination
	 *             cannot be opened for writing
	 * @throws IOException
	 *             if another I/O error occurs
	 * @see #copy(InputStream,File)
	 */
	public static void copy(File source, File destination) throws FileNotFoundException, IOException {
		if(source == null)
			throw new NullPointerException("The source may not be null.");
		FileInputStream sourceStream = new FileInputStream(source);
		FileChannel sourceChannel = sourceStream.getChannel();
		copy(sourceChannel, destination, sourceChannel.size());
		close(sourceStream);
	}

	/**
	 * Copies up to the specified number of bytes from a stream to a file.
	 * 
	 * @param source
	 *            the stream to read from
	 * @param destination
	 *            where to write to
	 * @param size
	 *            the maximum number of bytes to copy
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #copy(InputStream,File)
	 */
	public static void copy(InputStream source, File destination, long size) throws IOException {
		if(source == null)
			throw new NullPointerException("The source may not be null.");
		copy(Channels.newChannel(source), destination, size);
	}

	private static void copy(ReadableByteChannel source, File destination, long size) throws IOException {
		if(source == null)
			throw new NullPointerException("The source may not be null.");
		if(destination == null)
			throw new NullPointerException("The destination may not be null.");
		if(size < 0)
			throw new IllegalArgumentException("The size may not be negative.");
		try {
			destination.getParentFile().mkdirs();
			destination.getAbsoluteFile().getParentFile().mkdirs();
		}
		catch(Exception e) {}
		FileOutputStream destStream = new FileOutputStream(destination);
		FileChannel destChannel = destStream.getChannel();
		try {
			destChannel.transferFrom(source, 0, size);
		}
		finally {
			close(source);
			close(destStream);
		}
	}
	
	/**
	 * Copies a stream to a file. This method is equivalent to
	 * <code>Streams.{@linkplain #copy(InputStream,File,long) copy}(source, destination, Long.MAX_VALUE)</code>.
	 * 
	 * @param source
	 *            the stream to read from
	 * @param destination
	 *            where to write to
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #copy(File,File)
	 */
	public static void copy(InputStream source, File destination) throws IOException {
		copy(source, destination, Long.MAX_VALUE);
	}

	/**
	 * Reads the entire contents of a stream into a buffer.
	 * 
	 * @param stream
	 *            the stream to read
	 * @return a buffer containing the contents of the stream
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(InputStream) Iterators#lines(InputStream)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 * @see #slurp(URLConnection)
	 */
    public static StringBuilder slurp(InputStream stream) throws IOException {
    	return slurp(new InputStreamReader(stream));
    }

	/**
	 * Reads the entire contents of a <code>Reader</code> into a buffer.
	 * 
	 * @param reader
	 *            the <code>Reader</code> to read
	 * @return a buffer containing the contents of the <code>Reader</code>
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(BufferedReader) Iterators#lines(BufferedReader)
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(Reader) Iterators#lines(Reader)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 * @see #slurp(URLConnection)
	 */
    public static StringBuilder slurp(Reader reader) throws IOException {
		StringBuilder ret = new StringBuilder();
		char[] buf;
		
		// Try getting a buffer from the store if possible.
		synchronized(bufferStore) {
			buf = bufferStore.isEmpty() ? new char[BUFFER_SIZE] : bufferStore.pop(); 
		}
		
		try {
			for(int n; (n = reader.read(buf)) != -1;)
				ret.append(buf, 0, n);
		}
		finally {
			close(reader);
			
			// Add it to the store unless we already have some.
			synchronized(bufferStore) {
				if(bufferStore.size() < MAX_BUFFERS_TO_STORE)
					bufferStore.push(buf);
			}
		}
		
		return ret;
	}

	/**
	 * Reads the entire contents of a file into a buffer.
	 * 
	 * @param file
	 *            the file to read
	 * @return a buffer containing the contents of the file
	 * @throws FileNotFoundException
	 *             if the file cannot be opened for reading
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(File) Iterators#lines(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 * @see #slurp(URLConnection)
	 */
	public static StringBuilder slurp(File file) throws FileNotFoundException, IOException {
		return slurp(new FileInputStream(file));
	}

	/**
	 * Reads the entire contents of a file descriptor into a buffer.
	 * 
	 * @param fd
	 *            the file descriptor to read
	 * @return a buffer containing the contents of the file descriptor
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(FileDescriptor) Iterators#lines(FileDescriptor)
	 * @see #slurp(File)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 * @see #slurp(URLConnection)
	 */
	public static StringBuilder slurp(FileDescriptor fd) throws IOException {
		return slurp(new FileInputStream(fd));
	}

	/**
	 * Reads the entire contents of a file into a buffer.
	 * 
	 * @param path
	 *            the path of the file to read
	 * @return a buffer containing the contents of the file
	 * @throws FileNotFoundException
	 *             if the file cannot be opened for reading
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(URL)
	 * @see #slurp(URLConnection)
	 */
	public static StringBuilder slurp(String path) throws FileNotFoundException, IOException {
		return slurp(new FileInputStream(path));
	}

	/**
	 * Reads the entire contents of a resource into a buffer.
	 * 
	 * @param url
	 *            address of the resource to read
	 * @return a buffer containing the contents of the resource
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(URL) Iterators#lines(URL)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URLConnection)
	 */
	public static StringBuilder slurp(URL url) throws IOException {
		return slurp(url.openStream());
	}
	
	/**
	 * Reads the entire contents of a resource into a buffer.
	 * 
	 * @param connection
	 *            a connection to the resource to read
	 * @return a buffer containing the contents of the resource
	 * @throws IOException
	 *             if an I/O error occurs
	 * @see com.googlecode.prmf.merapi.util.Iterators#lines(URLConnection) Iterators#lines(URLConnection)
	 * @see #slurp(File)
	 * @see #slurp(FileDescriptor)
	 * @see #slurp(InputStream)
	 * @see #slurp(Reader)
	 * @see #slurp(String)
	 * @see #slurp(URL)
	 */
	public static StringBuilder slurp(URLConnection connection) throws IOException {
		return slurp(connection.getInputStream());
	}

	/**
	 * Closes the given source or destination of data, capturing any thrown
	 * exception.
	 * 
	 * @param closeable
	 *            the source or destination to close
	 */
	public static void close(Closeable closeable) {
		try {
			closeable.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
}
