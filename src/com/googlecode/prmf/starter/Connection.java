package com.googlecode.prmf.starter;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection 
	{
	Socket soc;
	PrintStream os;
	public Connection(String socket, int port) throws UnknownHostException, IOException 
	{
		soc = new Socket(socket, port);
		os = new PrintStream(soc.getOutputStream());
	}
	
	
}
