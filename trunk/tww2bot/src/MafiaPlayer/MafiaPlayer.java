package MafiaPlayer;

import java.util.ArrayList;

import PircAPI.PircBot;

public class MafiaPlayer extends PircBot {
    
    private String gameStarter;
    private String mafiaBot;
    
    public MafiaPlayer(String myName) 
    {
    	this.setName(myName);
    }
    
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) 
    {
 		if(channel.equalsIgnoreCase("#ufpt"))
 		{
 			if(containsIgnoreCase(message, "Mafia game started by "))
 			{
 				gameStarter = message.substring(22, message.length() - 1);
 				mafiaBot = sender;
 				sendMessage("#ufpt", "~join");
 			} 
 			else if (containsIgnoreCase(message, "The game is now over"))
 			{
 				gameStarter = "";
 			}
 			else if (containsIgnoreCase(message, "~nolynch") && sender.equalsIgnoreCase(gameStarter))
 			{
 				sendMessage("#ufpt", "~nolynch");
 			}
 			else if (containsIgnoreCase(message, "~lynch") && sender.equalsIgnoreCase(gameStarter))
 			{
 				sendMessage("#ufpt", message);
 			}
 			
		} else if (channel.equalsIgnoreCase("#TWW2") && message.equalsIgnoreCase("disconnect"))
		{
			quitServer();
		}
    }
    
    public void onPrivateMessage(String sender, String login, String hostname, String message)
    {
		sendMessage("#TWW2", message);
		if(containsIgnoreCase(message, "night:"))
		{
			sendMessage(mafiaBot, message.substring(6));
		} 
		else if(containsIgnoreCase(message, "day:"))
		{
			sendMessage("#ufpt", message.substring(4));
		}
    }
    
    
    public void onDisconnect()
    {
    	dispose();
    }
    
    public static boolean containsIgnoreCase(String mes, String sub)
    {
    	int lengthDiff = mes.length() - sub.length();
    	for(int i = 0; i<=lengthDiff; i++)
    	{
    		if(mes.substring(i,i+sub.length()).equalsIgnoreCase(sub))
    		{
    			return true;
    		}
    	}
    	return false;
    }

    public int stringArrayIndexOf(String[] array, String obj)
    {
    	for(int i=0; i < array.length; i++)
    	{
    		if(array[i].equalsIgnoreCase(obj))
    			return i;
    	}
    	return -1;
    }
    
    public boolean isOnList(String name, ArrayList<String> list)
    {
    	for(int i=0; i < list.size(); i++)
    	{
    		if(list.get(i).equalsIgnoreCase(name))
    		{
    			return true;
    		}
    	}
    	return false;
    }

}
