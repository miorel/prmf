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

import java.util.regex.MatchResult;

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over the captured groups of a {@link MatchResult}.
 * 
 * @author Miorel-Lucian Palii
 * @see Iterators#iterator(MatchResult)
 */
public class MatchResultIterator extends ListIterator<String> {
	private final MatchResult match;

	/**
	 * Constructs an iterator over the captured groups of the specified match
	 * result.
	 * 
	 * @param match
	 *            the match result
	 */
	public MatchResultIterator(MatchResult match) {
		this(match, false);
	}
	
	/**
	 * Constructs an iterator over the captured groups of the specified match
	 * result, possibly in reverse order.
	 * 
	 * @param match
	 *            the match result
	 * @param reverse
	 *            whether or not to reverse the traversal order
	 */
	protected MatchResultIterator(MatchResult match, boolean reverse) {
		super(1, match.groupCount() + 1, reverse);
		this.match = match;
	}

	@Override
	protected String get(int position) {
		return this.match.group(position);
	}

	@Override
	public ReversibleIterator<String> reverse() {
		return new MatchResultIterator(this.match, true);
	}
}
