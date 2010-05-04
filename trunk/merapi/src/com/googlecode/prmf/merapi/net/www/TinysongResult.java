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

/**
 * Result of a <a href="http://tinysong.com/">Tinysong</a> search.
 *
 * @author Miorel-Lucian Palii
 * @see Tinysong
 */
public class TinysongResult {
	private final String songName;
	private final long songId;
	private final String artistName;
	private final long artistId;
	private final String albumName;
	private final long albumId;
	private final String url;

	/**
	 * Constructs a Tinysong search result with the specified fields.
	 *
	 * @param songName
	 *            the name of the song
	 * @param songId
	 *            the song ID
	 * @param artistName
	 *            the name of the artist
	 * @param artistId
	 *            the artist ID
	 * @param albumName
	 *            the name of the album
	 * @param albumId
	 *            the album ID
	 * @param url
	 *            a short URL
	 */
	public TinysongResult(String songName, long songId, String artistName, long artistId, String albumName, long albumId, String url) {
		if(songName == null)
			throw new NullPointerException("The song name may not be null.");
		if(songId < 0)
			throw new IllegalArgumentException("The song ID may not be negative.");
		if(artistName == null)
			throw new NullPointerException("The artist name may not be null.");
		if(artistId < 0)
			throw new IllegalArgumentException("The artist ID may not be negative.");
		if(albumName == null)
			throw new NullPointerException("The album name may not be null.");
		if(albumId < 0)
			throw new IllegalArgumentException("The album ID may not be negative.");
		if(url == null)
			throw new NullPointerException("The URL may not be null.");
		this.songName = songName;
		this.songId = songId;
		this.artistName = artistName;
		this.artistId = artistId;
		this.albumName = albumName;
		this.albumId = albumId;
		this.url = url;
	}

	/**
	 * Returns the name of the song.
	 *
	 * @return the name of the song.
	 */
	public String getSongName() {
		return this.songName;
	}

	/**
	 * Returns the song ID.
	 *
	 * @return the song ID
	 */
	public long getSongId() {
		return this.songId;
	}

	/**
	 * Returns the name of the artist.
	 *
	 * @return the name of the artist
	 */
	public String getArtistName() {
		return this.artistName;
	}

	/**
	 * Returns the artist ID.
	 *
	 * @return the artist ID
	 */
	public long getArtistId() {
		return this.artistId;
	}

	/**
	 * Returns the name of the album.
	 *
	 * @return the name of the album
	 */
	public String getAlbumName() {
		return this.albumName;
	}

	/**
	 * Returns the album ID.
	 *
	 * @return the album ID
	 */
	public long getAlbumId() {
		return this.albumId;
	}

	/**
	 * Returns the short URL corresponding to this result.
	 *
	 * @return a short URL
	 */
	public String getUrl() {
		return this.url;
	}

	@Override
	public String toString() {
		return String.format("%s by %s on %s <%s>", this.songName, this.artistName, this.albumName, this.url);
	}
}
