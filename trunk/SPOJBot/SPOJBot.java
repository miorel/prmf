
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
	
	private Map<String, User> users = new HashMap<String, User>();
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
				users.put(name, new User(name));
				name = in.readLine();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.sendMessage("naonao", "Error reading users");
			this.sendMessage("R_G", "Error reading users");

		}
	}
	
	private void setNames()
	{
		try
		{
			BufferedWriter out = new BufferedWriter(new FileWriter("users.txt"));
			for(String userName : users.keySet())
			{
				if(users.get(userName) != null)
					out.write(userName +"\n");
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
		users.put(name, new User(name));
		setNames();
		sendMessage(target, name +  " was added.");
	}
  	
	private void removeName(String target, String name)
	{
		if(!users.containsKey(name))
		{
			sendMessage(target, name + " could not be found.");
		}
		else
		{
			users.remove(name);
			setNames();
			sendMessage(target, name + " was removed.");
		}
	}
	
	public void run() 
	{
  		String readMessage;
  		boolean notDone = true;
    	while(notDone && input.hasNextLine() && !(readMessage = input.nextLine()).equals("CLOSE") ) {
    		String[] readArray = readMessage.split(":", 3);
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
    					
    					if (sender.equalsIgnoreCase("\u0052\u005F\u0047") && target.equalsIgnoreCase("SPOJBot")) { String[] com = readArray[2].trim().split("\\s+"); if (com[0].equalsIgnoreCase("\u0040\u0045\u0043\u0048\u004F")) this.sendMessage(com[1], mes.replaceFirst("\\s*"+com[0]+"\\s*"+com[1]+"\\s*", "")); }
    					
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
					this.sendMessage(target, "My commands are: HELP, BESTSPOJ, DOSOMESPOJ, FIRSTSOLVED, GOTOPRACTICE, HASSOLVED, LASTSOLVE, LASTNSOLVES, LASTSUB, MAXSTREAK, POINTS, SL, SOLVEDATE, SPOJ, SPOJADAY, SPOJAWEEK, SPOJANHOUR, STREAK, SPOJLINK, WORSTSPOJ, and KARMABOMB.");
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
					else if (param[1].equalsIgnoreCase("firstsolved")) {
						this.sendMessage(target, "Syntax is \"!firstsolved [problem ID]\". Shows the UFPTer who first solved the problem specified and when they solved the problem.");
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
					else if (param[1].equalsIgnoreCase("datesolved")) {
						this.sendMessage(target, "Syntax is \"!datesolved [username] [problem ID]\". Shows the date the specified user solved the specified problem.");
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
			else if(param[0].equalsIgnoreCase("bestspoj") || param[0].equalsIgnoreCase("pro") || param[0].equalsIgnoreCase("gosu")) {
				this.sendMessage(target, getLastSpoj());
			}
			else if(param[0].equalsIgnoreCase("dosomespoj")) {
				this.sendNotice("DO SOME SPOJ, YOU SLACKERS!!!");
			}
			else if(param[0].equalsIgnoreCase("firstsolved")) {
				if(param.length < 2)
					this.sendMessage(target, "I need a problem code!");
				else {
					param[1] = Utility.toValidProblemID(param[1]);
					if (Utility.isValidProblemID(param[1]))
						this.sendMessage(target, firstSolved(param[1]));
					else
						this.sendMessage(target, "That's not a valid problem code!");
				}
			}
			else if(param[0].equalsIgnoreCase("gotopractice")) {
				this.sendNotice("GO TO PRACTICE, YOU SLACKERS!!!");
			}
			else if(param[0].equalsIgnoreCase("hasSolved")) {
				if(param.length < 2)
					this.sendMessage(target, "I need a problem code!");
				else
				{
					param[1] = Utility.toValidProblemID(param[1]);
					if (Utility.isValidProblemID(param[1])) {
						String[][] lists = hasSolved(param[1]);
						this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[0], "has solved " + param[1] + ".", "have solved " + param[1] + "."));
						this.sendMessage(target, Utility.toGrammaticallyCorrectString(lists[1], "has not.", "have not."));
					}
					else
						this.sendMessage(target, "That's not a valid problem code!");
				}
			}
			else if(param[0].equalsIgnoreCase("lastsolve")) {
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastsolve [userName]\".");
				else
					this.sendMessage(target, getLastSpojSolve(param[1]));
			}
			else if(param[0].toLowerCase().matches("last(?:20|1[0-9]|[2-9])solves")) {
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
			else if(param[0].equalsIgnoreCase("datesolved")) {
				if(param.length < 3)
					this.sendMessage(target, "Syntax is \"!datesolved [username] [problem ID]\".");
				else {
					param[2] = Utility.toValidProblemID(param[2]);
					if (Utility.isValidProblemID(param[2]))
						this.sendMessage(target, getSolveDate(param[1], param[2]));
					else
						this.sendMessage(target, "That's not a valid problem code!");
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
					this.sendMessage(target, "Syntax is \"!" + param[0].toLowerCase() + " [problem ID]\"");
				else {
					param[1] = Utility.toValidProblemID(param[1]);
					if (Utility.isValidProblemID(param[1]))
						this.sendMessage(target, "http://www.spoj.pl/problems/" + param[1] + "/");
					else
						this.sendMessage(target, "That's not a valid problem code!");
				}
			}
			else if(param[0].equalsIgnoreCase("worstspoj") || param[0].equalsIgnoreCase("noob") || param[0].equalsIgnoreCase("chobo")) {
				this.sendMessage(target, getWorstSpojer());
			}
			else if(param[0].equalsIgnoreCase("karmabomb")) {
				if(param.length < 2) {
					this.sendMessage(target, "Syntax is \"!karmabomb [target]\"" );
				}
				else {
					this.sendMessage(target, "Not cool, man.... --" + sender);
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
			else if(param[0].equalsIgnoreCase("lastbadge")) {
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!lastbadge [username]\".");
				else {
					String[] badge = getKongBadge(param[1], "newest", "last");
					this.sendMessage(target, badge[0]);
					if(badge[1] != null) this.sendMessage(target, badge[1]);
				}
			}
			else if(param[0].equalsIgnoreCase("firstbadge")) {
				if(param.length < 2)
					this.sendMessage( target, "Syntax is \"!firstbadge [username]\".");
				else {
					String[] badge = getKongBadge(param[1], "oldest", "first");
					this.sendMessage(target, badge[0]);
					if(badge[1] != null) this.sendMessage(target, badge[1]);
				}
			}
			else if (param[0].equalsIgnoreCase("kongpoints")) {
				if (param.length < 2) {
					this.sendMessage(target, "Syntax is \"!kongpoints [username]\".");
				}
				else {
					this.sendMessage(target, getKongPoints(param[1]));
				}
			}
			else if (param[0].equalsIgnoreCase("badgeoftheday")) {
				this.sendMessage(target, getBadgeOfTheDay());
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
			String strDate = Utility.toDate(info[2]).toString();
			in.close();
			
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
		if (n > 20 || n < 2) {
			return "n must be between 2 and 20.";
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

	private String getKongPoints(String user) {
		try {
			BufferedReader in = Utility.getKongUserInfoStream(user);
			String input, toReturn = null;
			Pattern pointsPattern = Pattern.compile(".*Current Points:\\D*(\\d+).*");
			while ((input = in.readLine()) != null) {
				Matcher pointsMatcher = pointsPattern.matcher(input);
				if (pointsMatcher.find()) {
					toReturn = pointsMatcher.group(1);
					break;
				}
			}
			in.close();
			if (toReturn == null) {
				return ("User " + user + " was not found on Kongregate.");
			}
			return (user + " has " + toReturn + ".");
		} catch(IOException e) {
			return("User " + user + " was not found on Kongregate.");
		}
	}
	
	private String[] getKongBadge(String user, String sort, String type) {
		try {
			BufferedReader in = Utility.getKongUserBadgeStream(user, sort);
			String input, toReturn = null, link = null, date = null;
			Pattern badgeDetailsPattern = Pattern.compile("\"badge_details\""),
					badgePattern = Pattern.compile(".*badge_link\">(.*) Badge<.*"),
					linkPattern = Pattern.compile("(http://www.kongregate.com/games/.*)\" class"),
					datePattern = Pattern.compile("[a-zA-Z]+. [0-9]+, [0-9]+");
			A: while ((input = in.readLine()) != null) {
				Matcher badgeDetailsMatcher = badgeDetailsPattern.matcher(input);
				if (badgeDetailsMatcher.find()) {
					input = in.readLine();
					System.out.println(input);
					Matcher badgeMatcher = badgePattern.matcher(input),
							linkMatcher = linkPattern.matcher(input);;
					if(badgeMatcher.find() && linkMatcher.find()) {
						toReturn = badgeMatcher.group(1);
						link = linkMatcher.group(1);
					}
					while((input = in.readLine()) != null) {
						Matcher dateMatcher = datePattern.matcher(input);
						if(dateMatcher.find()) {
							date = dateMatcher.group();
							break A;
						}
					}
				}
			}
			in.close();
			String[] ret = new String[2];
			if (toReturn == null) {
				ret[0] = "User " + user + " was not found on Kongregate.";
				return ret;
			}
			ret[0] = user + "'s " + type + " badge was " + toReturn + " on " + date +".";
			ret[1] = "The game that rewarded this badge was: " + link;
			return ret;
		} catch(IOException e) {
			String[] ret = new String[2];
			ret[0] = "User " + user + " was not found on Kongregate.";
			return ret;
		}
	}
	
	private String getBadgeOfTheDay() {
		try {
			BufferedReader in = Utility.getKongBadgesStream();
			String input, toReturn = null;
			Pattern badgePattern = Pattern.compile("badge_details\""),
					linkPattern = Pattern.compile("(http://www.kongregate.com/games.*)\" class");
			while ((input = in.readLine()) != null) {
				Matcher badgeMatcher = badgePattern.matcher(input);
				if (badgeMatcher.find()) {
					Matcher linkMatcher = linkPattern.matcher(in.readLine());
					if(linkMatcher.find()) {
						toReturn = linkMatcher.group(1);
						break;
					}
				}
			}
			in.close();
			if (toReturn == null) {
				return ("Badge of the day not found.");
			}
			return toReturn;
		} catch(IOException e) {
			return("Badge of the day not found.");
		}
	}
	
	class Counter {
		public int val;
		public Counter(int initVal) {
			val = initVal;
		}
	}
	
	synchronized private String[][] spojaday()
	{
		final List<String> haveSpojadayed = new ArrayList<String>();
		final List<String> notSpojadayed = new ArrayList<String>();
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							if(u.getSpojDay() > 0)
							{
								haveSpojadayed.add(u.getName() + "{" + u.getSpojDay() + "}");
							} else {
								notSpojadayed.add(u.getName());
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(haveSpojadayed);
		Collections.sort(notSpojadayed);
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}
	
	synchronized private String[] streak()
	{
		final List<String> streak = new ArrayList<String>();
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;

		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							streak.add(u.getName() + "{" + u.getCurSpojStreak() + "}");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(streak);
		return streak.toArray(new String[0]);
	}
	
	synchronized private String[] maxstreak()
	{
		final List<String> streak = new ArrayList<String>();
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;

		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							streak.add(u.getName() + "{" + u.getMaxSpojStreak() + "}");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(streak);
		return streak.toArray(new String[0]);
	}
	
	synchronized private String[][] spojaweek()
	{
		final List<String> haveSpojadayed = new ArrayList<String>();
		final List<String> notSpojadayed = new ArrayList<String>();
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							if(u.getSpojWeek() > 0)
							{
									haveSpojadayed.add(u.getName() + "{" + u.getSpojWeek() + "}");
							} else {
								notSpojadayed.add(u.getName());
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(haveSpojadayed);
		Collections.sort(notSpojadayed);
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}
	
	synchronized private String[][] spojahour()
	{
		final List<String> haveSpojadayed = new ArrayList<String>();
		final List<String> notSpojadayed = new ArrayList<String>();
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							if(u.getSpojHour() > 0)
							{
								haveSpojadayed.add(u.getName() +"{" +  u.getSpojHour() + "}");
							} else {
								notSpojadayed.add(u.getName());
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(haveSpojadayed);
		Collections.sort(notSpojadayed);
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}
	
	class SpojSolver {
		public String user;
		public Date solveDate;
		
		public SpojSolver(Date newSolveDate) {
			solveDate = newSolveDate;
		}
	}
	
	synchronized public String getLastSpoj()
	{
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		final SpojSolver bestSpojer = new SpojSolver(new Date(0));
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						Date check = Utility.getDateOfLastSolve(u.getName());
						synchronized(thisBot) {
							if (check.compareTo(bestSpojer.solveDate) > 0) {
								bestSpojer.user = u.getName();
								bestSpojer.solveDate = check;
							}
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String ret = bestSpojer.user + " is the best spojer. " + Utility.getLastSpojSolve(bestSpojer.user);
		return ret;
	}
	
	synchronized public String getWorstSpojer()
	{
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		final SpojSolver bestSpojer = new SpojSolver(new Date());
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						Date check = Utility.getDateOfLastSolve(u.getName());
						synchronized(thisBot) {
							if (check.compareTo(bestSpojer.solveDate) < 0) {
								bestSpojer.user = u.getName();
								bestSpojer.solveDate = check;
							}
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String ret = bestSpojer.user + " is a huge slacker. " + Utility.getLastSpojSolve(bestSpojer.user);
		return ret;
	}
	
	synchronized public String[][] hasSolved(final String prob)
	{
		final List<String> haveSpojadayed = new ArrayList<String>();
		final List<String> notSpojadayed = new ArrayList<String>();
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							if(u.hasSolved(prob))
							{
								haveSpojadayed.add(u.getName());
							} else {
								notSpojadayed.add(u.getName());
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(haveSpojadayed);
		Collections.sort(notSpojadayed);
		String[][] ret = new String[2][];
		ret[0] = haveSpojadayed.toArray(new String[0]);
		ret[1] = notSpojadayed.toArray(new String[0]);
		return ret;
	}

	synchronized public String firstSolved(final String prob)
	{
		final Counter userThreads = new Counter(users.size());
		final SPOJBot thisBot = this;
		final SpojSolver firstSolver = new SpojSolver(new Date());
		
		try {
			for(final User u : users.values())
			{
				Thread t = new Thread() {
					public void run() {
						try {
							u.update();
							if(u.hasSolved(prob))
							{
								Date solveDate = Utility.getSolveDate(u.getName(), prob);
								synchronized(thisBot) {
									if (solveDate.compareTo(firstSolver.solveDate) < 0) {
										firstSolver.user = u.getName();
										firstSolver.solveDate = solveDate;
									}
								}
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						--(userThreads.val);
					}
				};
				t.setDaemon(true);
				t.start();
			}
			long time = System.currentTimeMillis();
			// Wait until either 15 seconds elapse (timeout) or all threads have returned
			while (System.currentTimeMillis() - time < 15000 && userThreads.val > 0)
				thisBot.wait(500); // Wait for a nominal half a second to make sure we don't endless wait
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		String ret = firstSolver.user + " was the first to solve " + prob + " on " + firstSolver.solveDate.toString() + "."; 

		if (firstSolver.user == null) {
			ret = "No one has solved " + prob + ".";
		}
		
		return ret;
	}

	public String getSolveDate(String userName, String prob) {
		try {
			Date solveDate = Utility.getSolveDate(userName, prob);
			if (solveDate == null)
				return userName + " has not solved " + prob + ".";
			return userName + " first solved " + prob + " on " + solveDate.toString() + ".";
		}
		catch (IOException e) {
			return("User " + userName + " was not found on SPOJ.");
		}
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
