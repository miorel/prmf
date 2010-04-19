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

import com.googlecode.prmf.merapi.util.Iterators;

/**
 * An iterator over the characters of a {@link CharSequence}.
 * 
 * @see Iterators#chars(CharSequence)
 */
public class CharacterIterator extends ListIterator<Character> {
	private final CharSequence sequence;

	/**
	 * Constructs an iterator over the characters of the specified
	 * <code>CharSequence</code>.
	 * 
	 * @param sequence
	 *            sequence over whose characters to iterate
	 */
	public CharacterIterator(CharSequence sequence) {
		this(sequence, false);
	}

	/**
	 * Constructs an iterator over the characters of the specified
	 * <code>CharSequence</code>, possibly in reverse order.
	 * 
	 * @param sequence
	 *            sequence over whose characters to iterate
	 * @param reverse
	 *            whether or not to reverse the traversal order
	 */
	protected CharacterIterator(CharSequence sequence, boolean reverse) {
		super(0, sequence.length(), reverse);
		this.sequence = sequence;
	}
	
	@Override
	protected Character get(int position) {
		return Character.valueOf(this.sequence.charAt(position));
	}

	@Override
	public ReversibleIterator<Character> reverse() {
		return new CharacterIterator(this.sequence, true);
	}
}
