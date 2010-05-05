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
 * A command to leave an IRC channel.
 *
 * @author Miorel-Lucian Palii
 */
public class PartCommand extends IrcOptionalMessageCommand implements IncomingIrcCommand {
	private final String channel;

	/**
	 * Builds a channel part command with the specified message.
	 *
	 * @param channel
	 *            the channel to part
	 * @param message
	 *            the part message
	 */
	public PartCommand(String channel, String message) {
		super(message);
		if(channel == null)
			throw new NullPointerException("The channel may not be null.");
		if(channel.isEmpty())
			throw new IllegalArgumentException("The channel may not be zero length.");
		this.channel = channel;
	}

	/**
	 * Builds a channel part command with no message.
	 *
	 * @param channel
	 *            the channel to part
	 */
	public PartCommand(String channel) {
		this(channel, null);
	}

	/**
	 * Gets the channel to part.
	 *
	 * @return the channel to part
	 */
	public String getChannel() {
		return this.channel;
	}

	@Override
	public UniversalIterator<String> getArguments() {
		return hasMessage() ? Iterators.iterator(this.channel, getMessage()) : Iterators.iterator(this.channel);
	}

	@Override
	public String getCommand() {
		return "PART";
	}

	/**
	 * Builds an IRC channel part command using the specified parameters.
	 *
	 * @param param
	 *            the command parameters
	 * @return an IRC channel part command
	 */
	public static PartCommand build(String[] param) {
		validateParam(param, 1, 2);
		return new PartCommand(param[0], param.length == 1 ? null : param[1]);
	}

	@Override
	public IrcEvent<PartCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<PartCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.partEvent(this);
			}
		};
	}
}
