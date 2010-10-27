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
 * A {@link UrlShortener} that uses the <a href="http://j.mp/">j.mp</a> service,
 * powered by <a href="http://bit.ly/">bit.ly</a>. This effect can also be
 * accomplished by initializing a pure <code>Bitly</code> instance with the
 * <code>j.mp</code> domain.
 * 
 * @author Miorel-Lucian Palii
 * @see Bitly#Bitly(String,String,String)
 */
public class Jmp extends Bitly {
	/**
	 * Constructs a new URL shortener using the specified login and API key,
	 * which will return short URLs in the <code>j.mp</code> domain.
	 * 
	 * @param login
	 *            the <code>bit.ly</code> login
	 * @param apiKey
	 *            the <code>bit.ly</code> API key
	 */
	public Jmp(String login, String apiKey) {
		super(login, apiKey, "j.mp");
	}
	
}
