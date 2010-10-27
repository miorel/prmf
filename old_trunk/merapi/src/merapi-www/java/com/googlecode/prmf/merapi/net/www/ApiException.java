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
package com.googlecode.prmf.merapi.net.www;

/**
 * Indicates that a problem occurred while accessing some external API.
 * 
 * @author Miorel-Lucian Palii
 */
public class ApiException extends Exception {
	private static final long serialVersionUID = -3768024211225356367L;

	/**
	 * Constructs a new external API exception with a <code>null</code> detail
	 * message.
	 */
	public ApiException() {
		super();
	}

	/**
	 * Constructs a new external API exception with the specified detail message
	 * and cause.
	 * 
	 * @param message
	 *            the detail message
	 * @param cause
	 *            the cause
	 */
	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new external API exception with the specified detail
	 * message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public ApiException(String message) {
		super(message);
	}

	/**
	 * Constructs a new external API exception with the specified cause.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ApiException(Throwable cause) {
		super(cause);
	}
}
