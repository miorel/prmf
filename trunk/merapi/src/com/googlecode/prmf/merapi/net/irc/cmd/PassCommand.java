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

import static com.googlecode.prmf.merapi.util.Iterators.*;
import com.googlecode.prmf.merapi.util.iterators.UniversalIterator;

/**
 * An IRC server password command.
 * 
 * @author Miorel-Lucian Palii
 */
public class PassCommand extends AbstractIrcCommand {
	private final String password;

	/**
	 * Builds a command to authorize access to the server using the specified
	 * password.
	 * 
	 * @param password
	 *            the server password
	 */
	public PassCommand(String password) {
		validateString("password", password, false, false);
		this.password = password;
	}

	/**
	 * Returns the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	@Override
	public UniversalIterator<String> getArguments() {
		return iterator(this.password);
	}

	@Override
	public String getCommand() {
		return "PASS";
	}
}
