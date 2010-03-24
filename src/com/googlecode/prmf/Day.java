package com.googlecode.prmf;

public class Day {
        Timer timer;
        VoteTracker votes;
        Player[] players;
       
        public Day(int time, Player[] players)
        {
                timer = new Timer(time);
                votes = new VoteTracker(players);
                this.players = players;
        }
       
		private int searchPlayers(String name)
		{
		    int ret = -1;
		    for(int i=0;i<players.length; ++i)
		    {
				if(players[i].name.equals(name))
				{
				    ret = i;
				}
		    }
		    return ret;
		}
	 

        public boolean parseMessage(String instruc, String speaker)
        {
        	boolean ret = false;
		    String[] instrucTokens = instruc.split(" ");
		    String command = instrucTokens[0];
		    String target = instrucTokens[1];
		    
		    int speakerId = searchPlayers(speaker);
		    int targetId = searchPlayers(target);
		    
		    //should be impossible though
		    if(speakerId == -1)
		    	return false;
		    
		    if( command.equals("!lynch") )
		    {
		    	players[speakerId].votedFor = targetId; 
		    	ret = true;
		    }
		    else if( command.equals("!nolynch") )
		    {
		    	//assuming the decided 'no decision' value is in fact -1.
		    	players[speakerId].votedFor = -1;
		    	ret = true;
		    }
		    else if( command.equals("!quit") )
		    {
		    	//kill speaker
		    	players[speakerId].isAlive = false;
		    	ret = true;
		    }
		    
		    return ret;
        }
       
        public int processVote()
        {
                return -1;
        }
}



