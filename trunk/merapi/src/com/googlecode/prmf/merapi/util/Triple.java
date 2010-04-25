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
package com.googlecode.prmf.merapi.util;

/**
 * Groups three values in an immutable triple.
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            the first type in the triple
 * @param <U>
 *            the second type in the triple
 * @param <V>
 *            the third type in the triple
 * @see Pair
 * @see Quadruple
 */
public final class Triple<T,U,V> {
	private final T first;
	private final U second;
	private final V third;

	/**
	 * Constructs a triple of the three specified values.
	 * 
	 * @param first
	 *            the first value in the triple
	 * @param second
	 *            the second value in the triple
	 * @param third
	 *            the third value in the triple
	 */
	public Triple(T first, U second, V third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	/**
	 * Retrieves the first value in the triple.
	 * 
	 * @return the first value in the triple
	 */
	public T getFirst() {
		return this.first;
	}

	/**
	 * Retrieves the second value in the triple.
	 * 
	 * @return the second value in the triple
	 */
	public U getSecond() {
		return this.second;
	}

	/**
	 * Retrieves the third value in the triple.
	 * 
	 * @return the third value in the triple
	 */
	public V getThird() {
		return this.third;
	}
	
	/**
	 * Returns a string representation of the values in the triple.
	 */
	@Override
	public String toString() {
		return String.format("(%s, %s, %s)", this.first, this.second, this.third);
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(this == obj)
			ret = true;
		else if(obj instanceof Triple<?,?,?>) {
			Triple<?,?,?> pair = (Triple<?,?,?>) obj;
			ret = (this.first == null ? pair.first == null : this.first.equals(pair.first))
				&& (this.second == null ? pair.second == null : this.second.equals(pair.second))
				&& (this.third == null ? pair.third == null : this.third.equals(pair.third));
		}
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int f = this.first == null ? 0 : this.first.hashCode();
		int s = this.second == null ? 0 : this.second.hashCode();
		int t = this.third == null ? 0 : this.third.hashCode();
		return (f * prime + s) * prime + t;
	}
}
