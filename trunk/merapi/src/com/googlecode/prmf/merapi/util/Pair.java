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

/**
 * Groups two values in an immutable pair.
 * 
 * @param <T>
 *            the first type in the pair
 * @param <U>
 *            the second type in the pair
 * @see Triple
 * @see Quadruple
 */
public class Pair<T,U> {
	private final T first;
	private final U second;

	/**
	 * Constructs a pair of the two specified values.
	 * 
	 * @param first
	 *            the first value in the pair
	 * @param second
	 *            the second value in the pair
	 */
	public Pair(T first, U second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Retrieves the first value in the pair.
	 * 
	 * @return the first value in the pair
	 */
	public T getFirst() {
		return this.first;
	}

	/**
	 * Retrieves the second value in the pair.
	 * 
	 * @return the second value in the pair
	 */
	public U getSecond() {
		return this.second;
	}
	
	/**
	 * Returns a string representation of the values in the pair.
	 */
	@Override
	public String toString() {
		return String.format("(%s, %s)", this.first, this.second);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(this == obj)
			ret = true;
		else if(obj instanceof Pair<?,?>) {
			Pair<?,?> pair = (Pair<?,?>) obj;
			ret = (this.first == null ? pair.first == null : this.first.equals(pair.first))
				&& (this.second == null ? pair.second == null : this.second.equals(pair.second));
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int f = this.first == null ? 0 : this.first.hashCode();
		int s = this.second == null ? 0 : this.second.hashCode();
		return f * prime + s;
	}
}
