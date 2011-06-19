import java.util.*;

public class C1 {
	static int[] values;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int cases=in.nextInt();
		
		for(int c=0;c<cases;++c) {
			int numbers = in.nextInt();
			int strats = in.nextInt();
			int[][] orders = new int[strats][numbers];
			values = new int[numbers];
			for(int i=0;i<strats;++i) {
				for(int k=0;k<numbers;++k) {
					orders[i][k] = in.nextInt();
					
				}
			}
			int[] answer = new int[numbers];
			int[] finalAns = new int[numbers];
			for(int i=0;i<strats;++i) {
				solve(orders[i],0,numbers-1,0);
				for(int k=0;k<numbers;++k) {
					answer[k] += values[k];
				}
				System.err.println(Arrays.toString(values));
			}
			
			int maxCandies = 0;
			int finalAnswer = 0;
			for(int i=0;i<numbers;++i) {
				if (answer[i] > maxCandies) {
					maxCandies = answer[i];
					finalAnswer = i;
				}
			}
			finalAns[finalAnswer] = strats;
			int gcd = gcd(maxCandies,strats);
			maxCandies /= gcd;
			strats /= gcd;
			System.out.println(maxCandies + "/" + strats);
			for(int i=0;i<numbers;++i) {
				System.out.print(finalAns[i] + " ");
			}
			
			
			System.out.println();
		}
	}
	
	public static void solve(int[] numbers, int start, int end, int moves) {
		if (end < start || start >= numbers.length) return;
		values[numbers[start]] = (moves+1);
		//System.out.println(moves+1);
		if (start == end) return;
		int endOfSmaller = start+1;
		if (numbers[start+1] < numbers[start]) {
			for(int i=start+2;i<=end;++i) {
				if (numbers[i] > numbers[start]) {
					System.err.println("calling solve from " + numbers[start+1] + " to " + numbers[i-1] + " with " + (moves+1) + " moves.");
					solve(numbers,start+1,i-1,moves+1);
					endOfSmaller=i;
					break;
				}
			}
		}
		System.err.println("calling solve from " + numbers[endOfSmaller] + " to " + numbers[end] + " with " + (moves+1) + " moves.");
		solve(numbers,endOfSmaller,end,moves+1);
	}
	
	public static int gcd(int a, int b) {
		if (b > a) return gcd(b,a);
		return (b == 0 ? a : gcd(b,a%b));
	}
}