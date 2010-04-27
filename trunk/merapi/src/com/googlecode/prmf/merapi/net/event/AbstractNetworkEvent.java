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

import com.googlecode.prmf.merapi.event.AbstractEvent;
import com.googlecode.prmf.merapi.net.SocketClient;

/**
 * A basic {@link NetworkEvent} implementation.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class AbstractNetworkEvent extends AbstractEvent<NetworkEventListener> implements NetworkEvent {
	private final SocketClient client;

	/**
	 * Constructs a network event associated with the given client.
	 * 
	 * @param client
	 *            the client to associate with
	 */
	public AbstractNetworkEvent(SocketClient client) {
		if(client == null)
			throw new NullPointerException("The client may not be null.");
		this.client = client;
	}
	
	@Override
	public SocketClient getClient() {
		return this.client;
	}
}
