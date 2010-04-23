package com.googlecode.prmf.connection;

public class Main {
	public static void main(String[] arg) {
		SettingsFileParser settings;
		try {
			settings = new SettingsFileParser("settings.txt");
			settings.parseFile();
			IOThread inputOutputThread = new IOThread(settings);
			Listener mafiaListener = new MafiaListener(settings.getBotName());
			PingListener pingListener = new PingListener();
			DisconnectListener disconnectListener = new DisconnectListener();
			inputOutputThread.addListener(mafiaListener);
			inputOutputThread.addListener(pingListener);
			inputOutputThread.addListener(disconnectListener);
			inputOutputThread.start();
		}
		catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
