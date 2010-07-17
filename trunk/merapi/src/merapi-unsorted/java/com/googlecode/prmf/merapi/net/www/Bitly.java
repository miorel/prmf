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

import com.googlecode.prmf.merapi.json.JsonException;
import com.googlecode.prmf.merapi.json.JsonNumber;
import com.googlecode.prmf.merapi.json.JsonObject;
import com.googlecode.prmf.merapi.json.JsonParser;
import com.googlecode.prmf.merapi.json.JsonString;
import com.googlecode.prmf.merapi.json.JsonValue;
import com.googlecode.prmf.merapi.util.Strings;

/**
 * A {@link UrlShortener} that supports services powered by <a
 * href="http://bit.ly/">bit.ly</a>. Now using version 3.0 of the API!
 *
 * @author Miorel-Lucian Palii
 */
public class Bitly extends AbstractUrlShortener {
	private static final int STATUS_CODE_OK = 200;

	private final String login;
	private final String apiKey;
	private final String domain;

	private final JsonParser parser;

	/**
	 * Constructs a new URL shortener using the specified login and API key,
	 * which will return short URLs in the default domain.
	 *
	 * @param login
	 *            the <code>bit.ly</code> login
	 * @param apiKey
	 *            the <code>bit.ly</code> API key
	 */
	public Bitly(String login, String apiKey) {
		this(login, apiKey, "bit.ly");
	}

	/**
	 * Constructs a new URL shortener using the specified login and API key,
	 * which will return short URLs in the specified domain.
	 *
	 * @param login
	 *            the <code>bit.ly</code> login
	 * @param apiKey
	 *            the <code>bit.ly</code> API key
	 * @param domain
	 *            the domain of short URLs returned by this shortener
	 */
	public Bitly(String login, String apiKey, String domain) {
		if(login == null)
			throw new NullPointerException("The login may not be null.");
		if(login.isEmpty())
			throw new IllegalArgumentException("The login may not be zero length.");
		if(apiKey == null)
			throw new NullPointerException("The API key may not be null.");
		if(apiKey.isEmpty())
			throw new IllegalArgumentException("The API key may not be zero length.");
		if(domain == null)
			throw new NullPointerException("The domain may not be null.");
		if(domain.isEmpty())
			throw new IllegalArgumentException("The domain may not be zero length.");
		this.login = login;
		this.apiKey = apiKey;
		this.domain = domain;
		this.parser = new JsonParser();
	}

	/**
	 * Returns this shortener's login.
	 *
	 * @return the <code>bit.ly</code> login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * Returns this shortener's API key.
	 *
	 * @return the <code>bit.ly</code> API key
	 */
	public String getApiKey() {
		return this.apiKey;
	}

	/**
	 * Returns the domain of this shortener's URLs.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return this.domain;
	}

	@Override
	protected String getRequestUrl(String longUrl) {
		return String.format("http://api.bit.ly/v3/shorten?uri=%s&format=json&login=%s&apiKey=%s&domain=%s",
				Strings.encodeUtf8(longUrl), Strings.encodeUtf8(this.login), Strings.encodeUtf8(this.apiKey), Strings.encodeUtf8(this.domain));
	}

	@Override
	protected String parseServerResponse(CharSequence serverResponse) throws ApiException {
		// Parse JSON.
		JsonValue val;
		try {
			val = this.parser.parse(serverResponse);
		}
		catch(JsonException e) {
			throw new ApiException("Failed to parse the server response as JSON.", e);
		}

		// Convert to a JSON object.
		if(!(val instanceof JsonObject))
			throw new ApiException("The server response was not a JSON object.");
		JsonObject jo = (JsonObject) val;

		String shortUrl;
		try {
			// Report any errors.
			int statusCode = ((JsonNumber) jo.get("status_code")).intValue();
			if(statusCode != STATUS_CODE_OK) {
				String statusTxt = ((JsonString) jo.get("status_txt")).getValue();
				throw new ApiException("API error: " + statusCode + " " + statusTxt);
			}

			// Set the result.
			shortUrl = ((JsonString) jo.get("data", "url")).getValue();
		}
		catch(RuntimeException e) {
			throw new ApiException("The server response did not have the expected structure.", e);
		}

		if(shortUrl == null) // It shouldn't be null.
			throw new ApiException("The server response did not have the expected structure.");

		return shortUrl;
	}
}
