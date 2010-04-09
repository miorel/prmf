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

	public Pregame(String startName) {
		this.startName = startName;
		players = new ArrayList<Player>();
		townRoles = new ArrayList<Role>();
		mafiaRoles = new ArrayList<Role>();
		roles = new ArrayList<Role>();
	}

	// TODO change return type to enum for day-start, night-start
	// REBUTTAL that sounds like a hack to me
	//we wont need this to-do if the swapState() i'm thinking of is the one we implement
	public boolean receiveMessage(String line, IOThread inputThread)
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
					startGame(inputThread);
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
			return ret;
	}	
	
	private void startGame(IOThread inputThread)
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
			players.get(a).setRole(roles.get(a));
			inputThread.sendMessage(players.get(a).getName(), "your role is " + roles.get(a).getName());
		}
		
		//TODO tell game that a day or night needs to start? should this method have a return type?
		// I don't think it should. Just have this class set the next state itself. --melic
	}

	public Player[] getPlayerList() // TODO not exactly a player LIST, is it? :P
	{
		return players.toArray(new Player[0]);
	}
	
	public void swapState(Game game)
	{
		//TODO: send game a message telling it which state to start on
	}
}