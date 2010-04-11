package com.googlecode.prmf.starter;

public class Main {
	public static void main(String[] arg) {
		SettingsFileParser settings;
		try
		{
			settings = new SettingsFileParser("settings.txt");
			settings.parseFile();
			IOThread inputOutputThread = new IOThread(settings);
			Listener mafiaListener = new MafiaListener(settings.getBotName());
			PingListener pingListener = new PingListener();
			inputOutputThread.addListener(mafiaListener);
			inputOutputThread.addListener(pingListener);
			inputOutputThread.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
}
