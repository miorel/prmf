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

import com.googlecode.prmf.merapi.dp.Iterator;

/**
 * An IRC pong message.
 * 
 * @author Miorel-Lucian Palii
 * @see PingCommand
 */
public class PongCommand extends IrcTargetsCommand {
	/**
	 * Builds an IRC pong with the specified targets.
	 * 
	 * @param targets
	 *            the entities to pong
	 */
	public PongCommand(Iterator<String> targets) {
		super(targets);
	}
	
	@Override
	public String getCommand() {
		return "PONG";
	}
}
