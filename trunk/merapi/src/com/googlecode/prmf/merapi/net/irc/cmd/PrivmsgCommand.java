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
 * A private message on IRC. This is a misnomer, because a "private message" may
 * be sent to an entire channel.
 * 
 * @author Miorel-Lucian Palii
 */
public class PrivmsgCommand extends IrcTargetMessageCommand implements IncomingIrcCommand {
	/**
	 * Builds an IRC private message command directed at the specified target.
	 * 
	 * @param target
	 *            the message target
	 * @param message
	 *            the message
	 */
	public PrivmsgCommand(String target, String message) {
		super(target, message);
	}

	@Override
	public String getCommand() {
		return "PRIVMSG";
	}

	@Override
	public IrcEvent<? extends PrivmsgCommand> getEvent(final IrcClient client, final Entity origin) {
		return new AbstractIrcEvent<PrivmsgCommand>(client, origin, this) {
			@Override
			protected void doTrigger(IrcEventListener listener) {
				listener.privmsgEvent(this);
			}
		};
	}
	
	/**
	 * Builds a private message command using the specified parameters.
	 * 
	 * @param param
	 *            the command parameters
	 * @return a private message command
	 */
	public static PrivmsgCommand build(String[] param) {
		validateParam(param, 2);
		PrivmsgCommand ret;
		if(param[1].matches(CtcpCommand.CTCP_REGEX))
			ret = CtcpCommand.build(param);
		else
			ret = new PrivmsgCommand(param[0], param[1]);
		return ret;
	}
}
