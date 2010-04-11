package com.googlecode.prmf.starter;
import com.googlecode.prmf.*;

public class MafiaListener implements Listener {
	private Game game;
	private String botName;
	public MafiaListener(String botName) {
		super();
		this.botName = botName;
	}
	public void receiveLine(String in, IOThread inputThread) 
	{
		
		String[] msg = in.split(" ",4);
		String user = "";
		
		if(msg[0].indexOf("!") > 1)
			user = msg[0].substring(1,msg[0].indexOf("!"));
		if (msg.length >= 2 && !msg[1].equals("PRIVMSG"))
			return;
		if (msg.length >= 4 && msg[2].equals(botName) && msg[3].startsWith(":~"))
			game.receiveMessage(in);
		if(msg.length >= 4)
			msg[3] = msg[3].toLowerCase();
		if(msg.length >= 4 && msg[3].equalsIgnoreCase(":~mafia") && (game == null || game.getPostgame() != null)) 
		{
			game = new Game(user, inputThread);
			inputThread.sendMessage(inputThread.getChannel(), "Mafia game started by " + user + "!");
			game.receiveMessage(":" + user + "! PRIVMSG " + inputThread.getChannel() + " :~join"); // TODO H4X is bad
			
		}
		else if(msg.length >= 4 && msg[3].equals(":~stats") && game != null)
		{
			if(game.getPostgame() != null)
				inputThread.sendMessage(inputThread.getChannel(), "Game over");
			else{
				inputThread.sendMessage(inputThread.getChannel(), "The following people are still alive:");
		    	StringBuilder livingPeople = new StringBuilder();
		    	for (Player p : game.getPlayerList())
		    	{
		    		if (p.isAlive())
		    		{
		    			if(livingPeople.length() > 0)
		    				livingPeople.append(", ");
		    			livingPeople.append(p);
		    		}
		    	}
		    	inputThread.sendMessage(inputThread.getChannel(), livingPeople.toString());
			}
		}
		else if(msg.length >= 4 && msg[3].equals(":~join") && game != null) 
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].equals(":~end") && game != null) 
		{
			if(user.equals(game.getGameStarter()))
			{
				inputThread.sendMessage(inputThread.getChannel(), "Mafia game ended!");
				game.stopTimer();
				game = null;
			}
			else
				inputThread.sendMessage(inputThread.getChannel(), "The game can only be ended by " + game.getGameStarter());
		}
		else if(msg.length >= 4 && msg[3].equals(":~quit") && game != null)
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].equals(":~start") && game != null)
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].startsWith(":~lynch") && game != null)
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].startsWith(":~nolynch") && game != null)
		{
			game.receiveMessage(in);
		}
		else if(msg.length >= 4 && msg[3].startsWith(":~unvote") && game != null)
		{
			game.receiveMessage(in);
		}
	}
	
	public void timerMessage()
	{
		game.stopTimer();
	}
	
}