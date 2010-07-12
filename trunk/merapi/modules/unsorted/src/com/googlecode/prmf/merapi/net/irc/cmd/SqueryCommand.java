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

/**
 * A query message sent to an IRC service.
 * 
 * @author Miorel-Lucian Palii
 */
public class SqueryCommand extends IrcTargetMessageCommand {
	/**
	 * Builds a service query command with the specified message directed at the
	 * target.
	 * 
	 * @param target
	 *            the message target
	 * @param message
	 *            the query
	 */
	public SqueryCommand(String target, String message) {
		super(target, message);
	}
	
	@Override
	public String getCommand() {
		return "SQUERY";
	}
}
