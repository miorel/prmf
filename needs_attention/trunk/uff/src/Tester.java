import java.io.*;
import java.lang.Runtime;

public class Tester
{
	private File input;
	public Tester(File input)
	{
		this.input = input;
	}
	
	public boolean test(File source, Language language)
	{
		try
		{
			language.compile(source);
			File output = language.run(source, input);
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
		}
		return true;
	}
}
