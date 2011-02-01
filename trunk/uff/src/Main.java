
import util.diff_match_patch;
import util.diff_match_patch.*;
import java.util.*;
import java.io.File;

public class Main {
	public static void main(String[] args)
	{
		File input = new File("/cise/homes/f93022dt/folder/k.in");
		File source = new File("/cise/homes/f93022dt/folder/k.java");
		File judgeOutput = new File("/cise/homes/f93022dt/Desktop/problemK.out");
		Tester tester = new Tester(input);
		boolean result = tester.test(source, new LanguageJava());
		
		/*
		String a = "HELLO WORLD";
		String b = "HELLO ORLD";
		String c = "HELLO  WORLD";
		diff_match_patch diff = new diff_match_patch();
		LinkedList<Diff> result = diff.diff_main(a, c);
		for(Diff d : result)
		{
			System.out.println(d);
		}
		*/
	}
}
