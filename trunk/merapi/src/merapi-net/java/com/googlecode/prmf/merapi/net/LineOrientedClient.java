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
package com.googlecode.prmf.merapi.net;

import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;

import com.googlecode.prmf.merapi.io.Registrar;

/**
 * A {@link SocketClient} that sends and receives lines.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class LineOrientedClient extends SocketClient {
	/**
	 * Builds a line-oriented client that will connect to the specified address,
	 * register with the specified registrar, and use the specified provider to
	 * open channels.
	 * 
	 * @param address
	 *            the address to connect to
	 * @param registrar
	 *            the registrar which will dictate network activity
	 * @param provider
	 *            the provider to use for opening channels, or <code>null</code>
	 *            to use the default provider
	 */
	public LineOrientedClient(SocketAddress address, Registrar registrar, SelectorProvider provider) {
		super(address, registrar, provider);
	}

	/**
	 * Builds a line-oriented client that will connect to the specified address,
	 * register with the specified registrar, and use a default provider to open
	 * channels.
	 * 
	 * @param address
	 *            the address to connect to
	 * @param registrar
	 *            the registrar which will dictate network activity
	 */
	public LineOrientedClient(SocketAddress address, Registrar registrar) {
		super(address, registrar);
	}

	@Override
	protected void raiseWritingEvent(String message) {
		super.raiseWritingEvent(message.substring(0, message.length() - 2));
	}
	
	@Override
	protected void send(CharSequence message) {
		super.send(message + "\r\n");
	}
	
	@Override
	protected void scanBuffer(StringBuilder buffer) {
		for(int i = 0; i < buffer.length(); ++i)
			if(isLineTerminator(buffer.charAt(i))) {
				String line = buffer.substring(0, i);
				do ++i;
				while(i < buffer.length() && isLineTerminator(buffer.charAt(i)));
				buffer.delete(0, i);
				i = -1;
				raiseReadingEvent(line);
				process(line);
			}
	}

	private boolean isLineTerminator(char c) {
		return c == '\r' || c == '\n';
	}

	/**
	 *Defines how this client handles incoming lines.
	 * 
	 * @param line
	 *            the incoming line
	 */
	protected abstract void process(String line);
}
