package com.googlecode.prmf.starter;

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

	public IOThread(String server, int port, String channel, String botName) {
		try {
			this.setName("I/O");
			soc = new Socket(server, port);
			inputStream = soc.getInputStream();
			printStream = new PrintStream(soc.getOutputStream());
		}
		catch (Exception e) {
			System.out.println("oops");
		} 
		list = new ArrayList<Listener>();
		this.channel = channel;
		this.botName = botName;
		
		//TODO the following should not be done done in the constructor but when the thread is started
		printStream.println("NICK " + botName); //TODO don't hardcode the nick
		printStream.println("USER " + botName + " 12 * " + botName); //TODO don't hardcode the username and real name
		printStream.println("JOIN " + channel);
	}
	
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
			if(l instanceof MafiaListener)
			{
				// TODO if you find yourself needing a cast chances are you're doing something wrong
				((MafiaListener)l).timerMessage();
				break;
			}
	}
	
	public void addListener(Listener listener) {
		list.add(listener);
	}
	
	public void sendMessage(String destination, String message) 
	{
		printStream.println("PRIVMSG "+destination+" :"+message);
		System.out.println(">>>> " + "PRIVMSG "+destination+" :"+message);
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
}
