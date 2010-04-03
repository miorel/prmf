package com.googlecode.prmf;

import com.googlecode.prmf.starter.InputThread;

public class Day extends Thread implements MafiaGameState{
		Thread input;
		TimerThread timerThread;// TimerThread will cease input thread before ending
        VoteTracker tracker;
        Player[] players;
        boolean killed; //if anyone was killed the previous night
        String dead; // who was killed , if anyone;
        InputThread inputThread;
        
        public Day(Player[] players)
        {
        		tracker = new VoteTracker(players);
            	this.players = players;
            	this.inputThread = inputThread;
        }
        
        public void startDay(boolean killed, String dead)
        {
    		this.killed = killed;
    		this.dead = dead;
    		input = new Thread(this);
    		timerThread = new TimerThread(input);
			input.start(); 
        }
        
        public boolean receiveMessage(String line, InputThread inputThread)
        {
        	boolean ret = false;
        	inputThread.sendMessage("#UFPT","Morning welcome message");
        	
        	if(killed)
        		inputThread.sendMessage("#UFPT",dead+ " was found dead in his/her home this morning!!");
        	
        		String speaker = "testPlayer"; // HOW TO GET SPEAKER? .. must read up on..
        		String instruc = line;
        		int returnCode;
        		if( (returnCode = parseMessage(instruc, speaker)) >= 0)
        		{
        			inputThread.sendMessage("#UFPT",players[returnCode] + " was lynched :(");
        			ret = true;
        		}
        		else if(returnCode == -2)
        		{
        			inputThread.sendMessage("#UFPT","the majority has voted for no lynching today!");
        			ret = true;
        		}
        		else if(returnCode == -1)
        		{
        			inputThread.sendMessage("#UFPT", "The town was not able to reach a concensus.");
        			ret = false;
        		}
        		else if(returnCode == -3)
        		{
        			ret = false;
        		}
        		// Must handle all cases of parseMessage return such as -3,-2,-1, >=0
        		
        		// TODO actually, it would be better to use enums
        		// http://java.sun.com/docs/books/tutorial/java/javaOO/enum.html
        		
        		return ret;
        	}

        
        
		private int searchPlayers(String name)
		{
		    int ret = -3;
		    for(int i=0;i<players.length; ++i)
		    { 
				if(players[i].name.equals(name))
				{
				    ret = i;
				    // TODO you don't want to keep searching if you've found the correct one  
				}
		    }
		    return ret;
		}
	 

        public int parseMessage(String instruc, String speaker)
        {
        	// TODO this method looks like a perfect application of Java enums
        	int ret = -3;
		    String[] instrucTokens = instruc.split(" ");
		    String command = instrucTokens[0];
		    String target = instrucTokens[1];
		    
		    int speakerId = searchPlayers(speaker);
		    int targetId = searchPlayers(target);
		    
		    //should be impossible though
		    if(speakerId == -3)
		    	return -3;
		    
		    if( command.equals("!lynch") )
		    {
		    	ret = processVote(speakerId, targetId);
		    }
		    else if( command.equals("!nolynch") )
		    {
		    	ret = processVote(speakerId, -2);
		    
		    }
		    else if( command.equals("!unvote") )
		    {
		    	ret = processVote(speakerId, -1);
		    }
		    else if( command.equals("!quit") )
		    {
		    	//kill speaker
		    	players[speakerId].isAlive = false;
		    	tracker.status();
		    	ret = -3;
		    }
		    
		    return ret;
        }
       
        public int processVote(int voter, int voted)
        {
        	/** int voted values:
        	 *  -3 , voted player does not exist
        	 *  -2 , vote to nolynch
        	 *  -1 , command to retract vote
        	 *  i>=0 , player ID
        	 */ 
        	
        		//check to make sure voted name exists
        		if(voted == -3)
        			return -3;
        		
	    		return tracker.newVote( voter, voted );   
	    		/**return values:   -3 , bad vote, not processed
	    		 * 					-2, +1 nolynch
	    		 * 					-1 , no majority
	    		 * 					 i>=0  , i lynched.
				 */
        }
}



