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
package com.googlecode.prmf.merapi.math;

import java.util.Comparator;

/**
 * A displacement in three-dimensional space.
 * 
 * @author Miorel-Lucian Palii
 * @see Quaternion
 */
public class Vector {
	private final double x;
	private final double y;
	private final double z;

	/**
	 * A comparator that orders vectors by their magnitude. There is no breaking
	 * of ties, so watch out for that if you need a stable sort.
	 */
	public static final Comparator<Vector> MAGNITUDE_ORDER = new Comparator<Vector>() {
		@Override
		public int compare(Vector a, Vector b) {
			return Double.valueOf(a.magnitude()).compareTo(Double.valueOf(b.magnitude()));
		}
	};
	
	/**
	 * Constructs a vector with the specified coordinates.
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @param z
	 *            the z-coordinate
	 */
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a vector with the specified x- and y-coordinates and a
	 * z-coordinate of zero.
	 * 
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 */
	public Vector(double x, double y) {
		this(x, y, 0);
	}

	/**
	 * Constructs a zero vector.
	 */
	public Vector() {
		this(0, 0, 0);
	}

	/**
	 * Constructs a vector that has the same coordinates as the argument.
	 * 
	 * @param vector
	 *            the coordinates
	 */
	public Vector(Vector vector) {
		this(vector.x, vector.y, vector.z);
	}

	/**
	 * Retrieves the x-coordinate of this vector.
	 * 
	 * @return the x-coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Retrieves the y-coordinate of this vector.
	 * 
	 * @return the y-coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Retrieves the z-coordinate of this vector.
	 * 
	 * @return the z-coordinate
	 */
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Returns a new vector object which represents the result of adding this
	 * vector and the argument.
	 * 
	 * @param vector
	 *            the addend
	 * @return <code>this + vector</code>
	 */
	public Vector add(Vector vector) {
		return new Vector(this.x + vector.x, this.y + vector.y, this.z + vector.z);
	}

	/**
	 * Returns a new vector object which represents the result of subtracting
	 * the argument from this vector.
	 * 
	 * @param vector
	 *            the subtrahend
	 * @return <code>this - vector</code>
	 */
	public Vector subtract(Vector vector) {
		return new Vector(this.x - vector.x, this.y - vector.y, this.z - vector.z);
	}

	/**
	 * Returns a new vector object which represents the result of multiplying
	 * this vector by the scalar passed as an argument.
	 * 
	 * @param scalar
	 *            the factor
	 * @return <code>this * scalar</code>
	 */
	public Vector multiply(double scalar) {
		return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	/**
	 * Returns a new vector object which represents the result of multiplying
	 * this vector by the inverse of the scalar passed as an argument.
	 * 
	 * @param scalar
	 *            the divisor
	 * @return <code>this / scalar</code>
	 */
	public Vector divide(double scalar) {
		return new Vector(this.x / scalar, this.y / scalar, this.z / scalar);
	}
	
	/**
	 * Returns a new vector object which has the same magnitude as this one but
	 * the opposite direction. 
	 * 
	 * @return <code>-this</code>
	 */
	public Vector negate() {
		return new Vector(-this.x, -this.y, -this.z);
	}

	/**
	 * Computes the magnitude of this vector.
	 * 
	 * @return the magnitude of this vector
	 */
	public double magnitude() {
		return Math.hypot(Math.hypot(this.x, this.y), this.z);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long tmp;
		tmp = Double.doubleToLongBits(this.x);
		result = prime * result + (int) (tmp ^ (tmp >>> 32));
		tmp = Double.doubleToLongBits(this.y);
		result = prime * result + (int) (tmp ^ (tmp >>> 32));
		tmp = Double.doubleToLongBits(this.z);
		result = prime * result + (int) (tmp ^ (tmp >>> 32));
		return result;
	}

	/**
	 * <p>
	 * Indicates whether another object is equal to this vector.
	 * </p>
	 * <p>
	 * To be equal, the object must also be a vector and have the same
	 * coordinates as this one (according to {@link Double#equals(Object)}).
	 * There are a couple of cases in which this could get you into trouble, a
	 * subtle one being positive vs. negative zero. A more obvious one would be
	 * rounding error. Therefore, in general, it would probably be more useful
	 * to do comparisons by looking at the magnitude of the difference of two
	 * vectors.
	 * </p>
	 */
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(this == obj)
			ret = true;
		else if(obj instanceof Vector) {
			Vector v = (Vector) obj;
			ret = Double.valueOf(this.x).equals(Double.valueOf(v.x))
				&& Double.valueOf(this.y).equals(Double.valueOf(v.y))
				&& Double.valueOf(this.z).equals(Double.valueOf(v.z));
		}
		return ret;
	}
	
	/**
	 * Returns a string representation of the vector.
	 */
	@Override
	public String toString() {
		return String.format("<%.3f, %.3f, %.3f>", Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z));
	}
}
