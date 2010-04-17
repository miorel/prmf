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
package com.googlecode.prmf.merapi.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.util.iterators.ArrayIterator;
import com.googlecode.prmf.merapi.util.iterators.JListIterator;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/*
 * <p>
 * For example, the <code>iterator()</code> method in this class comes in
 * numerous flavors. Its intent is to give the most logical iterator based on
 * the argument(s). The method not only supports "obvious" iterables, such as
 * {@linkplain List lists} and arrays, but can also be used to iterate over the
 * matching groups of a {@link MatchResult}, the {@linkplain Node nodes} in a
 * {@link NodeList}, and hopefully (eventually) anything else over which it may
 * be philosophically desirable to iterate. Design pattern addicts rejoice:
 * calling <code>iterator()</code> with no arguments will return a null iterator
 * (not <code>null</code>, silly, just an iterator which has no elements).
 * Similarly, calling it with a single element will return a single element
 * iterator.
 * </p>
 * <p>
 * In addition to providing a variety of iterators, this class comes with
 * methods for concisely accomplishing some common tasks, such as sorting,
 * filtering, joining multiple iterators, or defining a mapping involving all
 * elements of a traversal. A neat trick is to:
 * 
 * <pre>import static lawu.util.iterator.Iterators.*;</pre>
 * 
 * ...and then you can <code>map()</code>, <code>grep()</code>, or
 * <code>join()</code> to your heart's desire. You won't win any code brevity
 * contests (this is still Java), but you might make a few Perl hackers smile.
 * (Or maybe you'll just annoy everyone.)
 * </p>
 */

/**
 * <p>
 * Utility methods for manipulating iterators and iterable structures. The
 * ultimate goal is to have an intuitive interface for straightforwardly dealing
 * with all kinds of traversals.
 * </p>
 * <p>
 * All iterators returned by this class are {@link UniversalIterator}s, which
 * means that you can use them whether you need an {@link Iterable}, a
 * {@linkplain java.util.Iterator Java iterator}, a {@linkplain Iterator Gang of
 * Four iterator}, or even an {@link Enumeration}. Adapter methods are also
 * provided for converting any of these interfaces to
 * <code>UniversalIterator</code>s.
 * </p>
 */
public class Iterators {
	/**
	 * There is no need to instantiate this class.
	 */
	private Iterators() {
	}

	/**
	 * Returns an iterator over the passed elements or element array.
	 * 
	 * @param <T>
	 *            type of elements
	 * @param array
	 *            the elements over which to iterate
	 * @return an iterator over the elements
	 * @see #iterator(List)
	 */
	public static <T> UniversalIterator<T> iterator(T... array) { // TODO reimplement reversibility
		return new ArrayIterator<T>(array);
	}

	/**
	 * Returns an iterator over the list.
	 * 
	 * @param <T>
	 *            type of list elements
	 * @param list
	 *            the list over which to iterate
	 * @return an iterator over the list
	 * @see #iterator(Object...)
	 */
	public static <T> UniversalIterator<T> iterator(List<T> list) { // TODO reimplement reversibility
		return new JListIterator<T>(list);
	}

//	/**
//	 * Adapts a Gang of Four iterator to a <code>UniversalIterator</code>.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterator
//	 *            the adaptee
//	 * @return a universal iterator
//	 * @see #adapt(Enumeration)
//	 * @see #adapt(Iterable)
//	 * @see #adapt(java.util.Iterator)
//	 */
//	public static <T> UniversalIterator<T> adapt(Iterator<T> iterator) {
//		return iterator instanceof UniversalIterator<?> ? (UniversalIterator<T>) iterator : new GOFIteratorAdapter<T>(iterator);
//	}
//
//	/**
//	 * Adapts an <code>Iterable</code> to a <code>UniversalIterator</code>.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterable
//	 *            the adaptee
//	 * @return a universal iterator
//	 * @see #adapt(Enumeration)
//	 * @see #adapt(java.util.Iterator)
//	 * @see #adapt(Iterator)
//	 */
//	public static <T> UniversalIterator<T> adapt(Iterable<T> iterable) {
//		return iterable instanceof UniversalIterator<?> ? (UniversalIterator<T>) iterable : new JIteratorAdapter<T>(iterable);
//	}
//
//	/**
//	 * Adapts a Java <code>Iterator</code> to a <code>UniversalIterator</code>.
//	 * Because Java iterators do not provide a <code>reset()</code>-like method,
//	 * the returned iterator will not be resettable.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterator
//	 *            the adaptee
//	 * @return a universal iterator
//	 * @see #adapt(Enumeration)
//	 * @see #adapt(Iterable)
//	 * @see #adapt(Iterator)
//	 */
//	public static <T> UniversalIterator<T> adapt(java.util.Iterator<T> iterator) {
//		return iterator instanceof UniversalIterator<?> ? (UniversalIterator<T>) iterator : new JIteratorAdapter<T>(iterator);
//	}

	/**
	 * Returns its argument. This method is rather useless. It's provided for
	 * orthogonality.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param iterator
	 *            the "adaptee"
	 * @return the argument
	 * @see #adapt(Enumeration)
	 * @see #adapt(Iterable)
	 * @see #adapt(java.util.Iterator)
	 * @see #adapt(Iterator)
	 */
	public static <T> UniversalIterator<T> adapt(UniversalIterator<T> iterator) {
		return iterator;
	}

//	/**
//	 * Adapts an <code>Enumeration</code> to a <code>UniversalIterator</code>.
//	 * Because <code>Enumeration</code>s do not provide a <code>reset()</code>
//	 * -like method, the returned iterator will not be resettable.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param enumeration
//	 *            the adaptee
//	 * @return a universal iterator
//	 * @see #adapt(Iterable)
//	 * @see #adapt(java.util.Iterator)
//	 * @see #adapt(Iterator)
//	 */
//	public static <T> UniversalIterator<T> adapt(Enumeration<T> enumeration) {
//		return enumeration instanceof UniversalIterator<?> ? (UniversalIterator<T>) enumeration : new JEnumerationAdapter<T>(enumeration);
//	}
//
//	/**
//	 * Returns an iterator over the nodes in the node list.
//	 * 
//	 * @param list
//	 *            the node list
//	 * @return a node iterator
//	 */
//	public static ReversibleIterator<Node> iterator(NodeList list) {
//		return new NodeListIterator(list);
//	}
//
//	/**
//	 * Returns an iterator over the children of the node.
//	 * 
//	 * @param node
//	 *            parent of the nodes over which to iterate
//	 * @return a node iterator
//	 */
//	public static ReversibleIterator<Node> children(Node node) {
//		return iterator(node.getChildNodes());
//	}
//
//	/**
//	 * Returns an iterator over the given file's subfiles. A non-directory will
//	 * not crash this method, it will simply result in an iterator with no
//	 * elements. The iterator gives files in lexicographical order based on
//	 * path, in a system-dependent manner (case-sensitive on Windows,
//	 * case-insensitive on Unix).
//	 * 
//	 * @param file
//	 *            parent of the nodes over which to iterate
//	 * @return a file iterator
//	 */
//	public static ReversibleIterator<File> children(File file) {
//		File[] files = file.listFiles();
//		if(files == null)
//			files = new File[0];
//		Arrays.sort(files);
//		return iterator(files);
//	}
//	
//	/**
//	 * Returns an iterator over the characters of the specified character
//	 * sequence.
//	 * 
//	 * @param sequence
//	 *            the character sequence
//	 * @return a character iterator
//	 * @see #chars(Iterator)
//	 */
//	public static ReversibleIterator<Character> chars(CharSequence sequence) {
//		return new CharacterIterator(sequence);
//	}
//
//	/**
//	 * Returns an iterator over the characters of the specified character
//	 * sequences. This might be useful in combination with the
//	 * <code>lines()</code> family of methods because it allows code like
//	 * {@code chars(lines(System.in))} (assuming you've statically imported the
//	 * needed methods). 
//	 * 
//	 * @param sequences
//	 *            an iterator of character sequences
//	 * @return a character iterator
//	 * @see #chars(CharSequence)
//	 */
//	public static UniversalIterator<Character> chars(Iterator<? extends CharSequence> sequences) {
//		return join(map(new Mapper<CharSequence,Iterator<Character>>() {
//			@Override
//			public Iterator<Character> map(CharSequence sequence) {
//				return chars(sequence);
//			}
//		}, sequences));
//	}	
//
//	/**
//	 * Returns an iterator over the capturing groups of the specified
//	 * <code>MatchResult</code>
//	 * 
//	 * @param match
//	 *            the match result
//	 * @return a match result iterator
//	 */
//	public static ReversibleIterator<String> iterator(MatchResult match) {
//		return new MatchResultIterator(match);
//	}
//
//	/**
//	 * Returns an iterator over the lines of the specified <code>Reader</code>.
//	 * 
//	 * @param reader
//	 *            the input source
//	 * @return a line iterator
//	 * @see Streams#slurp(Reader)
//	 * @see #lines(BufferedReader)
//	 * @see #lines(File)
//	 * @see #lines(FileDescriptor)
//	 * @see #lines(InputStream)
//	 * @see #lines(Scanner)
//	 * @see #lines(URL)
//	 */
//	public static UniversalIterator<String> lines(Reader reader) {
//		return new StreamIterator(reader);
//	}
//
//	/**
//	 * Returns an iterator over the lines of the specified
//	 * <code>BufferedReader</code>.
//	 * 
//	 * @param reader
//	 *            the input source
//	 * @return a line iterator
//	 * @see Streams#slurp(Reader)
//	 * @see #lines(File)
//	 * @see #lines(FileDescriptor)
//	 * @see #lines(InputStream)
//	 * @see #lines(Reader)
//	 * @see #lines(Scanner)
//	 * @see #lines(URL)
//	 */
//	public static UniversalIterator<String> lines(BufferedReader reader) {
//		return new StreamIterator(reader);
//	}
//
//	/**
//	 * Returns an iterator over the lines of the specified
//	 * <code>InputStream</code>.
//	 * 
//	 * @param stream
//	 *            the input source
//	 * @return a line iterator
//	 * @see Streams#slurp(InputStream)
//	 * @see #lines(BufferedReader)
//	 * @see #lines(File)
//	 * @see #lines(FileDescriptor)
//	 * @see #lines(Reader)
//	 * @see #lines(Scanner)
//	 * @see #lines(URL)
//	 */
//	public static UniversalIterator<String> lines(InputStream stream) {
//		return new StreamIterator(stream);
//	}
//
//	/**
//	 * Returns an iterator over the lines of the specified file descriptor.
//	 * 
//	 * @param fd
//	 *            the input source
//	 * @return a line iterator
//	 * @see Streams#slurp(FileDescriptor)
//	 * @see #lines(BufferedReader)
//	 * @see #lines(File)
//	 * @see #lines(InputStream)
//	 * @see #lines(Reader)
//	 * @see #lines(Scanner)
//	 * @see #lines(URL)
//	 */
//	public static UniversalIterator<String> lines(FileDescriptor fd) {
//		return new StreamIterator(fd);
//	}
//
//	/**
//	 * Returns an iterator over the lines of the specified file.
//	 * 
//	 * @param file
//	 *            the input source
//	 * @return a line iterator
//	 * @throws FileNotFoundException
//	 *             if the file can't be opened for reading
//	 * @see Streams#slurp(File)
//	 * @see #lines(BufferedReader)
//	 * @see #lines(FileDescriptor)
//	 * @see #lines(InputStream)
//	 * @see #lines(Reader)
//	 * @see #lines(Scanner)
//	 * @see #lines(URL)
//	 */
//	public static UniversalIterator<String> lines(File file) throws FileNotFoundException {
//		return new StreamIterator(file);
//	}

//	/**
//	 * Returns an iterator over the lines of the resource at the specified URL.
//	 * 
//	 * @param url
//	 *            the input source
//	 * @return a line iterator
//	 * @throws IOException
//	 *             if an I/O exception occurs
//	 * @see Streams#slurp(URL)
//	 * @see #lines(BufferedReader)
//	 * @see #lines(File)
//	 * @see #lines(FileDescriptor)
//	 * @see #lines(InputStream)
//	 * @see #lines(Reader)
//	 * @see #lines(Scanner)
//	 */
//	public static UniversalIterator<String> lines(URL url) throws IOException {
//		return new StreamIterator(url);
//	}
//
//	/**
//	 * Returns an iterator over the lines of the specified <code>Scanner</code>.
//	 * 
//	 * @param scanner
//	 *            the input source
//	 * @return a line iterator
//	 * @see #lines(BufferedReader)
//	 * @see #lines(File)
//	 * @see #lines(FileDescriptor)
//	 * @see #lines(InputStream)
//	 * @see #lines(Reader)
//	 * @see #lines(URL)
//	 */
//	public static UniversalIterator<String> lines(Scanner scanner) {
//		return new ScannerIterator(scanner);
//	}
//
//	/**
//	 * Returns an iterator over all the files in the directory hierarchy with
//	 * the specified root. That is, this method is similar to the Unix
//	 * <code>find</code> utility: if the <code>File</code> given to this method
//	 * is a directory, the returned iterator will recurse through the directory
//	 * tree in a depth-first manner. If the argument represents a normal file,
//	 * the iterator will have a single element, the <code>File</code> itself. A
//	 * file's children will be traversed in lexicographical order based on path,
//	 * in a system-dependent manner (case-sensitive on Windows, case-insensitive
//	 * on Unix).
//	 * 
//	 * @param file
//	 *            root of the directory hierarchy to traverse
//	 * @return an iterator over the directory hierarchy
//	 */
//	public static UniversalIterator<File> tree(File file) {
//		return new FileHierarchyIterator(file);
//	}
//
//	/**
//	 * Returns an iterator over all the nodes in the node tree with the
//	 * specified root.
//	 * 
//	 * @param node
//	 *            root of the node hierarchy to traverse
//	 * @return an iterator over the node hierarchy
//	 */
//	public static UniversalIterator<Node> tree(Node node) {
//		return new NodeHierarchyIterator(node);
//	}
//
//	/**
//	 * Maps the elements of a traversal using the specified mapping function.
//	 * The mapping is done lazily, i.e. the backing mapper does not get to see
//	 * the elements of the mapped traversal until the returned iterator is
//	 * explicitly asked for an element. Defining a mapping function with side
//	 * effects might be a bad idea, but if you insist on applying it to all
//	 * elements without doing anything else, you may find the
//	 * {@link #traverse(Iterator)} method of use.
//	 * 
//	 * @param <T>
//	 *            domain of mapping function (type of the input traversal)
//	 * @param <U>
//	 *            range of mapping function (type over which the returned
//	 *            iterator iterates)
//	 * @param mapper
//	 *            mapping function
//	 * @param iterator
//	 *            traversal to map
//	 * @return an iterator that performs the same traversal as the input but
//	 *         which applies the mapping function to each element before
//	 *         returning it
//	 */
//	public static <T,U> UniversalIterator<U> map(Mapper<? super T,? extends U> mapper, Iterator<? extends T> iterator) {
//		return new MappingIterator<T,U>(mapper, iterator);
//	}
//
//	/**
//	 * Keeps only those elements of a traversal which pass the specified filter.
//	 * 
//	 * @param <T>
//	 *            type over which the returned iterator iterates
//	 * @param filter
//	 *            the filtering method
//	 * @param iterator
//	 *            the traversal to filter
//	 * @return an iterator that gives only those elements of the input which
//	 *         pass the specified filter
//	 * @see #grep(Filter, Iterator)
//	 */
//	public static <T> UniversalIterator<T> filter(Filter<? super T> filter, Iterator<? extends T> iterator) {
//		return new FilteredIterator<T>(filter, iterator);
//	}
//
//	/**
//	 * Unix-esque synonym for {@link #filter(Filter,Iterator)}.
//	 * 
//	 * @param <T>
//	 *            type over which the returned iterator iterates
//	 * @param filter
//	 *            the filtering method
//	 * @param iterator
//	 *            the traversal to filter
//	 * @return an iterator that gives only those elements of the input which
//	 *         pass the specified filter
//	 */
//	public static <T> UniversalIterator<T> grep(Filter<? super T> filter, Iterator<? extends T> iterator) {
//		return filter(filter, iterator);
//	}
//
//	/**
//	 * Unfolds an iterator of iterators by joining the elements of its elements
//	 * into a single iterator.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterator
//	 *            the iterator to unfold
//	 * @return an iterator over all the elements of the given iterators
//	 * @see #join(Iterator...)
//	 */
//	public static <T> UniversalIterator<T> join(Iterator<? extends Iterator<? extends T>> iterator) {
//		return new JoiningIterator<T>(iterator);
//	}
//
//	/**
//	 * Joins the specified iterators into a single one.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterators
//	 *            the iterators to join
//	 * @return an iterator over all the elements of the given iterators
//	 * @see #join(Iterator)
//	 */
//	public static <T> UniversalIterator<T> join(Iterator<? extends T>... iterators) {
//		return join(iterator(iterators));
//	}

	/**
	 * Orders a traversal using the default comparison method.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param iterator
	 *            the traversal to sort
	 * @return an iterator which gives the same elements as the input but sorted
	 *         in order according to the default comparison method of the input
	 *         type
	 * @see #sort(Comparator,Iterator)
	 */
	public static <T extends Comparable<T>> UniversalIterator<T> sort(Iterator<? extends T> iterator) {
		List<T> list = list(iterator);
		Collections.sort(list);
		return iterator(list);
	}

	/**
	 * Orders a traversal using the specified comparison method.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param comparator
	 *            the comparison method
	 * @param iterator
	 *            the traversal to sort
	 * @return an iterator which gives the same elements as the input but sorted
	 *         in order according to the given comparison method
	 * @see #sort(Iterator)
	 */
	public static <T> UniversalIterator<T> sort(Comparator<? super T> comparator, Iterator<? extends T> iterator) { // TODO reimplement reversibility
		List<T> list = list(iterator);
		Collections.sort(list, comparator);
		return iterator(list);
	}

	/**
	 * Randomly permutes the elements of a traversal.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param iterator
	 *            the traversal to permute
	 * @param rnd
	 *            the source of randomness, or <code>null</code> to use a
	 *            default source
	 * @return an iterator which gives the same elements as the input but
	 *         randomly permuted
	 * @see #shuffle(Iterator)
	 */
	public static <T> UniversalIterator<T> shuffle(Iterator<? extends T> iterator, Random rnd) { // TODO reimplement reversibility
		List<T> list = list(iterator);
		if(rnd == null)
			Collections.shuffle(list);
		else
			Collections.shuffle(list, rnd);
		return iterator(list);
	}

	/**
	 * Randomly permutes the elements of a traversal. This method is equivalent
	 * to calling
	 * <code>Iterators.{@linkplain #shuffle(Iterator, Random) shuffle}(iterator, null)</code>.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param iterator
	 *            the traversal to permute
	 * @return an iterator which gives the same elements as the input but
	 *         randomly permuted
	 */
	public static <T> UniversalIterator<T> shuffle(Iterator<? extends T> iterator) { // TODO reimplement reversibility
		return shuffle(iterator, null);
	}
	
	/**
	 * Returns a list containing the elements of an iterator.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param iterator
	 *            the iterator to transform to a list
	 * @return a list containing the same elements as the iterator
	 */
	public static <T> List<T> list(Iterator<? extends T> iterator) {
		List<T> list = new ArrayList<T>();
		for(iterator.reset(); !iterator.isDone(); iterator.advance())
			list.add(iterator.current());
		return list;
	}

	/**
	 * Returns a copy of the given iterator, that is, another iterator
	 * containing the same elements. This is useful if you have an iterator
	 * that's backed by another structure (e.g. a list) that might be modified
	 * while the iterator is in existence. Note that this method requires
	 * traversing the input.
	 * 
	 * @param <T>
	 *            type over which the iteration takes place
	 * @param iterator
	 *            the iterator to copy
	 * @return an iterator containing the same elements as the input
	 */
	public static <T> UniversalIterator<T> copy(Iterator<? extends T> iterator) { // TODO reimplement reversibility
		return iterator(list(iterator));
	}

//	/**
//	 * Returns an iterator that continuously loops over the elements of a
//	 * traversal. This does not immediately traverse the elements of the input,
//	 * so it's safe to call with never-ending iterators (though it would serve
//	 * no purpose).
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterator
//	 *            the iterator to traverse
//	 * @return an iterator that continuously loops over the elements of the
//	 *         input
//	 */
//	public static <T> UniversalIterator<T> loop(Iterator<T> iterator) {
//		return new ContinuousIterator<T>(iterator);
//	}	
	
	/**
	 * Traverses all the elements of an iterator from the beginning, doing
	 * nothing. This might be useful for forcing evaluation of lazy methods,
	 * such as {@link #map(Mapper,Iterator)}.
	 * 
	 * @param iterator
	 *            the iterator to traverse
	 */
	public static void traverse(Iterator<?> iterator) {
		try {
			iterator.reset();
		}
		catch(IllegalStateException e) {
		}
		while(!iterator.isDone())
			iterator.advance();
	}

//	/**
//	 * Reverses a traversal. Note that this will probably require actually going
//	 * through the entire traversal, so avoid calling with never-ending
//	 * iterators unless you like infinite loops.
//	 * 
//	 * @param <T>
//	 *            type over which the iteration takes place
//	 * @param iterator
//	 *            the traversal to reverse
//	 * @return an iterator which gives the same elements as the input but in
//	 *         reverse order
//	 */
//	public static <T> ReversibleIterator<T> reverse(Iterator<T> iterator) {
//		return (iterator instanceof ReversibleIterator<?> ? ((ReversibleIterator<T>) iterator) : copy(iterator)).reverse();
//	}
}
