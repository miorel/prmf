package com.googlecode.prmf.gamePlay;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.prmf.connection.IOThread;

public class Pregame implements MafiaGameState {
	static private MafiaTeam mafiaTeam = new MafiaTeam();
	static private Town town = new Town();
	private String startName;
	private boolean profileLoaded;
	private List<Player> players;
	private List<Role> townRoles;
	private List<Role> mafiaRoles;
	private List<Role> roles;
	IOThread inputThread; // TODO why is this not private?
	private boolean dayStart;

	public Pregame(String startName, IOThread inputThread) {
		this.startName = startName;
		players = new ArrayList<Player>();
		townRoles = new ArrayList<Role>();
		mafiaRoles = new ArrayList<Role>();
		roles = new ArrayList<Role>();
		dayStart = true;
		this.inputThread = inputThread;
		profileLoaded = false;
	}

    //TODO: why is this receiving an IO thread? one was given in the constructor
	public boolean receiveMessage(Game game, String line, IOThread inputThread)
	{
			boolean ret = false;
			String[] msg = line.split(" ", 4);
			String user = "";

			//this is kind of a nasty solution...

			if(msg[0].indexOf("!")>1)
				user = msg[0].substring(1,msg[0].indexOf("!"));
			
			if(msg[1].startsWith("KICK") || msg[1].startsWith("PART") || msg[1].startsWith("QUIT") )
			{
				for(int i=0;i<players.size();++i)
				{
					if(players.get(i).getName().equals(user))
					{
						players.remove(i);
						return false;
					}
				}
			}
			
			if(msg[1].startsWith("NICK") )
			{
				changeNick(user,msg[2]);
				return false;
			}
			
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
					game.setState(new Day(getPlayerArray(), inputThread));
				else
					game.setState(new Night(getPlayerArray(), inputThread));
			}
			return ret;
	}	
	private void changeNick(String oldNick , String newNick)
	{
		for(int i=0;i<players.size();++i)
		{
			if(players.get(i).getName().equals(oldNick))
			{
				players.get(i).setName(newNick.substring(1));
				return;
			}
		}
	}
    //TODO: why is this receiving an IO thread? one was given in the constructor
	private void startGame(Game game, IOThread inputThread)
	{	
		game.setProgress(true);
		if(!profileLoaded)
		{
			defaultStart(game,inputThread);
			return;
		}
		
		Collections.shuffle(roles);
		for(int a = 0; a < players.size(); ++a)
		{
			Player p = players.get(a);
			
			p.setRole(roles.get(a));
			p.getRole().getTeam().addPlayer(p); //this seems kinda sloppy, any better way of doing this?
												//yes, do it from within setRole()
			inputThread.sendMessage(players.get(a).getName(), p.getRole().description());
		}
	}
	
	private void defaultStart(Game game,IOThread inputThread)
	{
		int numMafia = (int)Math.ceil(players.size()/4.0);

		for(int a = 0; a < numMafia; ++a)
		{
			mafiaRoles.add(new Mafia(mafiaTeam));
		}
		
		townRoles.add(new Cop(town));
		townRoles.add(new Doctor(town));
		//create the Town team
		for(int a = 0; a < (players.size() - numMafia-2); ++a)
		{
			townRoles.add(new Citizen(town));
		}
		roles.addAll(mafiaRoles);
		roles.addAll(townRoles);
		
		Collections.shuffle(roles);
		
		for(int a = 0; a < players.size(); ++a)
		{
			Player p = players.get(a);
			
			p.setRole(roles.get(a));
			p.getRole().getTeam().addPlayer(p); //this seems kinda sloppy, any better way of doing this?
												//yes, do it from within setRole()
			
		}
		for (Player p : players)
		{
			inputThread.sendMessage(p.getName(), p.getRole().description());
		}
	}
	
	public void loadRoleProfile(String[] roleMsg)
	{
		//Method not working yet
		roles.clear();
		for(int i=0;i<roleMsg.length;++i)
		{
			roleMsg[i] = roleMsg[i].trim();
			String[] roleSplit = roleMsg[i].split(":");
			Pregame pregame = null;
			try
			{
				//how do i fix these warnings
				Class clsBook = Pregame.class.getClassLoader().loadClass("com.googlecode.prmf.Pregame");
				pregame = (Pregame)clsBook.newInstance();

			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.err.println("dont add new shit nooby");
			}
			try
			{
				//TODO there be warnings here, fix them
				Class tempAssigner = Class.forName("com.googlecode.prmf.Pregame$"+roleSplit[1]+"Assigner");
				Method[] allMethods = tempAssigner.getMethods();
				Method getTeam = allMethods[0];
				Object obj = tempAssigner.getDeclaredConstructor(new Class[]{Pregame.class}).newInstance(new Object[]{pregame});
				Team specificTeam = (Team)(getTeam.invoke(obj));
				roles.add( (Role)Class.forName("com.googlecode.prmf."+roleSplit[0]).getConstructor(specificTeam.getClass()).newInstance(specificTeam) );
				profileLoaded = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.err.println("OOP is hard");
			}
		}
	}
	
	public Player[] getPlayerArray() // it's a player array now, hope you're happy ;p
	{
		return players.toArray(new Player[0]);
	}
	
	public boolean getDayStart()
	{
		return dayStart;
	}
	
	public void setDayStart(boolean day)
	{
		dayStart = day;
	}
	
	public void status()
	{
    	if(players.size() >= 1)
    		inputThread.sendMessage(inputThread.getChannel(), "The following people are registered");
    	else
    	{
    		inputThread.sendMessage(inputThread.getChannel(), "There is no one registered yet!");
    		return;
    	}
		StringBuilder playersIn = new StringBuilder();
		for (Player p : players)
		{
			if(playersIn.length() > 0)
				playersIn.append(", ");
			playersIn.append(p);
		}
		inputThread.sendMessage(inputThread.getChannel(), playersIn.toString());
		
		//TODO transform the following to biconditional
		if(dayStart)
			inputThread.sendMessage(inputThread.getChannel(), "This game is currently set to day start");
		else
			inputThread.sendMessage(inputThread.getChannel(), "This game is currently set to night start");
	}
	
	class MafiaTeamAssigner implements Assigner
	{
		public MafiaTeam getTeam()
		{
			return mafiaTeam;
		}
	}
	class TownAssigner implements Assigner
	{
		public Town getTeam()
		{
			return town;
		}
	}
}