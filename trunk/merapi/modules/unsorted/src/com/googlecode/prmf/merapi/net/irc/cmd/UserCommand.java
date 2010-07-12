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
 * An IRC user identification command.
 * 
 * @author Miorel-Lucian Palii
 */
public class UserCommand extends AbstractIrcCommand {
	/**
	 * User mode to become "invisible" (visible only to those who know your
	 * exact nickname or are on the same channel as you).
	 */
	public static final int MODE_INVISIBLE = 1 << 3;
	
	/**
	 * User mode to enable network wide messages between IRC operators.
	 */
	public static final int MODE_WALLOPS = 1 << 2;
	
	private final String userName;
	private final int initialMode;
	private final String realName;

	/**
	 * Builds an IRC user identification command with the specified user name,
	 * initial user mode, and real name.
	 * 
	 * @param userName
	 *            the user name
	 * @param initialMode
	 *            the initial user mode
	 * @param realName
	 *            the real name
	 */
	public UserCommand(String userName, int initialMode, String realName) {
		validateString("user name", userName, false, false);
		if(initialMode < 0)
			throw new IllegalArgumentException("The initial mode may not be negative.");
		validateString("real name", realName, false, true);
		this.userName = userName;
		this.initialMode = initialMode;
		this.realName = realName;
	}

	/**
	 * Builds an IRC user identification command with the specified user name
	 * and real name, and a default initial user mode.
	 * 
	 * @param userName
	 *            the user name
	 * @param realName
	 *            the real name
	 */
	public UserCommand(String userName, String realName) {
		this(userName, MODE_INVISIBLE | MODE_WALLOPS, realName);
	}
	
	/**
	 * Gets the user name.
	 * 
	 * @return the user name
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * Gets the real name.
	 * 
	 * @return the real name
	 */
	public String getRealName() {
		return this.realName;
	}

	/**
	 * Gets the initial user mode.
	 * 
	 * @return the initial user mode
	 */
	public int getInitialMode() {
		return this.initialMode;
	}
	
	@Override
	public UniversalIterator<String> getArguments() {
		return iterator(this.userName, Integer.toString(this.initialMode), "*", this.realName);
	}

	@Override
	public String getCommand() {
		return "USER";
	}
}
