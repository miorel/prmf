package MafiaPlayer;

import java.util.Locale;

import PircAPI.PircBot;

public class MafiaPlayer extends PircBot {
	private String gameStarter;
	private String mafiaBot;

	public MafiaPlayer(String myName) {
		this.setName(myName);
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if(channel.equalsIgnoreCase("#ufpt")) {
			if(containsIgnoreCase(message, "Mafia game started by ")) {
				gameStarter = message.substring(22, message.length() - 1);
				mafiaBot = sender;
				sendMessage("#ufpt", "~join");
			}
			else if(containsIgnoreCase(message, "The game is now over")) {
				gameStarter = "";
			}
			else if(containsIgnoreCase(message, "~nolynch") && sender.equalsIgnoreCase(gameStarter)) {
				sendMessage("#ufpt", "~nolynch");
			}
			else if(containsIgnoreCase(message, "~lynch") && sender.equalsIgnoreCase(gameStarter)) {
				sendMessage("#ufpt", message);
			}
		}
		else if(channel.equalsIgnoreCase("#TWW2") && message.equalsIgnoreCase("disconnect")) {
			quitServer();
		}
	}

	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		sendMessage("#TWW2", message);
		if(containsIgnoreCase(message, "night:")) {
			sendMessage(mafiaBot, message.substring(6));
		}
		else if(containsIgnoreCase(message, "day:")) {
			sendMessage("#ufpt", message.substring(4));
		}
	}

	public void onDisconnect() {
		dispose();
	}

	private boolean containsIgnoreCase(String mes, String sub) {
		return mes.toLowerCase(Locale.ENGLISH).contains(sub.toLowerCase(Locale.ENGLISH));
	}
}
