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
package com.googlecode.prmf.merapi.net.event;

import com.googlecode.prmf.merapi.net.SocketClient;

/**
 * Raised when an incoming message is successfully read.
 * 
 * @author Miorel-Lucian Palii
 */
public class ReadingEvent extends MessageEvent {
	/**
	 * Constructs an incoming message event associated with the given client.
	 * 
	 * @param client
	 *            the client to associate with
	 * @param message
	 *            the incoming message
	 */
	public ReadingEvent(SocketClient client, String message) {
		super(client, message);
	}
	
	@Override
	protected void doTrigger(NetworkEventListener listener) {
		listener.reading(this);
	}
}
