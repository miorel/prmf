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
package com.googlecode.prmf.merapi.math;

import java.math.BigInteger;

import com.googlecode.prmf.merapi.util.iterators.AbstractUniversalIterator;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * Number theory utilities.
 * 
 * @author Miorel-Lucian Palii
 */
public class NumberTheory {
	/**
	 * Returns an iterator over numbers which are probably prime. This iterator
	 * is guaranteed not to skip any primes, but it may give some composite
	 * numbers: the probability that any number iterated over is composite does
	 * not exceed 2<sup>-100</sup>.
	 * 
	 * @return an iterator over probable primes
	 * @see BigInteger#isProbablePrime(int)
	 * @see BigInteger#nextProbablePrime()
	 */
	public static UniversalIterator<BigInteger> probablePrimes() {
		UniversalIterator<BigInteger> ret = new AbstractUniversalIterator<BigInteger>() {
			private BigInteger current;
			
			@Override
			public void advance() {
				this.current = this.current.nextProbablePrime();
			}

			@Override
			public BigInteger current() {
				return this.current;
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			public void reset() {
				this.current = BigInteger.valueOf(2);
			}	
		};
		ret.reset();
		return ret;
	}
}
