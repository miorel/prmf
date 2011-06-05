import java.util.*;

public class A1 {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		HashMap<String,String> playThis = new HashMap<String,String>();
		playThis.put("rock","paper");
		playThis.put("rock2","Spock");
		playThis.put("scissors","Spock");
		playThis.put("scissors2","rock");
		playThis.put("paper","scissors");
		playThis.put("paper2","lizard");
		playThis.put("lizard","rock");
		playThis.put("lizard2","scissors");
		playThis.put("Spock","lizard");
		playThis.put("Spock2","paper");
		int cases = in.nextInt();
		String prev = "";
		for(int i=0;i<cases;++i) {
			String you = in.next();
			String cur = playThis.get(you);
			if (cur.equals(prev)) {
				cur = playThis.get(you + "2");
			}
			System.out.println(cur);
			prev = cur;
		}
	}
}