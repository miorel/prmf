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
 * An IRC command that takes a message.
 *
 * @author Miorel-Lucian Palii
 */
public abstract class IrcMessageCommand extends AbstractIrcCommand {
	private final String message;

	/**
	 * Builds an IRC message command.
	 *
	 * @param message
	 *            the command's message
	 */
	public IrcMessageCommand(String message) {
		validateMessage(message, true);
		this.message = message;
	}

	/**
	 * Gets the command's message.
	 *
	 * @return the command's message
	 */
	public String getMessage() {
		return this.message;
	}

	@Override
	public UniversalIterator<String> getArguments() {
		return iterator(this.message);
	}
}
