import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.tmatesoft.svn.cli.svnversion.SVNVersion;

public class Version {
	public static void main(String[] arg) {	
		// save the old System.out
		final PrintStream oldOut = System.out;
		
		// change System.out to a stream that will be captured
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		System.setOut(new PrintStream(os));
		
		// add shutdown hook to do the processing, since the SVNVersion command
		// will be doing a system wide exit when it completes
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// restore System.out
				System.setOut(oldOut);
		
				// retrieve the output
				String rev = null;
				try {
					rev = os.toString("US-ASCII");
				}
				catch(UnsupportedEncodingException e) {
					e.printStackTrace();
					System.exit(1);
				}
		
				rev = rev.replaceAll("^[^:]+:", "");
				System.out.println("huabot.version=0.2.1-r" + rev);
			}
		}));
		
		// execute the command
		SVNVersion.main(new String[] {"-c", "-n"});
	}
}
