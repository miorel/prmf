package com.googlecode.prmf.connection;

public interface Listener {
	public void receiveLine(String line, IOThread inputThread);
	public void timerMessage(); // not sure if having this method is good
}
