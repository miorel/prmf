package com.googlecode.prmf.huabot;

import java.util.Locale;

import static com.googlecode.prmf.merapi.net.irc.IrcCommands.*;

import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.cmd.JoinCommand;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEventListener;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;

public class Huabot extends AbstractIrcEventListener {
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
}
