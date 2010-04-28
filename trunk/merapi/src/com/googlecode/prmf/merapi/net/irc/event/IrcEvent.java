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
package com.googlecode.prmf.merapi.net.irc.event;

import com.googlecode.prmf.merapi.event.Event;
import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.cmd.IncomingIrcCommand;

/**
 * Decorates an incoming IRC command as an event object with information about
 * the client that received it and the entity that originated it.
 * 
 * @author Miorel-Lucian Palii
 * @param <C>
 *            the type of command held
 */
public interface IrcEvent<C extends IncomingIrcCommand> extends Event<IrcEventListener> {
	/**
	 * Retrieves a reference to the client that received the command.
	 * 
	 * @return the client that received the command.
	 */
	public IrcClient getClient();
	
	/**
	 * Retrieves the entity that originated the command.
	 * 
	 * @return the origin of the command
	 */
	public Entity getOrigin();
	
	/**
	 * Retrieves the decorated IRC command. 
	 * 
	 * @return the IRC command
	 */
	public C getCommand();
}
