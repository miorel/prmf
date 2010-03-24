package com.googlecode.prfm;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Connection {
	public static void main(String[] arg) throws UnknownHostException, IOException {
		Socket s = new Socket("irc.freenode.net", 6667);
		Scanner in = new Scanner(s.getInputStream());
		while(in.hasNextLine()) {
			String line = in.nextLine();
			System.out.println(line);
		}
	}
}
