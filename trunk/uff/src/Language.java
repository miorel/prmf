import java.io.File;
import java.io.IOException;

public abstract class Language
{
	protected String compile_command;
	protected String run_command;
	abstract public void compile(File file)  throws IOException;
	abstract public File run(File file, File input)  throws IOException;
	
}
