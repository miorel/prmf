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
package com.googlecode.prmf.merapi.util;

/**
 * <p>
 * Defines a function (in the mathematical sense) for manipulating objects.
 * </p>
 * <p>
 * The mapping may be between the same, or different types. Regardless, a mapper
 * should generally not (intentionally) alter the objects presented to it but
 * rather create new ones.
 * </p>
 * 
 * @author Miorel-Lucian Palii
 * @param <T>
 *            the mapper's domain
 * @param <U>
 *            the mapper's range
 */
public interface Mapper<T,U> {
	/**
	 * Evaluates this mapper's function for the given object and returns the
	 * result. In other words, it &quot;maps&quot; the given object onto the
	 * result of applying this mapper's function to it.
	 * 
	 * @param object
	 *            the function's argument
	 * @return the result of applying the function to the argument
	 */
	public U map(T object);
}
