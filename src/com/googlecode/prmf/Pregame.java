package com.googlecode.prmf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.prmf.starter.IOThread;

public class Pregame implements MafiaGameState {
	private String startName;
	private List<Player> players;
	private List<Role> townRoles;
	private List<Role> mafiaRoles;
	private List<Role> roles;
	IOThread inputThread;
	boolean dayStart;

	public Pregame(String startName, IOThread inputThread) {
		this.startName = startName;
		players = new ArrayList<Player>();
		townRoles = new ArrayList<Role>();
		mafiaRoles = new ArrayList<Role>();
		roles = new ArrayList<Role>();
		dayStart = true;
		this.inputThread = inputThread;
	}

	// TODO change return type to enum for day-start, night-start
	// REBUTTAL that sounds like a hack to me
	//we wont need this to-do if the swapState() i'm thinking of is the one we implement
	public boolean receiveMessage(Game game, String line, IOThread inputThread)
	{
			boolean ret = false;
			String[] msg = line.split(" ", 4);
			String user = "";

			//this is kind of a nasty solution...

			if(msg[0].indexOf("!")>1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			Player temp = new Player(user);
			int index = players.indexOf(temp);
			
			String destination = msg[2];
			String command = msg[3].toLowerCase();
			if(command.equalsIgnoreCase(":~start"))
			{
				if(user.equals(startName))		
				{
					inputThread.sendMessage(destination, "The game has begun!");
					startGame(game, inputThread);
					ret = true;
				}
				else
					inputThread.sendMessage(destination,  "Only " + startName + " can start the game!");
			}	
			if(command.equalsIgnoreCase(":~join"))
			{
				System.out.println(index);
				if(index == -1)
				{
					players.add(new Player(user));
					inputThread.sendMessage(destination,  user + " has joined the game!");
				}
				else
					inputThread.sendMessage(destination,  user + " has already joined the game!");
			}
			
			if(command.equalsIgnoreCase(":~quit"))
			{
				System.out.println(index);
				if(index == -1)
					inputThread.sendMessage(destination, user + " is not part of the game!");
				else
				{
					players.remove(index);
					inputThread.sendMessage(destination,  user + " has quit the game!");
				}
				
			}	
			if (ret)
			{
				if (dayStart)
					game.swapState(new Day(getPlayerArray(), inputThread));
				else
					game.swapState(new Night(getPlayerArray(), inputThread));
			}
			return ret;
	}	
	
	private void startGame(Game game, IOThread inputThread)
	{
		MafiaTeam mt = new MafiaTeam();
		Town t = new Town();
		//assigning roles
		int numMafia = (int)Math.ceil(players.size()/3.0);
		//create the Mafia team
		for(int a = 0; a < numMafia; ++a)
		{
			mafiaRoles.add(new Mafia(mt));
		}
		
		//create the Town team
		for(int a = 0; a < (players.size() - numMafia); ++a)
		{
			townRoles.add(new Citizen(t));
		}
		
		roles.addAll(mafiaRoles);
		roles.addAll(townRoles);
		
		Collections.shuffle(roles);
		
		for(int a = 0; a < players.size(); ++a)
		{
			Player p = players.get(a);
			
			p.setRole(roles.get(a));
			p.getRole().getTeam().addPlayer(p); //this seems kinda sloppy, any better way of doing this?
			inputThread.sendMessage(players.get(a).getName(), p.getRole().description());
		}
	}

	public Player[] getPlayerArray() // it's a player array now, hope you're happy ;p
	{
		return players.toArray(new Player[0]);
	}
	
	public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
		/*if(dayStart)
		{
			if (game.getDay() == null)
				game.makeDay();
			game.setState(game.getDay());
		}
		else
		{
			if (game.getNight() == null)
				game.makeNight();
			game.setState(game.getNight());
		}*/
		game.getState().status();
	}
	
	public void status()
	{
		inputThread.sendMessage(inputThread.getChannel(), "The following players are currently in:");
		StringBuilder playersIn = new StringBuilder();
		for (Player p : players)
		{
			if(playersIn.length() > 0)
				playersIn.append(", ");
			playersIn.append(p);
		}
		inputThread.sendMessage(inputThread.getChannel(), playersIn.toString());
		if(dayStart)
			inputThread.sendMessage(inputThread.getChannel(), "This game is currently set to day start");
		else
			inputThread.sendMessage(inputThread.getChannel(), "This game is currently set to night start");
	}
}