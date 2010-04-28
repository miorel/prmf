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

import com.googlecode.prmf.merapi.util.Strings;

/**
 * Some library methods facilitating validation in IRC commands. Subclass this
 * class to get them.
 * 
 * @author Miorel-Lucian Palii
 */
public abstract class AbstractIrcCommand implements IrcCommand {
	/**
	 * Default constructor, does nothing.
	 */
	public AbstractIrcCommand() {
	}

	/**
	 * Validates an IRC nick.
	 * 
	 * @param nick
	 *            the nick to validate
	 */
	protected static void validateNick(String nick) {
		validateString("nickname", nick, false, false);
	}

	/**
	 * Validates an IRC channel name.
	 * 
	 * @param channel
	 *            the channel name to validate
	 */
	protected static void validateChannel(String channel) {
		validateString("channel name", channel, false, false);
	}

	/**
	 * Validates an IRC "message" e.g.&nbsp;a part message, a kick message, etc.
	 * 
	 * @param message
	 *            the message to validate
	 * @param required
	 *            whether the message is required
	 */
	protected static void validateMessage(String message, boolean required) {
		validateString("message", message, !required, required);
	}

	/**
	 * Validates a parameter array.
	 * 
	 * @param param
	 *            the parameter array to validate
	 * @param size
	 *            the required array size
	 */
	protected static void validateParam(String[] param, int size) {
		validateParam(param, size, size);
	}

	/**
	 * Validates a parameter array.
	 * 
	 * @param param
	 *            the parameter array to validate
	 * @param minSize
	 *            the minimum array size
	 * @param maxSize
	 *            the maximum array size
	 */
	protected static void validateParam(String[] param, int minSize, int maxSize) {
		if(param == null)
			throw new NullPointerException("The parameter array may not be null.");
		if(param.length < minSize || param.length > maxSize)
			throw new IllegalArgumentException("The parameter array had unexpected size.");
		for(String p: param)
			if(p == null)
				throw new NullPointerException("Null parameter not allowed.");	
	}

	/**
	 * Validates some string.
	 * 
	 * @param identifier
	 *            a description of what is being validated for use in exception
	 *            messages
	 * @param string
	 *            the string to validate
	 * @param nullAllowed
	 *            whether the string being validated is allowed to be
	 *            <code>null</code>
	 * @param emptyAllowed
	 *            whether the string being validated is allowed to be the empty
	 *            string
	 */
	protected static void validateString(String identifier, String string, boolean nullAllowed, boolean emptyAllowed) {
		RuntimeException e = null;
		if(string == null) {
			if(!nullAllowed) {
				String problem = "The " + identifier + " may not be null";
				if(emptyAllowed)
					problem += ", use the empty string instead";
				problem += ".";
				e = new NullPointerException(problem);
			}
		}
		else if(string.isEmpty()) {
			if(!emptyAllowed) {
				String problem = "The " + identifier + " may not be zero length";
				if(nullAllowed)
					problem += ", use null instead";
				problem += ".";
				e = new IllegalArgumentException(problem);
			}
		}
		else if(!Strings.isSingleLine(string))
			e = new IllegalArgumentException("The " + identifier + " may not be multi-line.");
		if(e != null)
			throw e;
	}
}
