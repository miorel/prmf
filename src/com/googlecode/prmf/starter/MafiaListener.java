package com.googlecode.prmf.starter;

import com.googlecode.prmf.Game;

public class MafiaListener implements Listener {
	String channel = "#UFPT"; //TODO why is the channel listed in so many places? and why is it uppercase?
	private Game game;
	public void receiveLine(String in, InputThread inputThread) 
	{
		String[] msg = in.split(" ",4);
		String user = channel;
		
		if(msg[0].indexOf("!") > 1)
			user = msg[0].substring(1,msg[0].indexOf("!"));

		if(msg.length >= 4 && msg[3].equalsIgnoreCase(":~mafia") && game == null) 
		{
			game = new Game(user, inputThread);
			inputThread.sendMessage(channel, "Mafia game started by " + user + "!");
			game.receiveMessage(":" + user + "! PRIVMSG " + channel + " :~join");
			
		}
		else if(msg.length >= 4 && msg[3].equalsIgnoreCase(":~join") && game != null) 
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].equalsIgnoreCase(":~end") && game != null) 
		{
			if(user.equals(game.getGameStarter()))
			{
				inputThread.sendMessage(channel, "Mafia game ended!");
				game = null;
			}
			else
				inputThread.sendMessage(channel, "The game can only be ended by " + game.getGameStarter());
		}
		else if(msg.length >= 4 && msg[3].equalsIgnoreCase(":~quit") && game != null)
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].equalsIgnoreCase(":~start") && game != null)
		{
			game.receiveMessage(in);
		}
	}
}
