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

import java.util.Comparator;

/**
 * Object representation of Sir William Rowan Hamilton's <a
 * href="http://en.wikipedia.org/wiki/Quaternion">quaternions</a>.
 * 
 * @author Miorel-Lucian Palii
 * @see Vector
 */
public class Quaternion {
	private final double w;
	private final double x;
	private final double y;
	private final double z;

	/**
	 * A comparator that orders quaternions by their magnitude. There is no
	 * breaking of ties, so watch out for that if you need a stable sort.
	 */
	public static final Comparator<Quaternion> MAGNITUDE_ORDER = new Comparator<Quaternion>() {
		@Override
		public int compare(Quaternion a, Quaternion b) {
			return Double.valueOf(a.magnitude()).compareTo(Double.valueOf(b.magnitude()));
		}
	};
	
	/**
	 * Constructs a zero quaternion.
	 */
	public Quaternion() {
		this(0, 0, 0, 0);
	}

	/**
	 * Constructs a quaternion with the specified coordinates.
	 * 
	 * @param w
	 *            the w-coordinate
	 * @param x
	 *            the x-coordinate
	 * @param y
	 *            the y-coordinate
	 * @param z
	 *            the z-coordinate
	 */
	public Quaternion(double w, double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Constructs a pure imaginary (vector-only) quaternion that represents the
	 * specified vector.
	 * 
	 * @param vector
	 *            the vector to represent
	 */
	public Quaternion(Vector vector) {
		this(0, vector.getX(), vector.getY(), vector.getZ());
	}

	/**
	 * Constructs a real (scalar-only) quaternion that represents the specified
	 * scalar.
	 * 
	 * @param scalar
	 *            the scalar to represent
	 */
	public Quaternion(double scalar) {
		this(scalar, 0, 0, 0);
	}
	
	/**
	 * Returns the w-coordinate.
	 * 
	 * @return the w-coordinate
	 */
	public double getW() {
		return this.w;
	}

	/**
	 * Returns the x-coordinate.
	 * 
	 * @return the x-coordinate
	 */
	public double getX() {
		return this.x;
	}

	/**
	 * Returns the y-coordinate.
	 * 
	 * @return the y-coordinate
	 */
	public double getY() {
		return this.y;
	}

	/**
	 * Returns the z-coordinate.
	 * 
	 * @return the z-coordinate
	 */
	public double getZ() {
		return this.z;
	}
	
	/**
	 * Gets the vector portion of this quaternion.
	 * 
	 * @return a vector &lt;x, y, z&gt;
	 * @see #getScalar()
	 */
	public Vector getVector() {
		return new Vector(this.x, this.y, this.z);
	}
	
	/**
	 * Gets the scalar portion of this quaternion. This is a synonym for
	 * {@link #getW()}, included for orthogonality with {@link #getVector()}.
	 * 
	 * @return the w-coordinate
	 */
	public double getScalar() {
		return this.w;
	}
	
	/**
	 * Returns a new quaternion object which represents the result of adding
	 * this quaternion and the argument.
	 * 
	 * @param quaternion
	 *            the addend
	 * @return <code>this + quaternion</code>
	 */
	public Quaternion add(Quaternion quaternion) {
		return new Quaternion(this.w + quaternion.w, this.x + quaternion.x, this.y + quaternion.y, this.z + quaternion.z);
	}

	/**
	 * Returns a new quaternion object which represents the result of
	 * subtracting the argument from this quaternion.
	 * 
	 * @param quaternion
	 *            the subtrahend
	 * @return <code>this - quaternion</code>
	 */
	public Quaternion subtract(Quaternion quaternion) {
		return new Quaternion(this.w - quaternion.w, this.x - quaternion.x, this.y - quaternion.y, this.z - quaternion.z);
	}

	/**
	 * Returns a new quaternion object which represents the result of
	 * multiplying this quaternion by the scalar passed as an argument.
	 * 
	 * @param scalar
	 *            the factor
	 * @return <code>this * scalar</code>
	 */
	public Quaternion multiply(double scalar) {
		return new Quaternion(this.w * scalar, this.x * scalar, this.y * scalar, this.z * scalar);
	}

	/**
	 * Returns a new quaternion object which represents the result of
	 * multiplying this quaternion by the inverse of the scalar passed as an
	 * argument.
	 * 
	 * @param scalar
	 *            the divisor
	 * @return <code>this / scalar</code>
	 */
	public Quaternion divide(double scalar) {
		return new Quaternion(this.w / scalar, this.x / scalar, this.y / scalar, this.z / scalar);
	}
	
	/**
	 * Returns a new quaternion object which has the same magnitude as this one
	 * but the opposite direction.
	 * 
	 * @return <code>-this</code>
	 */
	public Quaternion negate() {
		return new Quaternion(-this.w, -this.x, -this.y, -this.z);
	}

	/**
	 * Computes the magnitude of this quaternion.
	 * 
	 * @return the magnitude of this quaternion
	 */
	public double magnitude() {
		return Math.hypot(Math.hypot(this.w, this.x), Math.hypot(this.y, this.z));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.w);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(this.z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * <p>
	 * Indicates whether another object is equal to this quaternion.
	 * </p>
	 * <p>
	 * To be equal, the object must also be a quaternion and have the same
	 * coordinates as this one (according to {@link Double#equals(Object)}).
	 * There are a couple of cases in which this could get you into trouble, a
	 * subtle one being positive vs. negative zero. A more obvious one would be
	 * rounding error. Therefore, in general, it would probably be more useful
	 * to do comparisons by looking at the magnitude of the difference of two
	 * quaternions.
	 * </p>
	 */
	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if(this == obj)
			ret = true;
		else if(obj instanceof Quaternion) {
			Quaternion q = (Quaternion) obj;
			ret = Double.valueOf(this.w).equals(Double.valueOf(q.w))
				&& Double.valueOf(this.x).equals(Double.valueOf(q.x))
				&& Double.valueOf(this.y).equals(Double.valueOf(q.y))
				&& Double.valueOf(this.z).equals(Double.valueOf(q.z));
		}
		return ret;
	}
	
	/**
	 * Returns a string representation of the quaternion.
	 */
	@Override
	public String toString() {
		return String.format("<%.3f, %.3f, %.3f, %.3f>", Double.valueOf(this.w), Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z));
	}
}
