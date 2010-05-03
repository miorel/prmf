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
package com.googlecode.prmf.merapi.net.irc;

import static com.googlecode.prmf.merapi.net.irc.IrcCommands.nick;
import static com.googlecode.prmf.merapi.net.irc.IrcCommands.pong;
import static com.googlecode.prmf.merapi.net.irc.IrcCommands.user;
import static com.googlecode.prmf.merapi.util.Iterators.adapt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.prmf.merapi.dp.Iterator;
import com.googlecode.prmf.merapi.event.EventManager;
import com.googlecode.prmf.merapi.net.LineOrientedClient;
import com.googlecode.prmf.merapi.net.event.AbstractNetworkEventListener;
import com.googlecode.prmf.merapi.net.event.ConnectedEvent;
import com.googlecode.prmf.merapi.net.irc.cmd.IncomingIrcCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.IrcCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PingCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.UnknownCommand;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEventListener;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.irc.event.IrcEventListener;
import com.googlecode.prmf.merapi.nio.Registrar;

/**
 * A client that speaks the IRC protocol.
 *
 * @author Miorel-Lucian Palii
 */
public class IrcClient extends LineOrientedClient {
	/**
	 * Regular expression for matching an IRC command.
	 */
	public static final String COMMAND_REGEX = "\\s*(?::(\\S+)\\s+)?(\\S+)\\s*(.*)";

	/**
	 * Regular expression for matching IRC command parameters.
	 */
	public static final String PARAM_REGEX = ":[\\S\\s]*|\\S+";

	private static final Pattern COMMAND_PATTERN = Pattern.compile(COMMAND_REGEX);
	private static final Pattern PARAM_PATTERN = Pattern.compile(PARAM_REGEX);

	private static final Map<String,Method> IRC_STRING_COMMANDS = new HashMap<String,Method>();
	static {
		ResourceBundle ircStringCmds = ResourceBundle.getBundle(IrcCommands.class.getPackage().getName() + ".IrcCommandList");
		for(String cmd: adapt(ircStringCmds.getKeys())) {
			Method m = null;
			try {
				m = Class.forName(ircStringCmds.getString(cmd)).getMethod("build", String[].class);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			if(m != null)
				IRC_STRING_COMMANDS.put(cmd.toUpperCase(Locale.ENGLISH), m);
		}
	}

	private final EventManager<IrcEventListener> ircEventManager;

	private String desiredNick;
	private String userName;
	private String realName;

	/**
	 * Builds an IRC client that will connect to the specified address, register
	 * with the specified registrar, and use a default provider to open
	 * channels.
	 *
	 * @param address
	 *            the address to connect to
	 * @param registrar
	 *            the registrar which will dictate network activity
	 */
	public IrcClient(SocketAddress address, Registrar registrar) {
		this(address, registrar, null);
	}

	/**
	 * Builds an IRC client that will connect to the specified address, register
	 * with the specified registrar, and use the specified provider to open
	 * channels.
	 *
	 * @param address
	 *            the address to connect to
	 * @param registrar
	 *            the registrar which will dictate network activity
	 * @param provider
	 *            the provider to use for opening channels, or <code>null</code>
	 *            to use the default provider
	 */
	public IrcClient(SocketAddress address, Registrar registrar, SelectorProvider provider) {
		super(address, registrar, provider);
		this.ircEventManager = new EventManager<IrcEventListener>();
		addIrcEventListener(new AbstractIrcEventListener() {
			@Override
			public void pingEvent(IrcEvent<PingCommand> event) {
				pong(IrcClient.this, event.getCommand().getTargets());
			}
		});
		addNetworkEventListener(new AbstractNetworkEventListener() {
			@Override
			public void connected(ConnectedEvent event) {
				nick(IrcClient.this, getDesiredNick());
				user(IrcClient.this, getUserName(), getRealName());
			}
		});
	}

	/**
	 * Gets the client's desired nick (nickname).
	 *
	 * @return the client's desired nick
	 * @see #setDesiredNick(String)
	 */
	public String getDesiredNick() {
		if(this.desiredNick == null)
			this.desiredNick = getUserName();
		return this.desiredNick;
	}

	/**
	 * Sets the client's desired nick (nickname). This may or may not be the
	 * nick that ends up being used, depending on availability. The argument may
	 * be <code>null</code> to choose a nick automatically (which currently
	 * means to use the {@linkplain #getUserName() username} as nick).
	 *
	 * @param desiredNick
	 *            the new value; may be <code>null</code> to choose
	 *            automatically
	 * @see #getDesiredNick()
	 */
	public void setDesiredNick(String desiredNick) {
		if(desiredNick != null && desiredNick.isEmpty())
			throw new IllegalArgumentException("The desired nickname may not have zero length, use null instead to choose value automatically.");
		this.desiredNick = desiredNick;
	}

	/**
	 * Gets the client's username.
	 *
	 * @return the client's username
	 * @see #setUserName(String)
	 */
	public String getUserName() {
		if(this.userName == null) {
			String user = System.getProperty("user.name");
			if(user == null) {
				StringBuilder sb = new StringBuilder();
				Random rnd = new Random();
				for(int n = 8; n >= 0; --n)
					sb.append((char) ('a' + rnd.nextInt('z' - 'a' + 1)));
				user = sb.toString();
			}
			this.userName = user;
		}
		return this.userName;
	}

	/**
	 * Sets the client's username. If the argument is <code>null</code>, a value
	 * will be chosen automatically: the username of the user running the
	 * application or, if this cannot be accessed, a randomly-generated value.
	 * Because the username is sent on connection and cannot be changed later,
	 * make any desired changes before {@linkplain #start() starting}.
	 *
	 * @param userName
	 *            the new value; may be <code>null</code> to choose
	 *            automatically
	 * @see #getUserName()
	 */
	public void setUserName(String userName) {
		if(userName != null && userName.isEmpty())
			throw new IllegalArgumentException("The user name may not have zero length, use null instead to choose value automatically.");
		this.userName = userName;
	}

	/**
	 * Gets the contents of the client's "real name" field.
	 *
	 * @return the "real name"
	 * @see #setRealName(String)
	 */
	public String getRealName() {
		if(this.realName == null)
			this.realName = " "; // because an empty value causes an error
		return this.realName;
	}

	/**
	 * Sets the contents of the client's "real name" field. Because the real
	 * name is sent on connection and cannot be changed latter, make any desired
	 * changes before {@linkplain #start() starting}.
	 *
	 * @param realName
	 *            the new value; may be <code>null</code> to use a blank one
	 * @see #getRealName()
	 */
	public void setRealName(String realName) {
		if(realName != null && realName.isEmpty())
			throw new IllegalArgumentException("The real name may not be zero length, use null instead.");
		this.realName = realName;
	}

	@Override
	protected void process(String line) {
		Matcher m = COMMAND_PATTERN.matcher(line);
		if(m.matches()) {
			String origin = m.group(1);
			String command = m.group(2);
			String params = m.group(3);
			Stack<String> paramStack = new Stack<String>();
			m = PARAM_PATTERN.matcher(params);
			while(m.find())
				paramStack.push(m.group());
			if(!paramStack.isEmpty() && paramStack.peek().startsWith(":"))
				paramStack.push(paramStack.pop().substring(1));
			Entity originObj = origin == null ? null : Entity.forInfo(origin, getAddress());
			String[] param = paramStack.toArray(new String[paramStack.size()]);
			if(command.matches("\\d{3}"))
				processCommand(originObj, Integer.parseInt(command), param);
			else
				processCommand(originObj, command.toUpperCase(Locale.ENGLISH), param);
		}
		else {
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("unused")
	private void processCommand(Entity origin, int command, String[] param) {
	}

	private void processCommand(Entity origin, String command, String[] param) {
		IncomingIrcCommand commandObj = null;
		Method method = IRC_STRING_COMMANDS.get(command.toUpperCase(Locale.ENGLISH));
		if(method != null) {
			try {
				commandObj = (IncomingIrcCommand) method.invoke(null, (Object) param);
			}
			catch(InvocationTargetException e) {
				report(e.getCause());
			}
			catch(Exception e) {
				report(e);
			}
		}
		if(commandObj == null) {
			System.err.println("Can't parse " + command + " command.");
			commandObj = new UnknownCommand(command, param);
		}
		distributeIrcEvent(commandObj.getEvent(this, origin));
	}

	/**
	 * Adds an IRC listener to this client's listener set.
	 *
	 * @param listener
	 *            the listener to add
	 */
	public void addIrcEventListener(IrcEventListener listener) {
		this.ircEventManager.addListener(listener);
	}

	/**
	 * Distributes an IRC event to the IRC listeners registered with this
	 * client.
	 *
	 * @param event
	 *            the event to distribute
	 */
	protected void distributeIrcEvent(IrcEvent<?> event) {
		this.ircEventManager.distribute(event);
	}

	/**
	 * Removes an IRC listener from this client's listener set.
	 *
	 * @param listener
	 *            the listener to remove
	 */
	public void removeIrcEventListener(IrcEventListener listener) {
		this.ircEventManager.removeListener(listener);
	}

	/**
	 * Queues the specified command to be sent to the server. It will actually
	 * be sent when the socket is ready for writing and the {@link #write()}
	 * method is invoked.
	 *
	 * @param command
	 *            the command to send
	 */
	public void send(IrcCommand command) {
		StringBuilder sb = new StringBuilder(command.getCommand());
		for(Iterator<String> args = command.getArguments(); !args.isDone();) {
			String arg = args.current();
			args.advance();
			sb.append(' ');
			if(args.isDone() && !arg.matches("\\S+"))
				sb.append(':');
			sb.append(arg);
		}
		send(sb);
	}
}
