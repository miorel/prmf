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

import java.util.Scanner;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * A line iterator that reads from a {@link Scanner}.
 * 
 * @author Miorel-Lucian Palii
 * @see Iterators#lines(Scanner)
 */
public class ScannerIterator extends LineIterator {
	private final Scanner scanner;

	/**
	 * Constructs an iterator that reads from the specified scanner.
	 * 
	 * @param scanner
	 *            the input source
	 */
	public ScannerIterator(Scanner scanner) {
		if(scanner == null)
			throw new NullPointerException("Can't read lines from null scanner.");
		this.scanner = scanner;
		init();
	}

	/**
	 * Reads and returns the contents of the next line, or <code>null</code> if
	 * there are no more lines.
	 * 
	 * @return the contents of the next line, or <code>null</code> if there are
	 *         no more lines
	 */
	@Override
	protected String getNextLine() {
		return this.scanner.hasNextLine() ? this.scanner.nextLine() : null;
	}
}
