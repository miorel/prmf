import java.io.File;
import java.io.IOException;
import static java.lang.Runtime.getRuntime;

public class LanguageJava extends Language
{
	
	public LanguageJava()
	{
		compile_command = "javac";
		run_command = "java -cp ";
	}
	
	public void compile(File file) throws IOException
	{
		System.out.println(compile_command + " " + file.getAbsolutePath());
		
		getRuntime().exec(compile_command + " " + file.getAbsolutePath());
	}
	
	public File run(File file, File input)  throws IOException
	{
		String filePath = file.getAbsolutePath();
		String classPath = filePath.substring(0, filePath.lastIndexOf("."));
		classPath = classPath.substring(0, filePath.lastIndexOf("/")+1) + " " + classPath.substring(filePath.lastIndexOf("/")+1);
		String path = filePath.substring(0, filePath.lastIndexOf("/")+1);
		File output = new File(path+"k.out");
		System.out.println(run_command+ " "+classPath + " < " + input.getAbsolutePath() + " > " + output.getAbsolutePath());
		getRuntime().exec(run_command+ " "+classPath + " < " + input.getAbsolutePath() + " > " + output.getAbsolutePath());
		return output;
	}
}
