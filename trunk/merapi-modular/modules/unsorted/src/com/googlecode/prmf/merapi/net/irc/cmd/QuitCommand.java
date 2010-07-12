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
import com.googlecode.prmf.merapi.util.Iterators;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * A command to quit IRC.
 *
 * @author Miorel-Lucian Palii
 */
public class QuitCommand extends IrcOptionalMessageCommand implements IncomingIrcCommand {
	/**
	 * Builds a quit command with the specified message.
	 *
	 * @param message
	 *            the quit message
	 */
	public QuitCommand(String message) {
		super(message);
	}

	/**
	 * Builds a quit command with no message.
	 */
	public QuitCommand() {
		super();
	}

	@Override
	public UniversalIterator<String> getArguments() {
		return hasMessage() ? Iterators.iterator(getMessage()) : Iterators.<String>iterator();
	}

	@Override
	public String getCommand() {
		return "QUIT";
	}

	/**
	 * Builds an IRC quit command using the specified parameters.
	 *
	 * @param param
	 *            the command parameters
	 * @return an IRC quit command
	 */
	public static QuitCommand build(String[] param) {
		validateParam(param, 0, 1);
		return new QuitCommand(param.length == 0 ? null : param[0]);
	}

	@Override
	public IrcEvent<QuitCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<QuitCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.quitEvent(this);
			}
		};
	}
}
