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
import java.util.ArrayList;
import java.util.List;

import com.googlecode.prmf.merapi.json.JsonArray;
import com.googlecode.prmf.merapi.json.JsonException;
import com.googlecode.prmf.merapi.json.JsonNumber;
import com.googlecode.prmf.merapi.json.JsonObject;
import com.googlecode.prmf.merapi.json.JsonParser;
import com.googlecode.prmf.merapi.json.JsonString;
import com.googlecode.prmf.merapi.json.JsonValue;
import com.googlecode.prmf.merapi.util.Iterators;
import com.googlecode.prmf.merapi.util.Strings;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * Programmatically search for music on <a
 * href="http://tinysong.com/">Tinysong</a>.
 *
 * @author Miorel-Lucian Palii
 * @see <a href="http://tinysong.com/api">Tinysong API</a>
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
	public String topUrl(CharSequence query) throws ApiException, IOException {
		JsonValue serverResponse = getResponse(String.format("http://tinysong.com/a/%s?format=json", Strings.encodeUtf8(query)));

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

	/**
	 * Searches Tinysong for the given query, returning the top result or
	 * <code>null</code> if the search produced no results.
	 *
	 * @param query
	 *            the search terms
	 * @return the top result, or <code>null</code> if there are no results
	 * @throws ApiException
	 *             if the server response indicates an error or has an
	 *             unexpected structure
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public TinysongResult topResult(CharSequence query) throws ApiException, IOException {
		JsonValue serverResponse = getResponse(String.format("http://tinysong.com/b/%s?format=json", Strings.encodeUtf8(query)));

		// Get the Tinysong result.
		TinysongResult result;
		if(serverResponse instanceof JsonObject) // Success!
			result = parseResult(serverResponse);
		else if(serverResponse instanceof JsonArray && ((JsonArray) serverResponse).isEmpty()) // No results.
			result = null;
		else
			throw new ApiException("The server response did not have the expected structure.");

		return result;
	}

	/**
	 * Searches Tinysong for the given query, returning an iterator over a
	 * default number of results.
	 *
	 * @param query
	 *            the search terms
	 * @return a result iterator
	 * @throws ApiException
	 *             if the server response indicates an error or has an
	 *             unexpected structure
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public UniversalIterator<TinysongResult> search(CharSequence query) throws ApiException, IOException {
		return search(query, 5); // The default is five according to the API.
	}

	/**
	 * Searches Tinysong for the given query, returning an iterator over up to
	 * <code>limit</code> results.
	 *
	 * @param query
	 *            the search terms
	 * @param limit
	 *            the maximum number of results; must be between 1 and 32, inclusive
	 * @return a result iterator
	 * @throws ApiException
	 *             if the server response indicates an error or has an
	 *             unexpected structure
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public UniversalIterator<TinysongResult> search(CharSequence query, int limit) throws ApiException, IOException {
		if(!(1 <= limit && limit <= 32))
			throw new IllegalArgumentException("The limit must be between 1 and 32, inclusive.");

		JsonValue serverResponse = getResponse(String.format("http://tinysong.com/s/%s?format=json&limit=%d",
				Strings.encodeUtf8(query), Integer.valueOf(limit)));

		// Get the Tinysong results.
		List<TinysongResult> results;
		if(serverResponse instanceof JsonArray) { // Success!
			results = new ArrayList<TinysongResult>();

			for(JsonValue val: (JsonArray) serverResponse)
				results.add(parseResult(val));
		}
		else
			throw new ApiException("The server response did not have the expected structure.");

		return Iterators.iterator(results);
	}

	private TinysongResult parseResult(JsonValue val) throws ApiException {
		TinysongResult result;
		try {
			JsonObject jo = (JsonObject) val;
			String songName = ((JsonString) jo.get("SongName")).getValue();
			long songId = ((JsonNumber) jo.get("SongID")).longValue();
			String artistName = ((JsonString) jo.get("ArtistName")).getValue();
			long artistId = ((JsonNumber) jo.get("ArtistID")).longValue();
			String albumName = ((JsonString) jo.get("AlbumName")).getValue();
			long albumId = ((JsonNumber) jo.get("AlbumID")).longValue();
			String url = ((JsonString) jo.get("Url")).getValue();
			result = new TinysongResult(songName, songId, artistName, artistId, albumName, albumId, url);
		}
		catch(RuntimeException e) {
			throw new ApiException("The server response did not have the expected structure.", e);
		}
		return result;
	}

	private JsonValue getResponse(String requestUrl) throws ApiException, IOException {
		JsonValue serverResponse;
		try {
			serverResponse = this.parser.parse(this.client.getContent(requestUrl));
		}
		catch(JsonException e) {
			throw new ApiException("Failed to parse the server response as JSON.", e);
		}
		return serverResponse;
	}
}
