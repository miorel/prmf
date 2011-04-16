package MafiaPlayer;
import java.util.*;

public class MafiaPlayerMain {
    
    public static void main(String[] args) throws Exception {
        
        Scanner in = new Scanner(System.in);
        
        int num = in.nextInt();
        MafiaPlayer[] bots = new MafiaPlayer[num];
        for(int i = 0; i < num; i++)
        {
        	bots[i] = new MafiaPlayer("UFPTplayer" + i);
        	bots[i].setVerbose(true);
        	bots[i].connect("irc.freenode.net");
        	bots[i].joinChannel("#TWW2");
        	bots[i].joinChannel("#UFPT");
        	
        }
        /**
        MafiaPlayer bot1 = new MafiaPlayer("UFPTplayer1");
        MafiaPlayer bot2 = new MafiaPlayer("UFPTplayer2");
        MafiaPlayer bot3 = new MafiaPlayer("UFPTplayer3");
        MafiaPlayer bot4 = new MafiaPlayer("UFPTplayer4");
        MafiaPlayer bot5 = new MafiaPlayer("UFPTplayer5");
        
        // Enable debugging output.
        bot1.setVerbose(true);
        bot2.setVerbose(true);
        bot3.setVerbose(true);
        bot4.setVerbose(true);
        bot5.setVerbose(true);
        
        // Connect to the IRC server.
        bot1.connect("irc.freenode.net");
        bot2.connect("irc.freenode.net");
        bot3.connect("irc.freenode.net");
        bot4.connect("irc.freenode.net");
        bot5.connect("irc.freenode.net");

        // Join my channels.
        bot1.joinChannel("#TWW2");
        bot2.joinChannel("#TWW2");
        bot3.joinChannel("#TWW2");
        bot4.joinChannel("#TWW2");
        bot5.joinChannel("#TWW2");
        bot1.joinChannel("#UFPT");
        bot2.joinChannel("#UFPT");
        bot3.joinChannel("#UFPT");
        bot4.joinChannel("#UFPT");
        bot5.joinChannel("#UFPT");
        */
    }
    
}
