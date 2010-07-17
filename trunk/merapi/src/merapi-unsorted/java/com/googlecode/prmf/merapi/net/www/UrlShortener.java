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

import java.io.IOException;

/**
 * Takes a URL and returns a (usually) shorter one that redirects to the same
 * resource.
 * 
 * @author Miorel-Lucian Palii
 */
public interface UrlShortener {
	/**
	 * Shortens the specified URL.
	 * 
	 * @param longUrl
	 *            the URL to shorten
	 * @return a shortened URL
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ApiException
	 *             if an error occurs while using an external API
	 */
	public String shorten(CharSequence longUrl) throws IOException, ApiException;
}
