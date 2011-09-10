
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

class SPOJBot extends Thread {

	private static final HashSet<String> OWNERS = new HashSet<String>();
	
	static {
		OWNERS.add("naonao");
		OWNERS.add("tww2");
		OWNERS.add("r_g");
	}
	
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
			this.sendMessage("R_G", "Error reading users");

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
		sendMessage(target, name +  " was added.");
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
			if(OWNERS.contains(sender.toLowerCase()) && message.equalsIgnoreCase("KILL"))
			{
				return false;
			}
			target = sender;
		}
		
		message = message.trim();
		
		// Special case: If the message contains a !spojlink or !sl command, treat it as a request for a spoj link.
		String lowerCaseMessage = message.toLowerCase();
		if ((lowerCaseMessage.contains("!spojlink")) || (lowerCaseMessage.contains("!sl"))) {
			int start = lowerCaseMessage.indexOf("!sl");
			start = start < 0 ? lowerCaseMessage.indexOf("!spojlink") : start;
			String[] tokens = lowerCaseMessage.substring(start, lowerCaseMessage.length()).split("\\s+");
			// Only consider the !sl/!spojlink inline command valid if the problem ID is in all caps (to prevent unwanted spojlinks).
			if (tokens.length > 1 && (message.indexOf(tokens[1].toUpperCase(), start) == message.toUpperCase().indexOf(tokens[1].toUpperCase(), start)))
				message = tokens[0] + " " + tokens[1];
		}
		
		if(message.charAt(0) == '!')
		{
			String[] param = message.substring(1).split(" +");
			
			/************************************************
			*				TWW2Bot Commands				*
			************************************************/

			if(param[0].equalsIgnoreCase("help"))
			{
				// **************************************************************************
				// *****                          HELP SECTION                          *****
				// **************************************************************************
				if(param.length == 1) {
					this.sendMessage(target, "My commands are: HELP, BESTSPOJ, DOSOMESPOJ, GOTOPRACTICE, HASSOLVED, LASTSOLVE, LASTNSOLVES, LASTSUB, MAXSTREAK, POINTS, SL, SPOJ, SPOJADAY, SPOJAWEEK, SPOJANHOUR, STREAK, SPOJLINK, WORSTSPOJ, and KARMABOMB.");
					this.sendMessage(target, "Type \"!help [cmd]\" for more help.");
				}
				else {
					
					//SO META
					if(param[1].equalsIgnoreCase("help")) {
						this.sendMessage(target, "I think you already got it...");
					}
					else if(param[1].equalsIgnoreCase("bestspoj")) {
						this.sendMessage(target, "Shows the UFPTer who has most recently solved a problem and the problem that was solved.");
					}
					else if(param[1].equalsIgnoreCase("dosomespoj")) {
						this.sendMessage(target, "Tells slackers to do more spoj.");
					}
					else if(param[1].equalsIgnoreCase("gotopractice")) {
						this.sendMessage(target, "Tells slackers to go to practice.");
					}
					else if (param[1].equalsIgnoreCase("hassolved")) {
						this.sendMessage(target, "Syntax is \"!hassolved [problem ID]\". Shows a list of the UFPTers who have and have not solved the problem specified.");
					}
					else if (param[1].equalsIgnoreCase("lastsolve")) {
						this.sendMessage(target, "Syntax is \"!lastsolve [username]\". Shows the last solved problem for the specified user.");
					}
					else if (param[1].equalsIgnoreCase("lastnsolves")) {
						this.sendMessage(target, "Syntax is \"!lastNsolves [username]\", where N is a number between 2 and 10. Shows the last N solved problems for the specified user.");
					}
					else if (param[1].equalsIgnoreCase("lastsub")) {
						this.sendMessage(target, "Syntax is \"!lastsub [username]\". Shows the last submission time and result for the specified user.");
					}
					else if(param[1].equalsIgnoreCase("maxstreak")) {
						this.sendMessage(target, "Shows each UFPTer's max streak.");
					}
					else if (param[1].equalsIgnoreCase("points")) {
						this.sendMessage(target, "Syntax is \"!points [username]\". Shows the number of points the specified user has.");
					}
					else if(param[1].equalsIgnoreCase("spoj")) {
						this.sendMessage(target, "Syntax is \"!spoj [username] [info]\". Supported info parameters are LASTSUB and LASTSOLVE.");
						//TODO: Add RANK and UFRANK
						if (param.length > 2) {
							if (param[2].equalsIgnoreCase("lastsub")) {
								this.sendMessage(target, "Syntax is \"!spoj [username] lastsub\". Shows the last submission time and result for the specified user.");
							}
							else if (param[2].equalsIgnoreCase("lastsolve")) {
								this.sendMessage(target, "Syntax is \"!spoj [username] lastsolve\". Shows the last solved problem for the specified user.");
							}
						}
					}
					else if(param[1].equalsIgnoreCase("spojaday")) {
						this.sendMessage(target, "Shows a list of the UFPTers who have and have not solved a spoj problem today.");
					}
					else if(param[1].equalsIgnoreCase("spojaweek")) {
						this.sendMessage(target, "Shows a list of the UFPTers who have and have not solved a spoj problem this week.");
					}
					else if(param[1].toLowerCase().matches("spojan?hour")) {
						this.sendMessage(target, "Shows a list of the UFPTers who have and have not solved a spoj problem this hour.");
					
					}
					else if(param[1].equalsIgnoreCase("streak")) {
						this.sendMessage(target, "Shows how many consecutive days each UFPTer has spojadayed.");
					}
					else if(param[1].toLowerCase().matches("spojlink|sl")) {
						this.sendMessage(target, "Syntax is \"!" + param[1].toLowerCase() + " [problem ID]\". For lazy people who don't want to have to type out simple problem URLs.");
					}
					else if(param[1].equalsIgnoreCase("worstspoj")) {
						this.sendMessage(target, "Shows the UFPTer who has least recently solved a problem and the problem that was solved.");
					}
					
					//TROLL HELP OPTIONS
					else if(param[1].equalsIgnoreCase("karmabomb")) {
						this.sendMessage(target, "Pretty self explanatory, really...");
					}
					else if(param[1].equalsIgnoreCase("huabot")) {
						this.sendMessage(target, "~help");
						this.sendMessage(target, "He can do that himself");
					}
					else if(param[1].equalsIgnoreCase("maubot")) {
						this.sendMessage(target, "LOL");
					}
					else if(param[1].equalsIgnoreCase("[cmd]")) {
						this.sendMessage(target, "Well, aren't YOU Mr. Literal?");
					}
					else {
						this.sendMessage(target, "I can't help you with " + param[1] + ".");
					}
				}
				// **************************************************************************
				// *****                       END HELP SECTION                         *****
				// **************************************************************************
			}
			// **************************************************************************
			// *****                       COMMANDS SECTION                         *****
			// **************************************************************************
			else if(param[0].equalsIgnoreCase("bestspoj") || param[0].equalsIgnoreCase("pro")) {
				this.sendMessage(target, getLastSpoj());
			}
			else if(param[0].equalsIgnoreCase("dosomespoj")) {
				this.sendNotice("DO SOME SPOJ, YOU SLACKERS!!!");
			}
			else if(param[0].equalsIgnoreCase("gotopractice")) {
				this.sendNotice("GO TO PRACTICE, YOU SLACKERS!!!");
			}
			else if(param[0].equalsIgnoreCase("hasSolved")) {
				if(param.length < 2)
					this.sendMessage(target, "I need a problem code!");
				else
				{
					String[][] lists = hasSolved(param[1].toUpperCase());
					this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[0], "has solved " + param[1].toUpperCase() + ".", "have solved " + param[1].toUpperCase() + "."));
					this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[1], "has not.", "have not."));
				}
			}
			else if(param[0].equalsIgnoreCase("lastsolve")) {
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastsolve [userName]\".");
				else
					this.sendMessage(target, getLastSpojSolve(param[1]));
			}
			else if(param[0].toLowerCase().matches("last(?:10|[2-9])solves")) {
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastnsolves [userName]\".");
				else
					this.sendMessage(target, getLastNSpojSolves(param[1], Integer.valueOf(param[0].toLowerCase().replaceAll("last","").replaceAll("solves",""))));
			}
			else if(param[0].equalsIgnoreCase("lastsub")) {
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastsub [userName]\".");
				else
					this.sendMessage(target, getLastSpojSub(param[1]));
			}
			else if(param[0].equalsIgnoreCase("maxstreak")) {
				String[] streaks = maxstreak();
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(streaks, "is the max streak.", "are the max streaks.", "There are no max streaks available."));
			}
			else if (param[0].equalsIgnoreCase("points")) {
				if (param.length < 2) {
					this.sendMessage(target, "Syntax is \"!points [username]\".");
				}
				else {
					this.sendMessage(target, getPoints(param[1]));
				}
			}
			else if(param[0].equalsIgnoreCase("spoj")) {
				if(param.length < 2) {
					this.sendMessage(target, "Syntax is \"!spoj [username] [info]\". Type \"!help spoj\" for more info");
				}
				else {
					String user = param[1];
					if(param.length < 3 || param[2].equalsIgnoreCase("lastsub")) {
						this.sendMessage(target, this.getLastSpojSub(user));
					}
					else if(param[2].equalsIgnoreCase("lastsolve")) {
						this.sendMessage(target, this.getLastSpojSolve(user));
					}
					else {
						this.sendMessage(target, "That command is not yet supported!");
					}
				}
			}
			else if(param[0].equalsIgnoreCase("spojaday")) {
				String[][] lists = spojaday();
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[0], "has spojadayed!", "have spojadayed!", "No one has spojadayed! You're all slackers!!! D:"));
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[1], "only has " + Utility.getTimeTillMidnight() + " left!", "only have " + Utility.getTimeTillMidnight() + " left!", "Great work, UFPT!"));
			}
			else if(param[0].equalsIgnoreCase("spojaweek")) {
				String[][] lists = spojaweek();
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[0], "has spojaweeked!", "have spojaweeked!", "No one has spojaweeked! You're all terrible people!!! D:<"));
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[1], "is a terrible human being and only has " + Utility.getDaysTillSunday() + " and " + Utility.getTimeTillMidnight() + " left!",
						"are terrible human beings and only have " + Utility.getDaysTillSunday() + " and " + Utility.getTimeTillMidnight() + " left!", "Way to not slack, UFPT!"));
			}
			else if(param[0].toLowerCase().matches("spojan?hour")) {
				String[][] lists = spojahour();
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[0], "is a superior human being!", "are superior human beings!", "No one has spojanhoured! None of you are superior human beings!"));
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[1], "only has " + Utility.getTimeTillHour() + " left!", "only have " + Utility.getTimeTillHour() + " left!", "We're going to win the SER at this rate!"));
			}
			else if(param[0].equalsIgnoreCase("streak")) {
				String[] streaks = streak();
				this.sendMessage(target, Utility.toGrammaticallyCorrectString(streaks, "is the current streak.", "are the current streaks.", "There are no current streaks available."));
			}
			else if(param[0].equalsIgnoreCase("spojlink") || param[0].equalsIgnoreCase("sl")) {
				if(param.length < 2)
				{
					this.sendMessage( target, "Syntax is \"!" + param[0].toLowerCase() + " [problem ID]\"");
				} else {
					this.sendMessage( target, "http://www.spoj.pl/problems/" + param[1].toUpperCase() + "/");
				}
			}
			else if(param[0].equalsIgnoreCase("worstspoj") || param[0].equalsIgnoreCase("noob")) {
				this.sendMessage(target, getWorstSpojer());
			}
			else if(param[0].equalsIgnoreCase("karmabomb")) {
				if(param.length < 2)
				{
					this.sendMessage( target, "Syntax is \"!karmabomb [target]\"" );
				} else {
					this.sendMessage( target, "Not cool, man.... --" + sender);
				}
			}
			else if(param[0].equalsIgnoreCase("sau")) {
				if(param.length < 2) { } 
				else {
					addName(sender, param[1]);
				}
			}
			else if(param[0].equalsIgnoreCase("sru")) {
				if(param.length < 2) { } 
				else {
					removeName(sender, param[1]);
				}
			}
			// **************************************************************************
			// *****                     END COMMANDS SECTION                       *****
			// **************************************************************************
			
		}
		return true;
	}
	
	private String getPoints(String user) {
		try {
			BufferedReader in = Utility.getUserInfoStream(user);
			String input, toReturn = null;
			Pattern pointsPattern = Pattern.compile("\\(\\s*[0-9.]*\\s*points\\s*\\)");
			while ((input = in.readLine()) != null) {
				Matcher pointsMatcher = pointsPattern.matcher(input);
				if (pointsMatcher.find()) {
					toReturn = input.substring(pointsMatcher.start() + 1, pointsMatcher.end() - 1).trim();
					break;
				}
			}
			in.close();
			if (toReturn == null) {
				return ("User " + user + " was not found on SPOJ.");
			}
			return (user + " has " + toReturn + ".");
		} catch(IOException e) {
			return("User " + user + " was not found on SPOJ.");
		}
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
			
			return(user + "'s last submission got " + retCode + " on " + prob + " on " + strDate + ".");
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
			
			return(user + "'s last solved classical problem was " + prob + " on " + strDate + ".");
		} catch(IOException e) {
			return("User " + user + " was not found on SPOJ.");
		}
		
	}
	
	/** Returns the last n solved SPOJ problems in a comma-separated formatted message
	 * 	or an error message if the SPOJ user was not found or if n > 10 or < 2. If the user
	 *  has solved less than n problems, the function returns a message containing all
	 *  SPOJ problems solved by the user. 
	 */
	private String getLastNSpojSolves(String user, int n) {
		if (n > 10 || n < 2) {
			return "n must be between 2 and 10.";
		}
		try {
			BufferedReader in = Utility.getUserSolveStream(user);
			Set<String> pCodes = new LinkedHashSet<String>(); 
			String line = "";
			for(int i=0; i<9; ++i)
			{
				line = in.readLine();
			}
			String[] info = line.split(" *\\| *");
			int actualN = 0;
			while (actualN < n) {
				line = in.readLine();
				info = line.split(" *\\| *");
				while(info.length >= 5 && !info[4].equalsIgnoreCase("AC"))
				{
					line = in.readLine();
					info = line.split(" *\\| *");
				}
				
				if(info.length < 5)
				{
					if (actualN == 0)
						return("User " + user + " has no correct submissions.");
					else
						break;
				}
				
				if (!pCodes.contains(info[3])) {
					pCodes.add(info[3]);
					++actualN;
				}
			}
			in.close();
			String[] probCodes = pCodes.toArray(new String[0]);
			String toReturn;
			if (actualN > 1) {
				toReturn = user + "'s last " + actualN + " solved classical problems were: ";
				if (actualN > 2) {
					for (int i = 0; i < actualN - 1; ++i)
						toReturn += probCodes[i] + ", ";
				}
				else {
					toReturn += probCodes[0] + " ";
				}
				toReturn += "and " + probCodes[actualN - 1] + ".";
			}
			else {
				toReturn = getLastSpojSolve(user);
			}
			return toReturn;
		} catch(IOException e) {
			return("User " + user + " was not found on SPOJ.");
		}
	}

	private String[][] spojaday()
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
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}
	
	private String[] streak()
	{
		List<String> streak = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			streak.add(u.getName() + "{" + u.getCurSpojStreak() + "}");
			
		}
		return streak.toArray(new String[0]);
	}
	
	private String[] maxstreak()
	{
		List<String> streak = new ArrayList<String>();
		
		for(User u : users)
		{
			u.update();
			streak.add(u.getName() + "{" + u.getMaxSpojStreak() + "}");
			
		}
		return streak.toArray(new String[0]);
	}
	
	private String[][] spojaweek()
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
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}
	
	private String[][] spojahour()
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
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}
	
	
	public String getLastSpoj()
	{
		String ret = "naonao1234 is a superior human being.";
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
		String ret = "naonao1234 is a terrible person.";
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
	
	public String[][] hasSolved(String prob)
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
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
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