package com.googlecode.prmf.starter;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection 
	{
	// TODO it's better practice to actually catch exceptions, and print a useful message
	public Connection(String socket, int port) throws UnknownHostException, IOException 
	{
		// TODO the host and port should not be hardcoded 
		Socket s = new Socket(socket, port);
		Scanner in = new Scanner(s.getInputStream());
		while(in.hasNextLine()) {
			String line = in.nextLine();
			System.out.println(line);
		}
	}
}
