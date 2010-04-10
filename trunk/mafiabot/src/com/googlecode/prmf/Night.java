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
	
	public boolean receiveMessage(Game game, String line, IOThread inputThread)
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
		{
			resolveNightActions();
			game.swapState(game.getDay());
		}
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
			p.getRole().resolveNightAction();	
	}
	
	public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
		game.getState().status();
	}
	
	public void status()
	{
		inputThread.sendMessage(inputThread.getChannel(), "It is now night!");
	}
	
	
}
