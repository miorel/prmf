
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

class SPOJBot extends Thread {

	private static final String OWNER = "naonao";
	private List<User> users = new ArrayList<User>();
  	private Scanner input;
  	private PrintStream output;
	public SPOJBot( Scanner input, PrintStream output ) {
   		this.input = input;
   		this.output = output;
		
		getNames();
  	}
	
	private void getNames()
	{
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("users.txt"));
			String name = in.readLine();
			while(name!= null)
			{
				users.add(new User(name));
				name = in.readLine();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.sendMessage("naonao", "Error reading users");

		}
		Collections.sort(users);
	}
	
	private void setNames()
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter("users.txt"));
			for(int i = 0; i < users.size(); i++)
			{
				if(users.get(i) != null)
					out.write(users.get(i).getName() +"\n");
			}
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
  
	private void addName(String target, String name)
	{
		users.add(new User(name));
		setNames();
		sendMessage(target, name +  " was added");
		Collections.sort(users);
	}
  	
	private void removeName(String target, String name)
	{
		boolean found = false;
		for(User u : users)
		{
			if(u.getName().equals(name))
			{
				found = true;
				users.remove(u);
				break;
			}
			
		}
		setNames();
		if(!found)
		{
			sendMessage(target, name + " could not be found.");
		}
		else
		{
			sendMessage(target, name + " was removed.");
		}
	}
	
	public void run() 
	{
  		String readMessage;
  		boolean notDone = true;
    	while(notDone && input.hasNextLine() && !(readMessage = input.nextLine()).equals("CLOSE") ) {
    		String[] readArray = readMessage.split(":");
    		if(readArray[0].equals("PING "))
    		{
    			output.println("PONG :" + readArray[1]);
    		} else if (readArray.length > 1) {
    			String[] cmdLine = readArray[1].split(" ");
    			if(cmdLine.length > 1)
    			{
    				String sender = cmdLine[0];
    				String command = cmdLine[1];
    				
    				/********************************************************
    				 *														*
    				 *						COMMANDS						*
    				 *														*
    				 ********************************************************/
    				if(command.equals("PRIVMSG"))
    				{
    					sender = sender.substring(0, sender.indexOf('!'));
    					String target = cmdLine[2];
    					String mes = "";
    					for(int i=2; i<readArray.length; ++i)
    					{
    						mes += readArray[i];
    					}
    					
    					System.out.println(sender + "@" + target + ": " + mes);
						notDone = this.processMessage(sender, target, mes);
    				} else {
    				    System.out.println(command + " ~~~ " + readMessage);
    				}
    				
    				/******************END OF COMMANDS *********************/
    				
    			} else {
					System.out.println("NOCOMMAND! ~~~ " + readMessage);
    			}
    		} else {
    			System.out.println("NOCOLON! ~~~ " + readMessage);
    		}
    	}//End While
    	input.close();
    }
	
	private boolean processMessage(String sender, String target, String message)
	{
		if(target.equalsIgnoreCase("SPOJBot"))
		{
			/*********** THIS IS A WHISPER *****************/
			if(sender.equalsIgnoreCase(OWNER) && message.equalsIgnoreCase("KILL"))
			{
				return false;
			}
			target = sender;
		}
		if(message.charAt(0) == '!')
		{
			String[] param = message.substring(1).split(" +");
			
			/************************************************
			*				TWW2Bot Commands				*
			************************************************/
			if(param[0].equalsIgnoreCase("help"))
			{
				if(param.length == 1)
					this.sendMessage(target, "My commands are: HELP, GOTOPRACTICE,SPOJ, SPOJADAY, SPOJAWEEK, SPOJAHOUR,STREAK, MAXSTREAK,SPOJLINK and KARMABOMB; type \"!help [cmd]\" for more help");
				else 
				{
					//SO META
					if(param[1].equalsIgnoreCase("help")) {
						this.sendMessage(target, "I think you already got it...");
						
					//HELP SPOJ
					}else if(param[1].equalsIgnoreCase("gotopractice")) {
						if(param.length > 1)
						{
							this.sendMessage(target,"Tells slackers to go to practice");
						}
					
					}else if(param[1].equalsIgnoreCase("spoj")) {
						if(param.length > 1)
						{
							this.sendMessage(target,"Syntax is \"!spoj [username] [info]\", supported info parameters are: LASTSUB and LASTSOLVE");
							//LASTSOLVE, SCORE, RANK and UFRANK
						}
					
					}else if(param[1].equalsIgnoreCase("spojaday")) {
						this.sendMessage(target, "Shows a list of ufpters who have and who have not solved a spoj problem today.");
					
					}else if(param[1].equalsIgnoreCase("spojaweek")) {
						this.sendMessage(target, "Shows a list of ufpters who have and who have not solved a spoj this week.");
					
					}else if(param[1].equalsIgnoreCase("spojahour")) {
						this.sendMessage(target, "Shows a list of ufpters who have and who have not solved a spoj problem this hour.");
					
					}else if(param[1].equalsIgnoreCase("streak")) {
						this.sendMessage(target, "Shows the how many consecutive days each UFPTer has spojadayed.");
					
					}else if(param[1].equalsIgnoreCase("maxstreak")) {
						this.sendMessage(target, "Shows each UFPTer's maxstreak.");
					
					}else if(param[1].equalsIgnoreCase("spojlink")) {
						this.sendMessage(target, "For lazy people who don't want to have to type out simple problem URLs.");
					
					//TROLL HELP OPTIONS
					}else if(param[1].equalsIgnoreCase("karmabomb")) {
						this.sendMessage(target, "Pretty self explanatory really...");
					}else if(param[1].equalsIgnoreCase("huabot")) {
						this.sendMessage(target, "~help He can do that himself");
					} else if(param[1].equalsIgnoreCase("maubot")) {
						this.sendMessage(target, "LOL");
					} else if(param[1].equalsIgnoreCase("[cmd]")) {
						this.sendMessage(target, "Well aren't you mr. literal?");
					} else {
						this.sendMessage(target, "I can't help you with " + param[1]);
					}
				}
				
			} else if(param[0].equalsIgnoreCase("spoj"))
			{
				if(param.length < 2)
				{
					this.sendMessage(target, "Syntax is \"!spoj [username] [info]\", enter \"!help spoj\" for more info");
				} else {
					String user = param[1];
					if(param.length < 3 || param[2].equalsIgnoreCase("LASTSUB"))
					{
						this.sendMessage(target, this.getLastSpojSub(user));
					} else if(param[2].equalsIgnoreCase("LASTSOLVE")){
						this.sendMessage(target, this.getLastSpojSolve(user));
					} else {
						this.sendMessage(target, "not yet supported!");
					}
				}
			} else if(param[0].equalsIgnoreCase("bestspoj")||param[0].equalsIgnoreCase("pro"))
			{
				this.sendMessage(target, getLastSpoj());
			}  else if(param[0].equalsIgnoreCase("worstspoj")||param[0].equalsIgnoreCase("noob"))
			{
				this.sendMessage(target, getWorstSpojer());
			} else if(param[0].equalsIgnoreCase("lastsolve"))
			{
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastsolve [userName]\"");
				else
					this.sendMessage(target, getLastSpojSolve(param[1]));
			}  else if(param[0].equalsIgnoreCase("lastsub"))
			{
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastsub [userName]\"");
				else
					this.sendMessage(target, getLastSpojSub(param[1]));
			}else if(param[0].equalsIgnoreCase("spojaday"))
			{
				String[] lists = spojaday();
				this.sendMessage(target, lists[0].toString() + " have spojadayed!");
				this.sendMessage(target, lists[1].toString() + " only have " + Utility.getTimeTillMidnight() + " left!");
			}else if(param[0].equalsIgnoreCase("spojaweek"))
			{
				String[] lists = spojaweek();
				this.sendMessage(target, lists[0].toString() + " have spojaweeked!");
				this.sendMessage(target, lists[1].toString() + " are terrible human beings and only have " + Utility.getDaysTillSunday() + " and " + Utility.getTimeTillMidnight() + " left!");
			}   else if(param[0].equalsIgnoreCase("spojahour"))
			{
				String[] lists = spojahour();
				this.sendMessage(target, lists[0].toString() + " are superior human beings!");
				this.sendMessage(target, lists[1].toString() + " only have " + Utility.getTimeTillHour() + " left!");
			}else if(param[0].equalsIgnoreCase("hasSolved"))
			{
				if(param.length < 2)
					this.sendMessage(target, "I need a problem code");
				else
				{
					String[] lists = hasSolved(param[1].toUpperCase());
					this.sendMessage(target, lists[0].toString() + " have solved " + param[1].toUpperCase());
					this.sendMessage(target, lists[1].toString() + " have not");
				}
			}  else if(param[0].equalsIgnoreCase("streak"))
			{
				String streaks = streak();
				this.sendMessage(target, streaks + " are the current streaks.");
			}   else if(param[0].equalsIgnoreCase("maxstreak"))
			{
				String streaks = maxstreak();
				this.sendMessage(target, streaks + " are the max streaks.");
			}else if(param[0].equalsIgnoreCase("spojlink"))
			{
				if(param.length < 2)
				{
					this.sendMessage( target, "Syntax is \"!spojlink [problem ID]\"");
				} else {
					this.sendMessage( target, "http://www.spoj.pl/problems/" + param[1].toUpperCase() + "/");
				}
			} else if(param[0].equalsIgnoreCase("gotopractice"))
			{
				this.sendNotice("GO TO PRACTICE YOU SLACKERS");
			} else if(param[0].equalsIgnoreCase("karmabomb"))
			{
				if(param.length < 2)
				{
					this.sendMessage( target, "Syntax is \"!karmabomb [target]\"" );
				} else {
					this.sendMessage( target, "Not cool man... --" + sender);
				}
			}else if(param[0].equalsIgnoreCase("sau"))
			{
				if(param.length < 2)
				{
				} 
				else
					addName(sender, param[1]);

			}else if(param[0].equalsIgnoreCase("sru"))
			{
				if(param.length < 2)
				{
				} 
				else
					removeName(sender, param[1]);
			
			}
			
		}
		return true;
	}
	
	
	private String getLastSpojSub(String user)
	{
		try {
			BufferedReader in = Utility.getUserSolveStream(user);
			String line = "";
			for(int i=0; i<10; ++i)
			{
				line = in.readLine();
			}
			String[] info = line.split(" *\\| *");
			if(info.length < 5)
			{
				return("User " + user + " has no submissions.");
			}
			String retCode = info[4];
			String prob = info[3];
			String strDate = info[2];
			in.close();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = new GregorianCalendar();
			try
			{
				c.setTime(df.parse(strDate));
				c.add(Calendar.HOUR_OF_DAY, -6);
				strDate = c.getTime().toString();
			} catch(ParseException e)
			{}	
			
			return(user + "'s last submission got " + retCode + " on " + prob + " on " + strDate);
		} catch(IOException e) {
			return("User " + user + " was not found on SPOJ.");
		}
		
	}
	
	private String getLastSpojSolve(String user)
	{
		try {
			BufferedReader in = Utility.getUserSolveStream(user);
			String line = "";
			for(int i=0; i<10; ++i)
			{
				line = in.readLine();
			}
			String[] info = line.split(" *\\| *");
			
			while(info.length >= 5 && !info[4].equalsIgnoreCase("AC"))
			{
				line = in.readLine();
				info = line.split(" *\\| *");
			}
			
			if(info.length < 5)
			{
				return("User " + user + " has no correct submissions.");
			}
			
			String prob = info[3];
			String strDate = info[2];
			in.close();
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = new GregorianCalendar();
			try
			{
				c.setTime(df.parse(strDate));
				c.add(Calendar.HOUR_OF_DAY, -6);
				strDate = c.getTime().toString();
			} catch(ParseException e)
			{}	
			
			return(user + "'s last classical problem solved was " + prob + " on " + strDate);
		} catch(IOException e) {
			return("User " + user + " was not found on SPOJ.");
		}
		
	}
	
	private String[] spojaday()
	{
		List<String> haveSpojadayed = new ArrayList<String>();
		List<String> notSpojadayed = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			if(u.getSpojDay() > 0)
			{
				haveSpojadayed.add(u.getName() + "{" + u.getSpojDay() + "}");
			} else {
				notSpojadayed.add(u.getName());
			}
		}
		String[] ret = new String[2];
		ret[0] = "";
		ret[1] = "";
		for(String s : haveSpojadayed)
		{
			ret[0] += s + ", ";
		}
		for(String s : notSpojadayed)
		{
			ret[1] += s + ", ";
		}
		return ret;
		
	}
	
	private String streak()
	{
		List<String> streak = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			streak.add(u.getName() + "{" + u.getCurSpojStreak() + "}");
			
		}
		String ret = "";
		for(String s : streak)
		{
			ret += s + ", ";
		}
		return ret;
		
	}
	
	private String maxstreak()
	{
		List<String> streak = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			streak.add(u.getName() + "{" + u.getMaxSpojStreak() + "}");
			
		}
		String ret = "";
		for(String s : streak)
		{
			ret += s + ", ";
		}
		return ret;
		
	}
	
	private String[] spojaweek()
	{
		List<String> haveSpojadayed = new ArrayList<String>();
		List<String> notSpojadayed = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			if(u.getSpojWeek() > 0)
			{
					haveSpojadayed.add(u.getName() + "{" + u.getSpojWeek() + "}");
			} else {
				notSpojadayed.add(u.getName());
			}
		}
		String[] ret = new String[2];
		ret[0] = "";
		ret[1] = "";
		for(String s : haveSpojadayed)
		{
			ret[0] += s + ", ";
		}
		for(String s : notSpojadayed)
		{
			ret[1] += s + ", ";
		}
		return ret;
		
	}
	
	private String[] spojahour()
	{
		List<String> haveSpojadayed = new ArrayList<String>();
		List<String> notSpojadayed = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			if(u.getSpojHour() > 0)
			{
				haveSpojadayed.add(u.getName() +"{" +  u.getSpojHour() + "}");
			} else {
				notSpojadayed.add(u.getName());
			}
		}
		String[] ret = new String[2];
		ret[0] = "";
		ret[1] = "";
		for(String s : haveSpojadayed)
		{
			ret[0] += s + ", ";
		}
		for(String s : notSpojadayed)
		{
			ret[1] += s + ", ";
		}
		return ret;
		
	}
	
	
	public String getLastSpoj()
	{
		String ret = "naonao1234 is a superior human being";
		Date bestDate = Utility.getDateOfLastSolve("naonao1234");
		for(int i = 0 ; i < users.size();i++)
		{
			Date check = Utility.getDateOfLastSolve(users.get(i).getName());
			//System.out.println(check + ":" + spojadayUsers.get(i) + ":" + check.compareTo(bestDate));
			if(check.compareTo(bestDate)>0){
				ret = users.get(i) + " is the best spojer. " + Utility.getLastSpojSolve(users.get(i).getName());
				bestDate = check;
			}
		}
		return ret;
	}
	
	public String getWorstSpojer()
	{
		String ret = "naonao1234 is a terrible person";
		Date worstDate = Utility.getDateOfLastSolve("naonao1234");
		for(int i = 0 ; i < users.size();i++)
		{
			Date check = Utility.getDateOfLastSolve(users.get(i).getName());
			if(check.compareTo(worstDate)<0){
				worstDate = check;
				ret = users.get(i) + " is a huge slacker. " + Utility.getLastSpojSolve(users.get(i).getName());
			}
		}
		return ret;
	}
	
	public String[] hasSolved(String prob)
	{
		List<String> haveSpojadayed = new ArrayList<String>();
		List<String> notSpojadayed = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			if(u.hasSolved(prob))
			{
				haveSpojadayed.add(u.getName());
			} else {
				notSpojadayed.add(u.getName());
			}
		}
		String[] ret = new String[2];
		ret[0] = "";
		ret[1] = "";
		for(String s : haveSpojadayed)
		{
			ret[0] += s + ", ";
		}
		for(String s : notSpojadayed)
		{
			ret[1] += s + ", ";
		}
		return ret;
	}
	
	
	private void sendMessage(String target, String message)
	{
		output.println("PRIVMSG " + target + " :" + message);
		System.out.println("PRIVMSG " + target + " :" + message);
	}
	
	
	
	private void sendNotice(String message)
	{
		output.println("NOTICE " + "#ufpt" + " :"  + message);
	}
}