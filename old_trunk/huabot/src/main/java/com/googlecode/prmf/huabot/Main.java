package com.googlecode.prmf.huabot;

import static com.googlecode.prmf.merapi.net.irc.IrcCommands.join;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.googlecode.prmf.merapi.net.SocketClientsThread;
import com.googlecode.prmf.merapi.net.event.AbstractNetworkEventListener;
import com.googlecode.prmf.merapi.net.event.DisconnectedEvent;
import com.googlecode.prmf.merapi.net.event.ReadingEvent;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.log.NetworkLogger;

public class Main {
	public static void main(String[] arg) throws Exception {
		final String channel = "#" + (arg.length >= 1 ? arg[0] : "ufpt");
		String nick = arg.length >= 2 ? arg[1] : "huabot";
		String hostname = arg.length >= 3 ? arg[2] : "irc.freenode.net";
		int port = arg.length >= 4 ? Integer.parseInt(arg[3]) : 6667;
		
		SocketAddress address = new InetSocketAddress(hostname, port);
		SocketClientsThread thread = new SocketClientsThread();
		
		final IrcClient client = new IrcClient(address, thread);
		
		client.setDesiredNick(nick);
		client.setUserName(nick);
		
		// Easy way to monitor everything.
		client.addNetworkEventListener(new NetworkLogger(System.out));
		
		client.addNetworkEventListener(new AbstractNetworkEventListener() {
			// The following is a hack to join the channel.
			// Basically we wait for the last line of the server's message of the day to send the command.
			@Override
			public void reading(ReadingEvent event) {
				String line = event.getMessage();
				if(line.matches("\\S+ 376 .*"))
					join(client, channel);
			}
			
			// Rejoin if we get disconnected.
			@Override
			public void disconnected(DisconnectedEvent event) {
				event.getClient().stop();
				try {
					event.getClient().start();
				}
				catch(IOException e) {
					// Bad stuff happened...
					e.printStackTrace();
					System.exit(1);
				}
			}
		});
		 
		client.addIrcEventListener(new Huabot());
		
		client.start();
		thread.start();
	}
}
