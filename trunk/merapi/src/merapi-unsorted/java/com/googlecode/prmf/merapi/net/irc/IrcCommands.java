/*
 * Merapi - Multi-purpose Java library
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
package com.googlecode.prmf.merapi.net.irc;

import static com.googlecode.prmf.merapi.util.Iterators.iterator;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.net.irc.cmd.InviteCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.IrcCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.JoinCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.KickCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.NickCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.NoticeCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PartCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PassCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PongCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PrivmsgCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.QuitCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.SqueryCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.UserCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.WhoisCommand;

/**
 * <p>
 * Makes the sending of IRC commands less verbose.
 * </p>
 * <p>
 * I didn't want to pollute the {@link IrcClient} class with methods for every
 * imaginable IRC action, but {@link IrcClient#send(IrcCommand)} is rather
 * verbose. This is where this class (technically a library, because it's used
 * procedurally) comes in. Try:
 * <pre>
 * import static com.googlecode.lawu.net.irc.IrcCommands.*;
 * </pre>
 * to get the IRC commands you've always wanted!
 * </p>
 *
 * @author Miorel-Lucian Palii
 * @see IrcClient
 */
public class IrcCommands {
	/**
	 * There is no need to instantiate this class.
	 */
	private IrcCommands() {
	}

	/**
	 * Tells the client to pong the specified targets.
	 *
	 * @param client
	 *            the IRC client
	 * @param targets
	 *            the targets to pong
	 */
	public static void pong(IrcClient client, Iterator<String> targets) {
		client.send(new PongCommand(targets));
	}

	/**
	 * Tells the client to pong the specified targets.
	 *
	 * @param client
	 *            the IRC client
	 * @param targets
	 *            the targets to pong
	 */
	public static void pong(IrcClient client, String... targets) {
		pong(client, iterator(targets));
	}

	/**
	 * Tells the client to send the specified password to the IRC server.
	 *
	 * @param client
	 *            the IRC client
	 * @param password
	 *            the server password
	 */
	public static void pass(IrcClient client, String password) {
		client.send(new PassCommand(password));
	}

	/**
	 * Tells the client to quit IRC with no message.
	 *
	 * @param client
	 *            the IRC client
	 */
	public static void quit(IrcClient client) {
		client.send(new QuitCommand());
	}

	/**
	 * Tells the client to quit IRC with the specified message.
	 *
	 * @param client
	 *            the IRC client
	 * @param message
	 *            the quit message
	 */
	public static void quit(IrcClient client, String message) {
		client.send(new QuitCommand(message));
	}

	/**
	 * Tells the client to invite the given nick to the specified channel.
	 *
	 * @param client
	 *            the IRC client
	 * @param nick
	 *            the user to invite
	 * @param channel
	 *            the channel to invite to
	 */
	public static void invite(IrcClient client, String nick, String channel) {
		client.send(new InviteCommand(nick, channel));
	}

	/**
	 * Tells the client to attempt joining the specified channel with no key.
	 *
	 * @param client
	 *            the IRC client
	 * @param channel
	 *            the channel to join
	 */
	public static void join(IrcClient client, String channel) {
		client.send(new JoinCommand(channel));
	}

	/**
	 * Tells the client to attempt joining the specified channel with the given
	 * key.
	 *
	 * @param client
	 *            the IRC client
	 * @param channel
	 *            the channel to join
	 * @param key
	 *            the channel key
	 */
	public static void join(IrcClient client, String channel, String key) {
		client.send(new JoinCommand(channel, key));
	}

	/**
	 * Tells the client to part the specified channel with no message.
	 *
	 * @param client
	 *            the IRC client
	 * @param channel
	 *            the channel to part
	 */
	public static void part(IrcClient client, String channel) {
		client.send(new PartCommand(channel));
	}

	/**
	 * Tells the client to part the specified channel with the specified
	 * message.
	 *
	 * @param client
	 *            the IRC client
	 * @param channel
	 *            the channel to part
	 * @param message
	 *            the part message
	 */
	public static void part(IrcClient client, String channel, String message) {
		client.send(new PartCommand(channel, message));
	}

	/**
	 * Tells the client to change IRC nicks.
	 *
	 * @param client
	 *            the IRC client
	 * @param nick
	 *            the new nick
	 */
	public static void nick(IrcClient client, String nick) {
		client.send(new NickCommand(nick));
	}

	/**
	 * Tells the client to send a whois query on the specified nick.
	 *
	 * @param client
	 *            the IRC client
	 * @param nick
	 *            the user to query
	 */
	public static void whois(IrcClient client, String nick) {
		client.send(new WhoisCommand(nick));
	}

	/**
	 * Tells the client to kick the specified nick from the channel with no
	 * message.
	 *
	 * @param client
	 *            the IRC client
	 * @param channel
	 *            the channel to kick from
	 * @param nick
	 *            the user to kick
	 */
	public static void kick(IrcClient client, String channel, String nick) {
		client.send(new KickCommand(channel, nick));
	}

	/**
	 * Tells the client to kick the specified nick from the channel with the
	 * given message.
	 *
	 * @param client
	 *            the IRC client
	 * @param channel
	 *            the channel to kick from
	 * @param nick
	 *            the user to kick
	 * @param message
	 *            the kick message
	 */
	public static void kick(IrcClient client, String channel, String nick, String message) {
		client.send(new KickCommand(channel, nick, message));
	}

	/**
	 * Tells the client to send the specified message to the specified target.
	 * The target may be a channel or a nick.
	 *
	 * @param client
	 *            the IRC client
	 * @param target
	 *            the target of the message
	 * @param message
	 *            the message
	 */
	public static void privmsg(IrcClient client, String target, String message) {
		client.send(new PrivmsgCommand(target, message));
	}

	/**
	 * Tells the client to send a service query message to the specified target.
	 * This is the same as a private message except the target must be a
	 * service.
	 *
	 * @param client
	 *            the IRC client
	 * @param target
	 *            the target of the message
	 * @param message
	 *            the message
	 * @see #privmsg(IrcClient,String,String)
	 */
	public static void squery(IrcClient client, String target, String message) {
		client.send(new SqueryCommand(target, message));
	}

	/**
	 * Tells the client to send a notice to the specified target. The target may
	 * be a channel or a nick.
	 *
	 * @param client
	 *            the IRC client
	 * @param target
	 *            the target of the message
	 * @param message
	 *            the notice
	 */
	public static void notice(IrcClient client, String target, String message) {
		client.send(new NoticeCommand(target, message));
	}

	/**
	 * Tells the client to send a user identification command with the specified
	 * user name and real name and a default initial user mode.
	 *
	 * @param client
	 *            the IRC client
	 * @param userName
	 *            the user name
	 * @param realName
	 *            the real name
	 */
	public static void user(IrcClient client, String userName, String realName) {
		client.send(new UserCommand(userName, realName));
	}

	/**
	 * Tells the client to send a user identification command with the specified
	 * user name, initial user mode, and real name.
	 *
	 * @param client
	 *            the IRC client
	 * @param userName
	 *            the user name
	 * @param initialMode
	 *            the initial user mode
	 * @param realName
	 *            the real name
	 */
	public static void user(IrcClient client, String userName, int initialMode, String realName) {
		client.send(new UserCommand(userName, initialMode, realName));
	}
}
