package com.googlecode.prmf.gamePlay;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.googlecode.prmf.connection.IOThread;

public class Pregame implements MafiaGameState {
	private MafiaTeam mafiaTeam;
	private Town town;
	private String startName;
	private boolean profileLoaded;
	private List<Player> players;
	private List<Role> townRoles;
	private List<Role> mafiaRoles;
	private List<Role> roles;
	private IOThread inputOutputThread;
	private boolean dayStart;

	public Pregame(String startName, IOThread inputOutputThread) {
		this.startName = startName;
		players = new ArrayList<Player>();
		townRoles = new ArrayList<Role>();
		mafiaRoles = new ArrayList<Role>();
		roles = new ArrayList<Role>();
		dayStart = true;
		this.inputOutputThread = inputOutputThread;
		profileLoaded = false;
		mafiaTeam = new MafiaTeam();
		town = new Town();
	}

	public boolean receiveMessage(Game game, String line)
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
			
			String command = msg[3].toLowerCase();
			
			Action action = null;
			
			//TODO: handle with Class.forName(), although I'm not sure how case sensitivity will work with that? =\
			//then we can catch class not found exceptions with a message telling user to see ~help or something
			if(command.equalsIgnoreCase(":~start"))
			{
				ret = true;
				action = new StartAction(user, game);
			}	
			if(command.equalsIgnoreCase(":~join"))
			{
				action = new JoinAction(user, game);
			}
			
			if(command.equalsIgnoreCase(":~quit"))
			{
				action = new QuitAction(user, game);
				
			}	
			
			if (action != null)
				action.handle();
			
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
	
	private void startGame(Game game)
	{	
		game.setProgress(true);
		if(!profileLoaded)
		{
			defaultStart();
			return;
		}
		
		Collections.shuffle(roles);
		for(int a = 0; a < players.size(); ++a)
		{
			Player p = players.get(a);
			
			p.setRole(roles.get(a));
			p.getRole().getTeam().addPlayer(p); //this seems kinda sloppy, any better way of doing this?
												//yes, do it from within setRole()
			inputOutputThread.sendMessage(players.get(a).getName(), p.getRole().description());
		}
	}
	
	private void defaultStart()
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
			inputOutputThread.sendMessage(p.getName(), p.getRole().description());
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
				//that's how!
				Class<?> clsBook = Pregame.class.getClassLoader().loadClass("com.googlecode.prmf.Pregame");
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
				Class<?> tempAssigner = Class.forName("com.googlecode.prmf.Pregame$"+roleSplit[1]+"Assigner");
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
    		inputOutputThread.sendMessage(inputOutputThread.getChannel(), "The following people are registered");
    	else
    	{
    		inputOutputThread.sendMessage(inputOutputThread.getChannel(), "There is no one registered yet!");
    		return;
    	}
		StringBuilder playersIn = new StringBuilder();
		for (Player p : players)
		{
			if(playersIn.length() > 0)
				playersIn.append(", ");
			playersIn.append(p);
		}
		inputOutputThread.sendMessage(inputOutputThread.getChannel(), playersIn.toString());
		inputOutputThread.sendMessage(inputOutputThread.getChannel(), (dayStart?"This game is currently set to day start":"This game is currently set to night start"));
	}
	
	class JoinAction implements Action
	{
		String name;
		Game game;
		public JoinAction(String name, Game game)
		{
			this.name = name;
			this.game = game;
		}
		
		public void handle()
		{
			Player potential = new Player(name);
			int index = players.indexOf(potential);
			if(index == -1)
			{
				players.add(potential);
				inputOutputThread.sendMessage(game.getIOThread().getChannel(), name + " has joined the game!");
			}
			else
				inputOutputThread.sendMessage(game.getIOThread().getChannel(), name + " has already joined the game!");
		}
	}
	
	class QuitAction implements Action
	{
		String name;
		Game game;
		public QuitAction(String name, Game game)
		{
			this.name = name;
			this.game = game;
		}
		
		public void handle()
		{
			Player potential = new Player(name);
			int index = players.indexOf(potential);
			if(index == -1)
				inputOutputThread.sendMessage(game.getIOThread().getChannel(), name + " is not part of the game!");
			else
			{
				players.remove(index);
				inputOutputThread.sendMessage(game.getIOThread().getChannel(), name + " has quit the game!");
			}
		}
	}
	
	class StartAction implements Action
	{
		String name;
		Game game;
		public StartAction(String name, Game game)
		{
			this.name = name;
			this.game = game;
		}
		
		public void handle()
		{
			if(name.equals(startName))		
			{
				inputOutputThread.sendMessage(game.getIOThread().getChannel(), "The game has begun!");
				startGame(game);
				//TODO: maybe move this, and the changes at the end of the day/night, to the appropriate constructors?
				//that way we won't have any extraneous chances after the end of the game, plus we can put it
				//in two places only instead of 3 ^_^
				if (dayStart)
				{
					for(Player p : game.getPlayerList())
					{
						if(p.isAlive())
							inputOutputThread.sendMessage("MODE",inputOutputThread.getChannel(), "+v "+p.getName());
					}
					game.setState(new Day(getPlayerArray(), inputOutputThread));
				}
				else
				{
					for(Player p : game.getPlayerList())
					{
						inputOutputThread.sendMessage("MODE",inputOutputThread.getChannel(), "-v "+p.getName());
					}
					game.setState(new Night(getPlayerArray(), inputOutputThread));
				}
				game.startTimer();
			}
			else
				inputOutputThread.sendMessage(game.getIOThread().getChannel(),  "Only " + startName + " can start the game!");
		}
	}
	
	public void endState(Game game)
	{
		
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