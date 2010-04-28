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

import com.googlecode.prmf.merapi.net.irc.cmd.CtcpCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.InviteCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.JoinCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.KickCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.NickCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.NoticeCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PingCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PrivmsgCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.QuitCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.UnknownCommand;

/**
 * Basic implementation of the {@link IrcEventListener} interface providing
 * empty implementations for all the methods.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class AbstractIrcEventListener implements IrcEventListener {
	@Override
	public void pingEvent(IrcEvent<PingCommand> event) {
	}

	@Override
	public void inviteEvent(IrcEvent<InviteCommand> event) {
	}

	@Override
	public void kickEvent(IrcEvent<KickCommand> event) {
	}

	@Override
	public void nickEvent(IrcEvent<NickCommand> event) {
	}

	@Override
	public void noticeEvent(IrcEvent<NoticeCommand> event) {
	}

	@Override
	public void privmsgEvent(IrcEvent<PrivmsgCommand> event) {
	}

	@Override
	public void quitEvent(IrcEvent<QuitCommand> event) {
	}

	@Override
	public void unknownCommandEvent(IrcEvent<UnknownCommand> event) {
	}

	@Override
	public void joinEvent(IrcEvent<JoinCommand> event) {
	}
	
	@Override
	public void unknwonCtcpEvent(IrcEvent<CtcpCommand> event) {
	}
}
