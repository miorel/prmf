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
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEventListener;

/**
 * An IRC command signaling an error.
 *
 * @author Miorel-Lucian Palii
 */
public class ErrorCommand extends IrcMessageCommand implements IncomingIrcCommand {
	/**
	 * Builds an IRC error command with the specified message.
	 *
	 * @param message
	 *            the error message
	 */
	public ErrorCommand(String message) {
		super(message);
	}

	@Override
	public String getCommand() {
		return "ERROR";
	}

	@Override
	public IrcEvent<ErrorCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<ErrorCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.errorEvent(this);
			}
		};
	}

	/**
	 * Builds an error command using the specified parameters.
	 *
	 * @param param
	 *            the command parameters
	 * @return an error command
	 */
	public static ErrorCommand build(String[] param) {
		validateParam(param, 1);
		return new ErrorCommand(param[0]);
	}
}
