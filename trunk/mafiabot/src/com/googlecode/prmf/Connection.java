package com.googlecode.prmf;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection {
	// TODO it's better practice to actually catch exceptions, and print a useful message
	public static void main(String[] arg) throws UnknownHostException, IOException {
		// TODO the host and port should not be hardcoded 
		Socket s = new Socket("irc.freenode.net", 6667);
		Scanner in = new Scanner(s.getInputStream());
		while(in.hasNextLine()) {
			String line = in.nextLine();
			System.out.println(line);
		}
	}
}
