package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;


public class Night implements MafiaGameState
{
	private Player[] players;
	private IOThread inputThread;
	private boolean[] actionComplete;
	
	public Night(Player[] players, IOThread inputThread)
	{
		this.players = players;
		this.inputThread = inputThread;
		actionComplete = new boolean[players.length];
	}
	
//	TODO add a timer to night
	
	public boolean receiveMessage(String line, IOThread inputThread)
	{
		if (isNightOver())
			return true;
		
		//temporary bad solution until we get around to overhauling the command system
		String[] splitLine = line.split(" ");
		if (splitLine.length <= 4)
			return false;
		String speaker = splitLine[0].substring(1,line.indexOf("!"));
		String action = splitLine[3];
		String target = splitLine[4];
		
		
		Player speaking = null;
		for (int i=0;i<players.length;i++)
		{
			if (players[i].equals(new Player(speaker)))
			{
				speaking = players[i];
				break;
			}
			if (i == players.length-1)
				return false;
		}
		
		if (speaking.getRole().hasNightAction())
		{
			speaking.getRole().nightAction(action + " " + target, players);
			
			inputThread.sendMessage(inputThread.getChannel(), "oh god you did a night action~");
		}
		
		for(int i = 0; i < players.length; ++i) {
			if(players[i].equals(speaking)) {
				actionComplete[i] = true;
				break;
			}
		}
		boolean isOver = isNightOver();
		if (isOver)
			resolveNightActions();
		return isOver;
	}
	
	public void introduction(IOThread inputThread)
	{
		for (Player player : players)
		{
			if (player.isAlive())
			{
				inputThread.sendMessage(player.getName(), player.getRole().description());
			}
		}
	}
	
	public boolean isNightOver()
	{
		boolean result = true;
		for (int i=0;i<players.length;i++)
			if(players[i].isAlive() && !actionComplete[i] && players[i].getRole().hasNightAction())
				result = false; 
		return result;
	}
	
	public void resolveNightActions()
	{
		for (Player p : players)
		{
			// TODO I encourage you to read http://www.pragprog.com/articles/tell-dont-ask
			// but basically what you want to do here is to tell EVERY role to resolveNightAction()
			// if one of them doesn't have one, it will simply do nothing 
			if (p.getRole().hasNightAction())
			{
				p.getRole().resolveNightAction();
			}
		}
	}
	
	public void swapState(Game game)
	{
		//TODO: send a message to game telling it to switch to day
	}
	
	
}
