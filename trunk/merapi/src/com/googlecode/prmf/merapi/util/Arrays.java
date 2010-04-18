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
 * Various methods for manipulating arrays.
 * 
 * @see java.util.Arrays
 */
public class Arrays {
	/**
	 * There is no need to instantiate this class.
	 */
	private Arrays() {
	}

	/**
	 * Boxes a <code>double</code> array as a {@link Double} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Double[] box(double[] array) {
		int n = array.length;
		Double[] ret = new Double[n];
		while(--n >= 0)
			ret[n] = Double.valueOf(array[n]);
		return ret;
	}

	/**
	 * Boxes a <code>short</code> array as a {@link Short} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Short[] box(short[] array) {
		int n = array.length;
		Short[] ret = new Short[n];
		while(--n >= 0)
			ret[n] = Short.valueOf(array[n]);
		return ret;
	}

	/**
	 * Boxes a <code>float</code> array as a {@link Float} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Float[] box(float[] array) {
		int n = array.length;
		Float[] ret = new Float[n];
		while(--n >= 0)
			ret[n] = Float.valueOf(array[n]);
		return ret;
	}

	/**
	 * Boxes a <code>byte</code> array as a {@link Byte} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Byte[] box(byte[] array) {
		int n = array.length;
		Byte[] ret = new Byte[n];
		while(--n >= 0)
			ret[n] = Byte.valueOf(array[n]);
		return ret;
	}

	/**
	 * Boxes an <code>int</code> array as an {@link Integer} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Integer[] box(int[] array) {
		int n = array.length;
		Integer[] ret = new Integer[n];
		while(--n >= 0)
			ret[n] = Integer.valueOf(array[n]);
		return ret;
	}

	/**
	 * Boxes a <code>char</code> array as a {@link Character} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Character[] box(char[] array) {
		int n = array.length;
		Character[] ret = new Character[n];
		while(--n >= 0)
			ret[n] = Character.valueOf(array[n]);
		return ret;
	}

	/**
	 * Boxes a <code>long</code> array as a {@link Long} array.
	 * 
	 * @param array
	 *            the array to box
	 * @return a boxed copy of the array
	 */
	public static Long[] box(long[] array) {
		int n = array.length;
		Long[] ret = new Long[n];
		while(--n >= 0)
			ret[n] = Long.valueOf(array[n]);
		return ret;
	}
}
