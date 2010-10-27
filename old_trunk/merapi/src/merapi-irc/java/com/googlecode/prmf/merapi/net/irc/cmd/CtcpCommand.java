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

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEventListener;

/**
 * A message in the client-to-client protocol (CTCP).
 * 
 * @author Miorel-Lucian Palii
 */
public class CtcpCommand extends PrivmsgCommand {
	/**
	 * Regular expression for a CTCP message.
	 */
	public static final String CTCP_REGEX ="\1([A-Z]+)\\s*(.*)\1";
	
	private static final Pattern CTCP_PATTERN = Pattern.compile(CTCP_REGEX);
	
	private final String ctcpCommand;
	private final String ctcpArgs;
	
	/**
	 * Builds a CTCP command directed at the specified target.
	 * 
	 * @param target
	 *            the message target
	 * @param message
	 *            the CTCP message
	 */
	public CtcpCommand(String target, String message) {
		super(target, message);
		Matcher m = CTCP_PATTERN.matcher(message);
		if(!m.matches())
			throw new IllegalArgumentException("The message is not proper CTCP.");
		this.ctcpCommand = m.group(1);
		this.ctcpArgs = m.group(2).isEmpty() ? null : m.group(2);
	}

	/**
	 * Builds a CTCP command directed at the specified target.
	 * 
	 * @param target
	 *            the message target
	 * @param ctcpCommand
	 *            the CTCP command name
	 * @param ctcpArgs
	 *            the CTCP command arguments
	 */
	public CtcpCommand(String target, String ctcpCommand, String ctcpArgs) {
		this(target, String.format(ctcpArgs == null || ctcpArgs.isEmpty() ? "\1%s\1" : "\1%s %s\1", ctcpCommand == null ? "" : ctcpCommand.toUpperCase(Locale.ENGLISH), ctcpArgs));
		validateString("the CTCP command", ctcpCommand, false, false);
	}

	/**
	 * Gets the name of the CTCP command.
	 * 
	 * @return the CTCP command name
	 */
	public String getCtcpCommand() {
		return this.ctcpCommand;
	}

	/**
	 * Gets the argument(s) of the CTCP command.
	 * 
	 * @return the CTCP command arguments
	 */
	public String getCtcpArguments() {
		return this.ctcpArgs;
	}
	
	@Override
	public IrcEvent<CtcpCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<CtcpCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.unknwonCtcpEvent(this);
			}
		};
	}

	/**
	 * Builds a CTCP command using the specified parameters.
	 * 
	 * @param param
	 *            the command parameters
	 * @return a CTCP command
	 */
	public static CtcpCommand build(String[] param) {
		validateParam(param, 2);
		return new CtcpCommand(param[0], param[1]);
	}
}
