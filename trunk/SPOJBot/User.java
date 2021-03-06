import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User implements Comparable<User>{
	
	final private File file;
	private Date lastUp = new Date(0);
	private String name = "";
	private int spojHour = 0;
	private int spojDay = 0;
	private int spojWeek = 0;
	private int curSpojStreak = 0;
	private int maxSpojStreak = 0;
	private boolean hasChanged = true;
	private Set<String> prevSolves = new HashSet<String>();
	
	public User(String name)
	{
		this.name = name.toLowerCase();
		file = new File("users/"+this.name+".txt");
		initialize();
	}
	
	public String getName(){ return name;}
	public int getSpojDay() {return spojDay;}
	public int getSpojWeek() { return spojWeek;}
	public int getSpojHour() { return spojHour;}
	public int getCurSpojStreak() { return curSpojStreak;}
	public int getMaxSpojStreak() { return maxSpojStreak;}
	
	private void initialize()
	{
		if(file.exists())
		{
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(file));
				DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
				lastUp = df.parse(in.readLine());
				Date now = new Date();
				spojWeek = Integer.parseInt(in.readLine());
				spojDay = Integer.parseInt(in.readLine());
				spojHour = Integer.parseInt(in.readLine());
				curSpojStreak = Integer.parseInt(in.readLine());
				maxSpojStreak = Integer.parseInt(in.readLine());
				
				String probCode = in.readLine();
				while(probCode != null)
				{
					prevSolves.add(probCode);
					probCode = in.readLine();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			if(lastUp.compareTo(Utility.getDateOfLastSolve(name)) < 0)
			{
				try { update(); } catch (IOException e) { e.printStackTrace(); }
			}
		}
		else
		{
			try { update(); } catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	public void update() throws IOException
	{
		Date now = new Date();
		if(!Utility.checkIfSameWeek(now,lastUp)) {
			spojWeek = 0;
			hasChanged = true;
		}
		if(!Utility.checkIfSameDay(now,lastUp))
		{
			if(spojDay == 0)curSpojStreak = 0;
			spojDay = 0;
			hasChanged = true;
		}
		if(!Utility.checkIfSameHour(now,lastUp)) {
			spojHour = 0;
			hasChanged = true;
		}
		try {
			BufferedReader in = Utility.getUserSolveStream(name);
			String line = "";
			for(int i=0; i<10; ++i)
			{
				line = in.readLine();
			}
			String[] info = line.split(" *\\| *");
			while(info.length >= 5)
			{
				if(info[4].equalsIgnoreCase("AC"))
				{
					if(!prevSolves.contains(info[3]) && lastUp.compareTo(Utility.toDate(info[1]) )>0)
					{
						hasChanged = true;
						prevSolves.add(info[3]);
						Date ret = Utility.toDate(info[2]);
						if(Utility.checkIfSameWeek(now,ret))
						{
							spojWeek++;
							if(Utility.checkIfSameDay(now,ret))
							{
								spojDay++;
								if(Utility.checkIfSameHour(now,ret))
									spojHour++;
								if(spojDay==1){
									curSpojStreak++;
									maxSpojStreak = Math.max(curSpojStreak,maxSpojStreak);
								}
							}
						}
					}
				}
				line = in.readLine();
				info = line.split(" *\\| *");
			}
			in.close();
			lastUp = now;
			writeFile();
			
		} catch(IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	public void writeFile()
	{
		if(hasChanged)
		{
			try
			{
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write(lastUp.toString() + "\n");
				out.write(spojWeek + "\n");
				out.write(spojDay + "\n");
				out.write(spojHour + "\n");
				out.write(curSpojStreak + "\n");
				out.write(maxSpojStreak + "\n");
				String[] problems = prevSolves.toArray(new String[1]);
				for(String p : problems)
					out.write(p + "\n");
				out.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		hasChanged=false;
	}
	
	@Override
	public int compareTo(User arg0) {
		return this.name.compareTo(arg0.name);
	}
	
	public boolean hasSolved(String prob)
	{
		return prevSolves.contains(prob);
	}
	
	public String toString()
	{
		return this.name;
	}
}
