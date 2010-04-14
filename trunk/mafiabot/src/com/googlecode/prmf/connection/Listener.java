package com.googlecode.prmf.connection;

public interface Listener {
	public void receiveLine(String line, IOThread inputThread);
}
