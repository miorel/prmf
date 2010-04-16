package com.googlecode.prmf.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IOThread extends Thread {
	private Socket soc;
	private InputStream inputStream; 
	private List<Listener> list;
	private PrintStream printStream; 
	private String channel;
	private String botName;
	private SettingsFileParser settings;
	
	public IOThread(SettingsFileParser settings) throws Exception
	{
		this.settings = settings;
		this.setName("I/O");
		makeSocket();
		inputStream = soc.getInputStream();
		printStream = new PrintStream(soc.getOutputStream());
		list = new ArrayList<Listener>();
		this.channel = settings.getChannel();
		this.botName = settings.getBotName();
		
		printStream.println("NICK " + this.botName); 
		printStream.println("USER " + this.botName + " 12 * " + this.botName);
		printStream.println("JOIN " + channel);
	}
	
	public IOThread()
	{
		
	}
	
	@Override
	public void run()
	{
		Scanner in = new Scanner(inputStream);
		while(in.hasNextLine())
		{
			String line = in.nextLine();
			System.out.println("<<<< " + line);
			for(Listener l : list)
				l.receiveLine(line, this);
		}
	}
	
	public void ceaseTimer()
	{
		for(Listener l : list)
			l.timerMessage();
	}
	
	public void addListener(Listener listener) {
		list.add(listener);
	}
	
	public void sendMessage(String destination, String message) 
	{
		printStream.println("PRIVMSG "+destination+" :"+message);
		System.out.println(">>>> " + "PRIVMSG "+destination+" :"+message);
	}
	
	public void sendMessage(String command, String destination, String message)
	{
		printStream.println(command+" "+destination+" "+message);
		System.out.println(">>>> " + command+" "+destination+" "+message);
	}
	
	public void sendPONG(String[] msg)
	{
		msg[0] = "PONG";
		
		StringBuilder concat = new StringBuilder();
		for(int i=0;i<msg.length;++i)
		{
			concat.append(msg[i]);
			if(i<msg.length-1)
				concat.append(" ");
		}
		printStream.println(concat);
		System.out.println(">>>> " + concat);
	}
	
	public String getChannel()
	{
		return channel;
	}
	
	public SettingsFileParser getSettings()
	{
		return settings;
	}
	
	public void makeSocket() throws IOException
	{
		soc = new Socket(getSettings().getServer(), getSettings().getPort());
	}
}
