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
package com.googlecode.prmf.merapi.net.irc.cmd;

import static com.googlecode.prmf.merapi.util.Iterators.iterator;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEventListener;

/**
 * An IRC ping message.
 * 
 * @author Miorel-Lucian Palii
 * @see PongCommand
 */
public class PingCommand extends IrcTargetsCommand implements IncomingIrcCommand {	
	/**
	 * Builds an IRC ping with the specified targets.
	 * 
	 * @param targets
	 *            the pingers
	 */
	public PingCommand(Iterator<String> targets) {
		super(targets);
	}
	
	@Override
	public String getCommand() {
		return "PING";
	}

	@Override
	public IrcEvent<PingCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<PingCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.pingEvent(this);
			}		
		};
	}
	
	/**
	 * Builds an IRC ping using the specified parameters.
	 * 
	 * @param param
	 *            the command parameters
	 * @return an IRC ping
	 */
	public static PingCommand build(String[] param) {
		validateParam(param, 1);
		return new PingCommand(iterator(param[0].split("\\s+")));
	}
}
