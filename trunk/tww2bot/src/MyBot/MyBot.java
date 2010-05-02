package MyBot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import PircAPI.PircBot;
import PircAPI.User;

public class MyBot extends PircBot {
	private boolean joining = false;
	private List<String> playerList = new ArrayList<String>();
	private boolean inGame = false;
	private String myName = "TestTWW2Bot";
    private String ownName = "TWW2";
	private String[] chans = {"#ufpt", "#TWW2"};
	private int[] finchCount;
	private long[] finchTime = new long[2];
    private long time = System.currentTimeMillis();
    private int[] jokeInfo;
    private String[][] JOKE_MES;
    private List<String> chansAmOp = new ArrayList<String>();
    
    public MyBot() {
        this.setName(myName);
    	JOKE_MES = new String[][] {
    		new String[] {"yoojuss", "yoojuss lossduhgame!"},
    		new String[] {"nobody", ""},
    		new String[] {"orange", "Orange you glad I didn't program the whole joke?"},
	    	new String[] {"under", "Ha ha! I made you say underwe... wait..."},
	    	new String[] {"figs", "Figs your doorbell! It's broken!"},
	    	new String[] {"TWW2Bot", getMyVersion()},
    	};
    }
    
    public void onConnect()
    {
    	//opList.add(ownName);
    	//voiceList.add(ownName);
    	jokeInfo = new int[2];
    	finchCount = new int[2];
    }
    
    public void onMessage(String channel, String sender, String login, String hostname, String message) 
    {
    	//Easter egg
        if (message.equalsIgnoreCase("!mafia") && !joining && !inGame)
        {
        	playerList = new ArrayList<String>();
        	sendMessage(channel, "Mafia game started, please join!");
        	joining = true;
        }
        
        else if (message.equalsIgnoreCase("!quit") && joining)
        {
        	if(isOnList(sender, playerList))
        	{
        		sendMessage(channel, sender + ": You have quit!");
        		playerList.remove(sender);
        	}
        }
        
        else if (message.equalsIgnoreCase("!join") && joining) 
        {
        	if(!isOnList(sender, playerList))
        	{
        		sendMessage(channel, sender + ": You have joined!");
        		playerList.add(sender);
        	}
        	
        }
        
        else if (message.equalsIgnoreCase("!start") && joining)
        {
        	if(playerList.size() == 0)
        	{
        		sendMessage(channel, "You can't play a game with no players!");
        		
        	} 
        	else if(playerList.size() < 3)
        	{
        		sendMessage(channel, "There is 1 mafia and " + (playerList.size() - 1) + " citizens.");
        		sendMessage(channel, "The mafia have won!");
        		joining = false;
        		inGame = false;
        	} 
        	else
        	{
        		int mafia = playerList.size() / 3;
        		sendMessage(channel, "The game has started! There are " + mafia + " mafia and " + (playerList.size() - mafia) + " citizens.!");
        		sendMessage(channel, "Night has fallen!");
        		joining = false;
        		inGame = true;
        		for(int i = 0; i < playerList.size(); i++)
        		{
        			sendMessage(playerList.get(i), "You are a citizen, a member of the town! You do not have a night action.");
        		}
        	}
        }
        else if (message.equalsIgnoreCase("!end") && (joining || inGame))
        {
        	sendMessage(channel, "The game has ended!");
        	joining = false;
        	inGame = false;
        }
        
        //Version message
        else if (message.equalsIgnoreCase("!version"))
        {
        	sendMessage(channel, getMyVersion());
        }
        
        //Finchley Crap
        else if (containsIgnoreCase(message, "finchley central"))
        {
        	int chan = stringArrayIndexOf(chans, channel);
        	finchCount[chan]++;
        	finchTime[chan] = System.currentTimeMillis();
        }
        
        else if (message.equalsIgnoreCase("!finchCount"))
        {
        	int chan = stringArrayIndexOf(chans, channel);
        	sendMessage(channel, "The current finchCount is " + finchCount[chan] + " .");
        }
        
        else if (message.equalsIgnoreCase("!finchTime"))
        {
        	int chan = stringArrayIndexOf(chans, channel);
        	if(finchTime[chan] == 0) {
        		sendMessage(channel, "I have not seen a finching.");
        	} else {
        		
        		long passedTime = System.currentTimeMillis() - finchTime[chan];
        		passedTime = passedTime / 1000;
        		String prefix = "The last finching was ";
        		String mes = toDayHourMinSec(passedTime, prefix, " ago.");
        		sendMessage(channel, mes);
        	}
        }
        
        //Help message
        else if (message.equalsIgnoreCase("!help"))
        {
        	sendMessage(channel, "Available commands are !HELP !FINCHTIME !FINCHCOUNT !MAFIA !ROLL !JOKE !VERSION and !UPTIME");
        	sendMessage(channel, "Mafia commands are !mafia, !join, !quit, !start and !end. In game commands will be messaged to you.");
        }
        
        //Useful things
        else if (message.equalsIgnoreCase("!roll"))
        {
        	int roll = getRandom(1,20);
        	sendMessage(channel, sender + " rolls a " + roll);
        }
        
        //Just Joking around
        else if (message.equalsIgnoreCase("!joke"))
        {
        	int prevJoke = jokeInfo[0];
        	do jokeInfo[0] = getRandom(0, 5);
        	while(prevJoke == jokeInfo[0]);
        	jokeInfo[1] = 1;
        	sendMessage(channel, "Knock knock!");      		
        }
        
        else if (jokeInfo[1] == 1 && message.equalsIgnoreCase("who's there?"))
        {
        	if(JOKE_MES[jokeInfo[0]][0].length() > 0)
        		sendMessage(channel, JOKE_MES[jokeInfo[0]][0]);
        	jokeInfo[1] = 2;
        }
        
        else if (jokeInfo[1] == 2 && message.equalsIgnoreCase(JOKE_MES[jokeInfo[0]][0] + " who?"))
        {
        	if(JOKE_MES[jokeInfo[0]][1].length() > 0)
        		sendMessage(channel, JOKE_MES[jokeInfo[0]][1]);
        	jokeInfo[1] = 0;
        }
        
        
        
		else if(message.equalsIgnoreCase("!uptime")) {
			long passedTime = System.currentTimeMillis() - time;
			passedTime = passedTime / 1000;
			String prefix = "I have been active for ";
			String mes = toDayHourMinSec(passedTime, prefix, ".");
			sendMessage(channel, mes);

		}
		else if(message.equalsIgnoreCase("!downtime")) {
			long passedTime = time;
			passedTime = passedTime / 1000;
			String prefix = "I have been inactive for ";
			String mes = toDayHourMinSec(passedTime, prefix, ".");
			sendMessage(channel, mes);

		}
		else if(message.equalsIgnoreCase("!hammertime")) {
			sendMessage(channel, "Can't touch this.");
		}

		else if(sender.equalsIgnoreCase(ownName) && isOnList(channel, chansAmOp)) {
			if(message.equalsIgnoreCase("!op")) {
				op(channel, sender);
			}
		}
        
        
        
        //CHANOP COMMANDS

/**CHANOP DISABLED
        else if (isOnList(channel, chansAmOp))
        {
        	if (message.equalsIgnoreCase("!Op"))
        	{
        		if(isOnList(sender, opList))
        			op(channel, sender);
        		else
        			sendMessage(sender, "You do not have Op privileges.");
        	}
        	else if (message.equalsIgnoreCase("!Voice"))
        	{
        		if(isOnList(sender, voiceList))
        			voice(channel, sender);
        		else
        			sendMessage(sender, "You do not have Voice privileges.");
        	}
        	else if (message.equalsIgnoreCase("!DeOp"))
        	{
        		deOp(channel, sender);
        	}
        	else if (message.equalsIgnoreCase("!DeVoice"))
        	{
        		deVoice(channel, sender);
        	}
        	else if (isOnList(sender,opList) && message.equalsIgnoreCase("!modOn"))
        	{
        		setMode(channel, "+m");
        	}
        	else if (isOnList(sender,opList) && message.equalsIgnoreCase("!modOff"))
        	{
        		setMode(channel, "-m");
        	}
        }
*/
        
    }
    
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		if(sender.equalsIgnoreCase(ownName)) {
			if(message.equalsIgnoreCase("partall")) {
				String[] chans = getChannels();
				for(int i = 0; i < chans.length; ++i) {
					if(!chans[i].equalsIgnoreCase("#TWW2"))
						partChannel(chans[i]);
				}
			}
        	
        	else if(message.equalsIgnoreCase("quitServer"))
        		quitServer();
        	
        	else if(message.substring(0,4).equalsIgnoreCase("join"))
        		joinChannel(message.substring(5));
        		
        	else if(message.substring(0,4).equalsIgnoreCase("part"))
        		partChannel(message.substring(5));
        	
        	else if(message.substring(0,6).equalsIgnoreCase("allbut")) //allbut hua #ufpt message
        	{
        		String[] mes = message.split(" ");
        		if(mes[2].charAt(0) == '#')
        		{
        			User[] useList = getUsers(mes[2]);
        			String theMes = mes[3];
        			for(int i = 4; i < mes.length; i++)
        			{
        				theMes = theMes + " " + mes[i];
        			}
        			for(int i=0; i<useList.length; i++)
        			{
        				
        				String nam = useList[i].getNick();
        				if(nam != mes[1] && nam != sender)
        					sendMessage(nam, theMes);
        			}
        		}
        		else 
        		{
        			sendMessage(sender, "The proper syntax is: allbut nick #chan message");
        		}
        	}
        	else if(message.charAt(0) == '#')
        	{
        		int firstSpace = message.indexOf(' ');
        		sendMessage(message.substring(0,firstSpace),message.substring(firstSpace + 1));
        	}
        	else if(message.substring(0,3).equalsIgnoreCase("msg"))
        	{
        		int firstSpace = message.indexOf(' ', 4);
        		sendMessage(message.substring(4,firstSpace),message.substring(firstSpace + 1));
        	}
        }
    }

    public void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    	if(recipient.equalsIgnoreCase(myName))
    	{
    		chansAmOp.add(channel);
    	}
    } 

    	
	public void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
		if(recipient.equalsIgnoreCase(myName)) {
			chansAmOp.remove(channel);
		}
	}

	public void onDisconnect() {
		dispose();
	}
    
    /*
     * HELPER METHODS
     */
	public String toDayHourMinSec(long passedTime, String prefix, String suffix) {
		int days = (int) passedTime / 86400;
		int hours = (int) passedTime % 86400 / 3600;
		int mins = (int) passedTime % 3600 / 60;
		int secs = (int) passedTime % 60;
		boolean andSecs = false;
		String mes = prefix;
		if(days > 1) {
			mes = mes + days + " days, ";
			andSecs = true;
		}
		else if(days == 1) {
			mes = mes + days + " day, ";
			andSecs = true;
		}
		if(hours > 1) {
			mes = mes + hours + " hours, ";
			andSecs = true;
		}
		else if(hours == 1) {
			mes = mes + hours + " hour, ";
			andSecs = true;
		}
		if(mins > 1) {
			mes = mes + mins + " minutes, ";
			andSecs = true;
		}
		else if(mins == 1) {
			mes = mes + mins + " minute, ";
			andSecs = true;
		}
		if(andSecs) {
			mes = mes + "and ";
		}

		mes += secs + (secs == 1 ? " second" : " seconds");
		mes += suffix;

		return mes;
	}
    
    private boolean containsIgnoreCase(String mes, String sub) {
    	return mes.toLowerCase(Locale.ENGLISH).contains(sub.toLowerCase(Locale.ENGLISH));
    }
    
	private int getRandom(int low, int high) {
		Random generator = new Random();
		int roll = generator.nextInt(high - low + 1) + low;
		return roll;
	}

	private int stringArrayIndexOf(String[] array, String obj) {
		return Arrays.asList(array).indexOf(obj);
	}

	private boolean isOnList(String name, List<String> list) {
		return list.contains(name);
	}

	private String getMyVersion() {
		String myV = "TWW2Bot version 0.1.2 running ";
		String theirV = getVersion();
		return myV + theirV;
	}
}
