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

import com.googlecode.prmf.merapi.json.JsonArray;
import com.googlecode.prmf.merapi.json.JsonException;
import com.googlecode.prmf.merapi.json.JsonParser;
import com.googlecode.prmf.merapi.json.JsonString;
import com.googlecode.prmf.merapi.json.JsonValue;
import com.googlecode.prmf.merapi.util.Strings;

/**
 * Programmatically search for music on <a
 * href="http://tinysong.com/">Tinysong</a>.
 *
 * @author Miorel-Lucian Palii
 */
public class Tinysong {
	private final ApiClient client;
	private final JsonParser parser;

	/**
	 * Constructs a new <code>Tinysong</code> instance that can be used for
	 * searching music.
	 */
	public Tinysong() {
		this.client = new ApiClient();
		this.parser = new JsonParser();
	}

	/**
	 * Searches Tinysong for the given query, returning the short URL of the top
	 * result or <code>null</code> if the search produced no results.
	 *
	 * @param query
	 *            the search terms
	 * @return a short URL, or <code>null</code> if there are no results
	 * @throws ApiException
	 *             if the server response indicates an error or has an
	 *             unexpected structure
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public String search(CharSequence query) throws ApiException, IOException {
		// Prepare request URL.
		String requestUrl = String.format("http://tinysong.com/a/%s?format=json", Strings.encodeUtf8(query));

		// Get the server response as JSON.
		JsonValue serverResponse;
		try {
			serverResponse = this.parser.parse(this.client.getContent(requestUrl));
		}
		catch(JsonException e) {
			throw new ApiException("Failed to parse the server response as JSON.", e);
		}

		// Get the Tinysong URL.
		String tinysongUrl;
		if(serverResponse instanceof JsonString) // Success!
			tinysongUrl = ((JsonString) serverResponse).getValue();
		else if(serverResponse instanceof JsonArray && ((JsonArray) serverResponse).isEmpty()) // No results.
			tinysongUrl = null;
		else
			throw new ApiException("The server response did not have the expected structure.");

		return tinysongUrl;
	}
}
