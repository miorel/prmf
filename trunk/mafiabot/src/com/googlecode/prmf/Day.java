package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public class Day implements MafiaGameState {
	VoteTracker tracker;
	Player[] players;
	boolean killed; // if anyone was killed the previous night
	String dead; // who was killed , if anyone;
	IOThread inputOutputThread; 
	//TODO: Day, and other classes, have inputOutputThreads, inputThreads, and IOThreads. we need to pick a name and keep it

	public Day(Player[] players, IOThread inputThread) {
		tracker = new VoteTracker(players);
		this.players = players;
		this.inputOutputThread = inputThread;
	}
    //TODO: why is this receiving an IO thread? one was given in the constructor
	public boolean receiveMessage(Game game, String line, IOThread inputThread) {
		boolean ret = false;
		String speaker = line.substring(1, line.indexOf("!"));
		int returnCode;
		if((returnCode = parseMessage(line, speaker, inputThread)) >= 0) {
			inputThread.sendMessage(inputThread.getChannel(), players[returnCode] + " was lynched :(");
			players[returnCode].setAlive(false);
			ret = true;
		}
		else if(returnCode == -2) {
			inputThread.sendMessage(inputThread.getChannel(), "The majority has voted for no lynching today!");
			ret = true;
		}
		else if(returnCode == -1) {
			ret = false;
		}
		else if(returnCode == -3) {
			inputThread.sendMessage(inputThread.getChannel(), "What?");
			ret = false;
		}
		else if(returnCode == -4) {
			inputThread.sendMessage(inputThread.getChannel(), speaker + " has been removed from the game!");
		}
		// Must handle all cases of parseMessage return such as -3,-2,-1, >=0

		// TODO actually, it would be better to use enums
		// http://java.sun.com/docs/books/tutorial/java/javaOO/enum.html
		System.err.println(ret);
		if (ret)
			game.swapState(new Night(players, inputThread));
		return ret;
	}
        
	private int searchPlayers(String name) {
		int ret = -3;
		
		// TODO replace with the sexier for-each syntax
		for(int i = 0; i < players.length; ++i) {
			if(players[i].getName().equals(name)) { 
				ret = i;
				break;
			}
		}
		return ret;
	}
    //TODO: why is this receiving an IO thread? one was given in the constructor
    private int parseMessage(String instruc, String speaker, IOThread inputThread)
    {
    	// TODO this method looks like a perfect application of Java enums
    	int ret = -3;
	    String[] instrucTokens = instruc.split(" ",5);
	    String command = instrucTokens[3];
	    String target="";
	    if(command.equals(":~lynch"))
	    {
	    	if(instrucTokens.length >= 5)
	    		target = instrucTokens[4];
	    	else
	    		return -3;
	    }
	    int speakerId = searchPlayers(speaker);
	    int targetId = searchPlayers(target);
	    
	    //should be impossible though
	    if(speakerId == -3)
	    	return -3;
	    
	    // TODO I hope you realize there are better ways to do this than a bunch of if/else statements.
	    //TODO: change this to use the Class.forName() method. I think that's a much nicer solution than the one we have
	    if( command.equals(":~lynch") )
	    {
	    	ret = processVote(speakerId, targetId, inputThread);
	    }
	    else if( command.equals(":~nolynch") )
	    {
	    	ret = processVote(speakerId, -2,inputThread);
	    
	    }
	    else if( command.equals(":~unvote") )
	    {
	    	ret = processVote(speakerId, -1,inputThread);
	    }
	    else if( command.equals(":~quit") )
	    {
	    	//kill speaker
	    	players[speakerId].setAlive(false);
	    	tracker.status(inputThread);
	    	ret = -4;
	    }
	    
	    return ret;
    }
   
    //TODO: why is this receiving an IO thread? one was given in the constructor
    private int processVote(int voter, int voted, IOThread inputThread)
    {
    	/** int voted values:
    	 *  -4 , player quit command ..
    	 *  -3 , voted player does not exist
    	 *  -2 , vote to nolynch
    	 *  -1 , command to retract vote
    	 *  i>=0 , player ID
    	 */ 
    	
    		//check to make sure voted name exists
    		if(voted == -3)
    			return -3;
    		
    		return tracker.newVote( voter, voted , inputThread);   
    		/**return values:   -3 , bad vote, not processed
    		 * 					-2, +1 nolynch
    		 * 					-1 , no majority
    		 * 					 i>=0  , i lynched.
			 */
    }
    
    //
    public void status()
    {
    	inputOutputThread.sendMessage(inputOutputThread.getChannel(), "It is now day!");
    	inputOutputThread.sendMessage(inputOutputThread.getChannel(), "The following people are still alive:");
    	StringBuilder livingPeople = new StringBuilder();
    	for (Player p : players)
    	{
    		if (p.isAlive())
    		{
    			if(livingPeople.length() > 0)
    				livingPeople.append(", ");
    			livingPeople.append(p);
    		}
    	}
    	inputOutputThread.sendMessage(inputOutputThread.getChannel(), livingPeople.toString());
    }
    
    public void swapState(Game game, MafiaGameState newState)
	{
		game.setState(newState);
		game.isOver();
		game.getState().status();
	}
    
    class LynchAction implements Action {

    	private int voter;
    	private int voted;
    	public LynchAction(int voter, int voted)
    	{
    		this.voter=voter;
    		this.voted=voted;
    	}
    	public void handle() {
    		tracker.newVote(voter, voted, inputOutputThread);
    	}

    }
}


