package MyBot;

public class MyBotMain {
	public static void main(String[] arg) throws Exception {
		// Now start our bot up.
		MyBot bot = new MyBot();

		// Enable debugging output.
		bot.setVerbose(true);

		// Connect to the IRC server.
		bot.connect("irc.freenode.net");
	}
}
