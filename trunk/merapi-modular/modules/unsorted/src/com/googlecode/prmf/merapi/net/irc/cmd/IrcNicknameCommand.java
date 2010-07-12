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

import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * An IRC command that takes a nick (nickname) as parameter.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class IrcNicknameCommand extends AbstractIrcCommand {
	private final String nick;

	/**
	 * Builds a command with the specified nick as parameter.
	 * 
	 * @param nick
	 *            the nick
	 */
	public IrcNicknameCommand(String nick) {
		validateNick(nick);
		this.nick = nick;
	}
	
	/**
	 * Returns the IRC nick associated with this command.
	 * 
	 * @return this command's nick
	 */
	public String getNick() {
		return this.nick;
	}
	
	@Override
	public UniversalIterator<String> getArguments() {
		return iterator(this.nick);
	}
}
