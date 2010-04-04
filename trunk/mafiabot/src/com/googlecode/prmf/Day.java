package com.googlecode.prmf;

import com.googlecode.prmf.starter.IOThread;

public class Day implements MafiaGameState{
		//Thread input;
		// TimerThread will cease input thread before ending
        VoteTracker tracker;
        Player[] players;
        boolean killed; //if anyone was killed the previous night
        String dead; // who was killed , if anyone;
        IOThread inputThread;
        
        public Day(Player[] players , IOThread inputThread)
        {
        		tracker = new VoteTracker(players);
            	this.players = players;
        		
        }
        
        
        public boolean receiveMessage(String line, IOThread inputThread)
        {
    		boolean ret = false;
    	
    		String speaker = line.substring(1,line.indexOf("!"));
    		int returnCode;
    		if( (returnCode = parseMessage(line, speaker , inputThread)) >= 0)
    		{
    			inputThread.sendMessage("#UFPT",players[returnCode] + " was lynched :(");
    			players[returnCode].isAlive = false;
    			ret = true;
    		}
    		else if(returnCode == -2)
    		{
    			inputThread.sendMessage("#UFPT","the majority has voted for no lynching today!");
    			ret = true;
    		}
    		else if(returnCode == -1)
    		{
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
	 

        public int parseMessage(String instruc, String speaker, IOThread inputThread)
        {
        	System.err.println("Entering parse message :" + instruc);
        	// TODO this method looks like a perfect application of Java enums
        	int ret = -3;
		    String[] instrucTokens = instruc.split(" ",5);
		    String command = instrucTokens[3];
		    String target="";
		    if(command.equals(":~lynch"))
		    	target = instrucTokens[4];
		    
		    int speakerId = searchPlayers(speaker);
		    int targetId = searchPlayers(target);
		    
		    //should be impossible though
		    if(speakerId == -3)
		    	return -3;
		    
		    System.err.println(command + " " + target);
		    
		    // TODO I hope you realize there are better ways to do this than a bunch of if/else statements.
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
		    	players[speakerId].isAlive = false;
		    	tracker.status(inputThread);
		    	ret = -3;
		    }
		    
		    return ret;
        }
       
        public int processVote(int voter, int voted, IOThread inputThread)
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
        		
	    		return tracker.newVote( voter, voted , inputThread);   
	    		/**return values:   -3 , bad vote, not processed
	    		 * 					-2, +1 nolynch
	    		 * 					-1 , no majority
	    		 * 					 i>=0  , i lynched.
				 */
        }
}
