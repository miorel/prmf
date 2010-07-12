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
 * A {@link NetworkEvent} that comes with a message!
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class MessageEvent extends AbstractNetworkEvent {
	private final String message;

	/**
	 * Constructs a message event associated with the given client which will
	 * hold the specified message.
	 * 
	 * @param client
	 *            the client to associate with
	 * @param message
	 *            the message to hold
	 */
	public MessageEvent(SocketClient client, String message) {
		super(client);
		if(message == null)
			throw new NullPointerException("The message may not be null.");
		this.message = message;
	}

	/**
	 * Returns the message associated with this event.
	 * 
	 * @return the message associated with this event
	 */
	public String getMessage() {
		return this.message;
	}
}
