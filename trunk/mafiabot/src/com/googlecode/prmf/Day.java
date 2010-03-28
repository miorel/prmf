package com.googlecode.prmf;

//TODO press Ctrl+Shift+O to have Eclipse make imports nice for you.
import java.util.*;
//TODO Everything in the java.lang package is automatically imported.
import java.lang.Thread;

public class Day extends Thread{
		Thread input;
		TimerThread timerThread;// TimerThread will cease input thread before ending
        VoteTracker tracker;
        Player[] players;
        boolean killed; //if anyone was killed the previous night
        String dead; // who was killed , if anyone;
        Scanner in; // Should be a scanner which is from main class.
        
        public Day(int time, Player[] players,boolean killed, String dead, Scanner in)
        {
        		this.killed = killed;
        		this.dead = dead;
        		this.in = in;
        		tracker = new VoteTracker(players);
            	this.players = players;
            	
        		input = new Thread(this);
        		timerThread = new TimerThread(input);
				input.start(); 
        }
        
        public void run()
        {
        	System.out.println("Morning welcome message");
        	if(killed) //TODO We might have ladies playing this game at some point. Messages shouldn't be gender-specific.
        		System.out.println(dead+ " was found dead in his home this morning!!");
        	while(in.hasNextLine())
        	{
        		String speaker = "testPlayer"; // HOW TO GET SPEAKER? .. must read up on..
        		String instruc = in.nextLine();
        		if(parseMessage(instruc, speaker) >= 0)
        		{
        			//... say who died
        			break;         			// Finchley Central
        		}
        		// Must handle all cases of parseMessage return such as -3,-2,-1, >=0
        		
        		// TODO actually, it would be better to use enums
        		// http://java.sun.com/docs/books/tutorial/java/javaOO/enum.html
        	}

    		//TODO this won't actually do anything unless the inputThread knows
    		// to check for interrupts.
        	timerThread.timer.interrupt();
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



