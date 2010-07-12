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
package com.googlecode.prmf.merapi.net.www;

import java.io.IOException;

/**
 * <p>
 * A partial {@link UrlShortener} implementation.
 * </p>
 * <p>
 * The typical <code>UrlShortener</code> works by talking to a server. The URL
 * to be shortened is passed to the server, often as part of a query string. The
 * shortened URL is then parsed from the server response.
 * </p>
 * <p>
 * Aside from the specifics of the request URL used to communicate to the server
 * and the format of the response, the above is very similar among many URL
 * shortening APIs. This class performs the common steps, so implementations
 * need only provide {@link #getRequestUrl(String)} and
 * {@link #parseServerResponse(CharSequence)}. (Services with a text-only
 * response might even be able to make do with the default
 * <code>parseServerResponse(CharSequence)</code>).
 * </p>
 *
 * @author Miorel-Lucian Palii
 */
public abstract class AbstractUrlShortener implements UrlShortener {
	private final ApiClient client;

	/**
	 * Default constructor.
	 */
	public AbstractUrlShortener() {
		this.client = new ApiClient();
	}

	@Override
	public String shorten(CharSequence longUrl) throws IOException, ApiException {
		// Get the long URL as a string.
		String longUrlStr = longUrl.toString();
		if(longUrlStr.indexOf("://") < 0) // Some services will croak if we don't do this.
			longUrlStr = "http://" + longUrlStr;

		// Get the request URL.
		String requestUrl = getRequestUrl(longUrlStr);

		// Get the server response.
		CharSequence serverResponse = this.client.getContent(requestUrl);

		// Parse it!
		return parseServerResponse(serverResponse);
	}

	/**
	 * Forms a request URL asking the service to shorten the argument.
	 *
	 * @param longUrl
	 *            the URL to shorten
	 * @return a URL for the shortening request
	 */
	protected abstract String getRequestUrl(String longUrl);

	/**
	 * <p>
	 * Parses a shortened URL from the server response.
	 * </p>
	 * <p>
	 * The default implementation interprets the entire server response as a
	 * shortened URL.
	 * </p>
	 *
	 * @param serverResponse
	 *            the server response
	 * @return a shortened URL
	 * @throws ApiException
	 *             if the server response indicates an error or has an
	 *             unexpected structure
	 */
	protected String parseServerResponse(CharSequence serverResponse) throws ApiException {
		return serverResponse.toString().trim();
	}
}
