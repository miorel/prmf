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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.googlecode.prmf.merapi.io.Streams;

/**
 * <p>
 * Gets content from URLs.
 * </p>
 * <p>
 * The name of this class isn't particularly informative, nor is its purpose all
 * that well defined. Basically, I created it to avoid some code duplication
 * when talking to various APIs. Hopefully this class will be improved in the
 * future.
 * </p>
 *
 * @author Miorel-Lucian Palii
 */
public class ApiClient {
	/**
	 * Default constructor.
	 */
	public ApiClient() {
	}

	/**
	 * Gets content from the specified location.
	 *
	 * @param url
	 *            the location whose content should be retrieved
	 * @return a buffer containing the content
	 * @throws ApiException
	 *             if the server carps but sends a useful response anyway; this
	 *             response will be included in the exception's detail message
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public CharSequence getContent(String url) throws ApiException, IOException {
		CharSequence ret = null;

		URLConnection conn = new URL(url).openConnection();
		try {
			ret = Streams.slurp(conn);
		}
		catch(IOException e) {
			String errMsg = null;

			// Try to extract an error message.
			if(conn instanceof HttpURLConnection) {
				InputStream errStream = ((HttpURLConnection) conn).getErrorStream();
				if(errStream != null)
					try {
						errMsg = Streams.slurp(errStream).toString();
					}
					catch(IOException ioe) {
						// We're already in error mode, this one can be ignored.
					}
			}

			// Report the error if message extraction was successful.
			if(errMsg != null)
				throw new ApiException("Server response: " + errMsg);

			// Otherwise propagate old error.
			throw e;
		}

		return ret;
	}
}
