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
package com.googlecode.prmf.merapi.math;

import java.math.BigInteger;

/**
 * Some combinatorics formulas.
 */
public class Combinatorics {
	/**
	 * There is no need to instantiate this class.
	 */
	private Combinatorics() {
	}

	/**
	 * Computes the factorial of a number n, i.e.&nbsp;(n)(n - 1)(n -
	 * 2)...(2)(1).
	 * 
	 * @param n the number whose factorial to calculate
	 * @return n factorial
	 */
	public BigInteger factorial(int n) {
		if(n < 0)
			throw new IllegalArgumentException("Can't calculate the factorial of negative numbers.");
		BigInteger ret = BigInteger.ONE;
		for(int i = 1; i <= n; ++i)
			ret = ret.multiply(BigInteger.valueOf(i));
		return ret;
	}
}
