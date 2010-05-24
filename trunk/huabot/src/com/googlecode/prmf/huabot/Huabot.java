package com.googlecode.prmf.huabot;

import java.io.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import static com.googlecode.prmf.merapi.net.irc.IrcCommands.*;

import com.googlecode.prmf.merapi.net.irc.Entity;
import com.googlecode.prmf.merapi.net.irc.IrcClient;
import com.googlecode.prmf.merapi.net.irc.cmd.JoinCommand;
import com.googlecode.prmf.merapi.net.irc.cmd.PrivmsgCommand;
import com.googlecode.prmf.merapi.net.irc.event.AbstractIrcEventListener;
import com.googlecode.prmf.merapi.net.irc.event.IrcEvent;
import com.googlecode.prmf.merapi.net.www.Tinysong;
import com.googlecode.prmf.merapi.net.www.TinysongResult;
import com.googlecode.prmf.merapi.util.Pair;
import com.googlecode.prmf.merapi.util.Strings;

public class Huabot extends AbstractIrcEventListener {
	private static final String COMMAND_TRIGGER = "~"; // what a message must start with to be considered a command
	private static final Pattern COMMAND_PATTERN = Pattern.compile("\\s*" + Pattern.quote(COMMAND_TRIGGER) + "\\s*(\\w+)\\s*(.*)", Pattern.DOTALL);
	
	private static final String KARMA_FILE = "karma.txt";
	private static final List<Pair<Pattern,Integer>> KARMA_PATTERNS = new ArrayList<Pair<Pattern,Integer>>();
	static {
		String entityRegex = "(\\w+)";
		String[] modifiers = {"+", "-"};
		int[] dks = {1, -1};
		
		for(int i = 0; i < modifiers.length; ++i) {
			Integer dk = Integer.valueOf(dks[i]);
			String modifier = "(?:" + Pattern.quote(modifiers[i]) + "){2,}";
			
			KARMA_PATTERNS.add(new Pair<Pattern,Integer>(Pattern.compile("(?<!\\S)" + modifier + entityRegex), dk)); // pre-notation
			KARMA_PATTERNS.add(new Pair<Pattern,Integer>(Pattern.compile(entityRegex + modifier + "(?!\\S)"), dk)); // post-notation
		}
	}
	
	private final Map<String,Integer> karma;
	
	/**
	 * Default constructor.
	 */
	public Huabot() {
		this.karma = readMapFromFile(KARMA_FILE);
	}

	@Override
	public void joinEvent(IrcEvent<JoinCommand> event) {
		IrcClient client = event.getClient();
		JoinCommand cmd = event.getCommand();

		String nick = client.getDesiredNick(); // We're assuming that the desired nick and actual nick are the same... Not good.
		String channel = cmd.getChannel().toLowerCase(Locale.ENGLISH);
		String user = event.getOrigin().getNick();

		if(!nick.equalsIgnoreCase(user)) // Otherwise we'd welcome ourselves!
			privmsg(client, channel, String.format("Welcome to %s, %s!", channel, user));
	}

	@Override
	public void privmsgEvent(IrcEvent<PrivmsgCommand> event) {
		IrcClient client = event.getClient();
		PrivmsgCommand cmd = event.getCommand();

		String nick = client.getDesiredNick(); // Again, unsafely assuming that the desired nick and actual nick are the same.

		// The "target" of the message is either a channel, or our nickname, in the case of a direct message.
		String target = cmd.getTarget();
		if(nick.equalsIgnoreCase(target)) {
			// direct message
		}
		else {
			// message in a channel
			reactToMessage(client, target, event.getOrigin(), cmd.getMessage());
		}
	}

	private int getKarma(String entity) {
		Integer k = this.karma.get(entity.toLowerCase(Locale.ENGLISH));
		return k == null ? 0 : k.intValue();
	}

	private void setKarma(String entity, int karma) {
		this.karma.put(entity.toLowerCase(Locale.ENGLISH), Integer.valueOf(karma));
		writeMapToFile(this.karma, KARMA_FILE);
	}

	private void changeKarma(String entity, int changeInKarma) {
		setKarma(entity, getKarma(entity) + changeInKarma);
	}
	
	private void reactToMessage(IrcClient client, String channel, Entity sender, String message) {
		Matcher cmdMatcher = COMMAND_PATTERN.matcher(message);

		if(cmdMatcher.matches()) {
			// We got ourselves a command!
			String cmd = cmdMatcher.group(1).toLowerCase(Locale.ENGLISH);
			String[] arg = cmdMatcher.group(2).trim().split("\\s+");

			if(cmd.equals("version")) {
				privmsg(client, channel, "This is huabot, REWRITE edition.");
			}

			if(cmd.equals("date")) {
				privmsg(client, channel, String.format("Today's date is %s.",
					DateFormat.getDateInstance(DateFormat.LONG).format(new Date())));
			}

			if(cmd.equals("time")) {
				privmsg(client, channel, String.format("The current time is %s.",
					DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date())));
			}

			if(cmd.equals("tinysong")) {
				Tinysong ts = new Tinysong();

				String response = null;
				if(arg.length == 0 || arg[0].length() == 0)
					response = "Some search terms would be nice.";
				else {
					String query = Strings.join(" ", arg);
					try {
						TinysongResult result = ts.topResult(query);
						if(result != null)
							response = String.format("Top result for \"%s\": %s by %s <%s>", query, result.getSongName(), result.getArtistName(), result.getUrl());
						else
							response = String.format("There were no search results for \"%s\" :/", query);
					}
					catch(Exception e) {
						response = "There was an error. " + e.getMessage();
					}
				}

				privmsg(client, channel, response);
			}
			
			if(cmd.equals("karma")) {
				if(arg.length == 0 || arg[0].length() == 0) {
					String top = topAndBotKarma(true);
					String bot = topAndBotKarma(false);
					if(top == null){
						privmsg(client, channel, "There is no registered karma!");
					} else {
						privmsg(client, channel, String.format("The highest registered karma is %s with %d and the lowest registered karma is %s with %d.",top,Integer.valueOf(getKarma(top)),bot,Integer.valueOf(getKarma(bot))));
					}
				}
				else {
					String entity = arg[0];
					privmsg(client, channel, String.format("%s has %d karma.", entity, Integer.valueOf(getKarma(entity))));
				}
			}
			
			
		}
		else {
			// This was not a command. Do other kinds of text processing.
			
			// Process karma!
			for(Pair<Pattern,Integer> pair: KARMA_PATTERNS) {
				int dk = pair.getSecond().intValue();
				Matcher karmaMatcher = pair.getFirst().matcher(message);
				while(karmaMatcher.find()) {
					String karmaTarget = karmaMatcher.group(1);
					changeKarma(karmaTarget, dk);
				}
			}
		}
	}

	private void writeMapToFile(Map<String,Integer> theMap, String fileName) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(fileName));
			Set<String> names = theMap.keySet();
			Iterator<String> nameIterator = names.iterator();
			
			while(nameIterator.hasNext()) {
				String name = nameIterator.next();
				Integer val = theMap.get(name);
				out.println(name + " " + val);
			}
			
		}
		catch(FileNotFoundException e) {
			System.out.println("Could not open " + fileName + ": data was not saved.");
		}
	}
	
	private Map<String,Integer> readMapFromFile(String fileName) {
		Map<String,Integer> ret = new HashMap<String,Integer>();

		try {
			Scanner fIn = new Scanner(new FileInputStream(fileName));
			while(fIn.hasNextLine()) {
				String[] line = fIn.nextLine().split(" ");
				ret.put(line[0].toLowerCase(Locale.ENGLISH), Integer.valueOf(Integer.parseInt(line[1])));
			}
		}
		catch(FileNotFoundException e) {
		}
		
		return ret;
	}
	
	private String topAndBotKarma(boolean top)
	{
		Set<String> names = this.karma.keySet();
		if(names.isEmpty()){
			return(null);
		} 
		
		Iterator<String> nameIterator = names.iterator();
		String name = nameIterator.next();
		int val = getKarma(name);
		while(nameIterator.hasNext()) {
			String test = nameIterator.next();
			if(top && getKarma(test) > val) {
				name = test;
				val = getKarma(test);
			} else if(getKarma(test) < val) {
				name = test;
				val = getKarma(test);
			}
		}
		return(name);
	}
}
