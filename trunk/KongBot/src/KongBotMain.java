import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class KongBotMain {

	private static final String HOST_NAME = "chat.freenode.net";
	private static final int PORT_NUMBER = 8001;
	private static final String NICK = "KongBot";
	private static final String FULL_NAME = "Programmed by TJ Boyd, Uttam Thakore, & Jason Fisher";
	private static final String CHANNEL = "#ufpt";
	//private static final String PASSWORD = "";
	
    public static void main(String[] args) {
    	
    	Socket clientSocket;
    	//Scanner stdIn;
    	Scanner input;
    	PrintStream output;
    	KongBot outThread;
    	try {
       		clientSocket = new Socket(HOST_NAME, PORT_NUMBER);
       		//stdIn = new Scanner(System.in);
       		input = new Scanner(clientSocket.getInputStream());
       		output = new PrintStream(clientSocket.getOutputStream());
       		outThread = new KongBot(input, output);
       		
       		outThread.start();
			
			output.println("NICK " + NICK);
			output.println("USER " + NICK + " 0 * :" + FULL_NAME);
			output.println("JOIN " + CHANNEL);
			//output.println("JOIN " + "#ufpt");
			//output.println("PRIVMSG nickserv :identify " + PASSWORD);
			
			//String sendMsg = "";
        	//while(!(sendMsg = stdIn.nextLine()).equals("CLOSE"))
        	//{
        	//	if(sendMsg.length() > 0 && sendMsg.charAt(0) == ':')
        	//		output.println("PRIVMSG " + CHANNEL + " " + sendMsg);
        	//	else
        	//		output.println(sendMsg);
        	//}
			//output.println(sendMsg);
			
			//stdIn.close();	
			
        }
        catch (IOException e) {
        	System.out.println(e);
        }
    	
    }
}