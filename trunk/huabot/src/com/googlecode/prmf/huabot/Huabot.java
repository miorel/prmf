package com.googlecode.prmf.huabot;

import static com.googlecode.prmf.merapi.net.irc.IrcCommands.privmsg;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.cmd.JoinCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PrivmsgCommand;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEventListener;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.www.Tinysong;
import com.googlecode.prmf.merapi.net.www.TinysongResult;
import com.googlecode.prmf.merapi.util.Strings;

public class Huabot extends AbstractIrcEventListener {
	private static final String COMMAND_TRIGGER = "~"; // what a message must start with to be considered a command
	private static final Pattern COMMAND_PATTERN = Pattern.compile("\\s*" + Pattern.quote(COMMAND_TRIGGER) + "\\s*(\\w+)\\s+([\\s\\S]*)");

	public Huabot() {
	}

	@Override
	public void joinEvent(IrcEvent<JoinCommand> event) {
		IrcClient client = event.getClient();
		JoinCommand cmd = event.getCommand();

		String nick = client.getDesiredNick(); // We're assuming that the desired nick and actual nick are the same... Not good.
		String channel = cmd.getChannel().toLowerCase(Locale.ENGLISH);
		String user = event.getOrigin().getNick();

		if(!nick.equalsIgnoreCase(user)) // Otherwise we'd welcome ourselves!
			privmsg(client, channel, String.format("Welcome to %s, %s!", channel, user));
	}

	@Override
	public void privmsgEvent(IrcEvent<PrivmsgCommand> event) {
		IrcClient client = event.getClient();
		PrivmsgCommand cmd = event.getCommand();

		String nick = client.getDesiredNick(); // Again, unsafely assuming that the desired nick and actual nick are the same.

		// The "target" of the message is either a channel, or our nickname, in the case of a direct message.
		String target = cmd.getTarget();
		if(nick.equalsIgnoreCase(target)) {
			// direct message
		}
		else {
			// message in a channel
			reactToMessage(client, target, event.getOrigin(), cmd.getMessage());
		}
	}

	private void reactToMessage(IrcClient client, String channel, Entity sender, String message) {
		Matcher m = COMMAND_PATTERN.matcher(message);
		if(m.matches()) {
			// We got ourselves a command!
			String cmd = m.group(1).toLowerCase(Locale.ENGLISH);
			String[] arg = m.group(2).trim().split("\\s+");

			if(cmd.equals("version")) {
				privmsg(client, channel, "This is huabot, REWRITE edition.");
			}

			if(cmd.equals("date")) {
				privmsg(client, channel, String.format("Today's date is %s.",
					DateFormat.getDateInstance(DateFormat.LONG).format(new Date())));
			}

			if(cmd.equals("time")) {
				privmsg(client, channel, String.format("The current time is %s.",
					DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date())));
			}

			if(cmd.equals("tinysong")) {
				Tinysong ts = new Tinysong();

				String query = Strings.join(" ", arg);
				String response = null;
				try {
					TinysongResult result = ts.topResult(query);
					if(result != null)
						response = String.format("Top result for \"%s\": %s by %s <%s>", query, result.getSongName(), result.getArtistName(), result.getUrl());
					else
						response = String.format("There were no search results for \"%s\" :/", query);
				}
				catch(Exception e) {
					response = "There was an error. " + e.getMessage();
				}

				privmsg(client, channel, response);
			}
		}
	}
}
