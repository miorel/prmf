import java.math.*;

class H_easy {
	public static void main(String[] arg) {
		String ans = "QO";
		String goal = "28180536462673263023";
		for(int i = 3, req = goal.length(); req != 0; ++i) {
			if(BigInteger.valueOf(i).isProbablePrime(10) || Integer.toString(i, 2).matches("10*")) ans = "L" + ans;
			else {
				ans = goal.charAt(goal.length() - 1) + ans;
				goal = goal.replaceAll(".$", "");
				--req;
			}
		}
		System.out.println(ans);
	}
}
