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
package com.googlecode.prmf.merapi.net.irc.event;

import com.googlecode.prmf.merapi.event.AbstractEvent;
import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.cmd.IncomingIrcCommand;

/**
 * A skeleton IRC event implementation.
 * 
 * @author Miorel-Lucian Palii
 * @param <C>
 *            the type of command held
 */
public abstract class AbstractIrcEvent<C extends IncomingIrcCommand> extends AbstractEvent<IrcEventListener> implements IrcEvent<C> {
	private final IrcClient client;
	private final Entity origin;
	private final C command;

	/**
	 * Builds an event that associates the given client and origin entity with
	 * the specified command.
	 * 
	 * @param client
	 *            the client that received the command
	 * @param origin
	 *            the origin of the command
	 * @param command
	 *            the command to wrap
	 */
	public AbstractIrcEvent(IrcClient client, Entity origin, C command) {
		if(client == null)
			throw new NullPointerException("The client may not be null.");
		if(command == null)
			throw new NullPointerException("The command may not be null.");
		this.client = client;
		this.origin = origin;
		this.command = command;
	}

	@Override
	public IrcClient getClient() {
		return this.client;
	}

	@Override
	public Entity getOrigin() {
		return this.origin;
	}
	
	@Override
	public C getCommand() {
		return this.command;
	}
}
