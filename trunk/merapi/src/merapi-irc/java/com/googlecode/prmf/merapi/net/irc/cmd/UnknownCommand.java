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

import java.util.Arrays;
import java.util.Locale;

import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEventListener;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * The fall-back type to use for unrecognized IRC commands. 
 * 
 * @author Miorel-Lucian Palii
 */
public class UnknownCommand extends AbstractIrcCommand implements IncomingIrcCommand {
	private final String command;
	private final String[] param;

	/**
	 * Builds an unknown command with the specified name and parameters.
	 * 
	 * @param command
	 *            the command name
	 * @param param
	 *            the command parameters
	 */
	public UnknownCommand(String command, String[] param) {
		validateString("command ", command, false, false);
		validateParam(param, 0, Integer.MAX_VALUE);
		this.command = command.toUpperCase(Locale.ENGLISH);
		this.param = Arrays.copyOf(param, param.length);
	}

	@Override
	public UniversalIterator<String> getArguments() {
		return iterator(this.param);
	}

	@Override
	public String getCommand() {
		return this.command;
	}

	@Override
	public IrcEvent<UnknownCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<UnknownCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.unknownCommandEvent(this);
			}		
		};
	}
}
