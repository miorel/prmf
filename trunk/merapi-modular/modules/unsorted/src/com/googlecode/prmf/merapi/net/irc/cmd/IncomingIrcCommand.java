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
package com.googlecode.prmf.merapi.net.irc.cmd;

import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;

/**
 * Identifies a command as one that can be received by a client from a server.
 * Mostly a marker interface, this does define one new method, to allowing
 * wrapping of commands as events.
 * 
 * @author Miorel-Lucian Palii
 */
public interface IncomingIrcCommand extends IrcCommand {
	/**
	 * Builds an event wrapper for this command, associating it with the given
	 * client and entity.
	 * 
	 * @param client
	 *            the client that received this command
	 * @param origin
	 *            the origin of this command, may be <code>null</code>
	 * @return an IRC event
	 */
	public IrcEvent<?> getEvent(IrcClient client, Entity origin);
}
