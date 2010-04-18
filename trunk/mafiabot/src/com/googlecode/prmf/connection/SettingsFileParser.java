package com.googlecode.prmf.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.text.ParseException;

public class SettingsFileParser {
	private Scanner in;
	private String channel;
	private String botName;
	private String server;
	private int port;

	public SettingsFileParser(String filename) throws FileNotFoundException {
		in = new Scanner(new File(filename));
	}
	
	public SettingsFileParser() throws FileNotFoundException {
		this("./settings.txt");
	}

	public void parseFile() throws ParseException {
		int numSettings = 0;
		while(in.hasNextLine()) {

			
			
			String line = in.nextLine();
			// TODO do the following using a regular expression!
			int start = 0;
			for(start = 0; start < line.length(); ++start) {
				if(line.charAt(start) != ' ')
					break;
			}
			if(line.charAt(start) == '#')
				continue;
			
			
			
			
			
			String setting[] = line.split("=");
			for(int i = 0; i < setting.length; ++i)
				setting[i] = setting[i].trim();

			if(setting[0].equalsIgnoreCase("server")) {
				server = setting[1];
			}
			else if(setting[0].equalsIgnoreCase("nick")) {
				botName = setting[1];
			}
			else if(setting[0].equalsIgnoreCase("port")) {
				port = Integer.parseInt(setting[1]);
			}
			else if(setting[0].equalsIgnoreCase("channel")) {
				channel = setting[1];
			}
			else {
				throw new ParseException("Settings file corrupt", numSettings);
			}
			++numSettings;
		}
		if(numSettings != 4) {
			throw new ParseException("Settings file corrupt", numSettings);
		}
		System.err.println(botName + " " + channel + " " + port + " " + server);
	}

	public String getBotName() {
		return botName;
	}

	public String getChannel() {
		return channel;
	}

	public int getPort() {
		return port;
	}

	public String getServer() {
		return server;
	}
}
