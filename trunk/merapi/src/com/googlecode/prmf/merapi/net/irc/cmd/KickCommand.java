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

import static com.googlecode.prmf.merapi.util.Iterators.iterator;

import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEventListener;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * A command to kick a user from a channel.
 *  
 * @author Miorel-Lucian Palii
 */
public class KickCommand extends AbstractIrcCommand implements IncomingIrcCommand {
	private final String channel;
	private final String nick;
	private final String message;

	/**
	 * Builds a new kick command with the specified channel, nick, and message.
	 * 
	 * @param channel
	 *            the channel of the kick
	 * @param nick
	 *            the user being kicked
	 * @param message
	 *            the kick message
	 */
	public KickCommand(String channel, String nick, String message) {
		validateChannel(channel);
		validateNick(nick);
		validateMessage(message, false);
		this.channel = channel;
		this.nick = nick;
		this.message = message;
	}

	/**
	 * Builds a new kick command with the specified channel and nick, and no
	 * message.
	 * 
	 * @param channel
	 *            the channel of the kick
	 * @param nick
	 *            the user being kicked
	 */
	public KickCommand(String channel, String nick) {
		this(channel, nick, null);
	}

	/**
	 * Gets the channel of the kick.
	 * 
	 * @return the channel of the kick
	 */
	public String getChannel() {
		return this.channel;
	}
	
	/**
	 * Gets the nick of the kicked user.
	 * 
	 * @return the kicked user
	 */
	public String getNick() {
		return this.nick;
	}
	
	/**
	 * Gets the kick message.
	 * 
	 * @return the kick message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Checks whether there is a message associated with this kick.
	 * 
	 * @return whether there is a kick message
	 */
	public boolean hasMessage() {
		return this.message != null;
	}
	
	@Override
	public UniversalIterator<String> getArguments() {	
		return hasMessage() ? iterator(this.channel, this.nick, this.message) : iterator(this.channel, this.nick);
	}

	@Override
	public String getCommand() {
		return "KICK";
	}

	@Override
	public IrcEvent<KickCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<KickCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.kickEvent(this);
			}		
		};
	}
	
	/**
	 * Builds a kick command using the specified parameters.
	 * 
	 * @param param
	 *            the command parameters
	 * @return a kick command
	 */
	public static KickCommand build(String[] param) {
		validateParam(param, 2, 3);
		return param.length == 2 || param[2].equals(param[1]) ? new KickCommand(param[0], param[1]) : new KickCommand(param[0], param[1], param[2]);
	}
}
