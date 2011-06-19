import java.util.*;

public class G1 {
	public static void main(String[] args) {
		int MAX = 4096;
		int[][] board = new int[MAX][MAX];
		Scanner in = new Scanner(System.in);
		int reps = in.nextInt();
		int commandCount = in.nextInt();
		int c = 0;
		List<String> commands = new ArrayList<String>();
		List<Addition> additions = new ArrayList<Addition>();
		in.nextLine();
		for(int i=0;i<commandCount;++i) {
			commands.add(in.nextLine());
		}
		for(int i=0;i<reps;++i) {
			for(int k=0;k<commandCount;++k) {
				String[] stuff = commands.get(k).split(" ");
				if (stuff.length == 4) {
					int xp = (c ^ Integer.parseInt(stuff[1])) % MAX;
					int yp = (c ^ Integer.parseInt(stuff[2])) % MAX;
					int a = Integer.parseInt(stuff[3]);
					board[yp][xp] += a;
					additions.add(new Addition(xp,yp,a));
					c = board[yp][xp];
				}
				else {
					int xone = (c ^ Integer.parseInt(stuff[1])) % MAX;
					int xtwo = (c ^ Integer.parseInt(stuff[2])) % MAX;
					if (xone > xtwo) {
						xone ^= xtwo;
						xtwo ^= xone;
						xone ^= xtwo;
					}
					int yone = (c ^ Integer.parseInt(stuff[3])) % MAX;
					int ytwo = (c ^ Integer.parseInt(stuff[4])) % MAX;
					if (yone > ytwo) {
						yone ^= ytwo;
						ytwo ^= yone;
						yone ^= ytwo;
					}
					int t = Integer.parseInt(stuff[5]);
					if (t == 0) {
						t = additions.size();
					}
					int answer = 0;
					if (t > 0) {
						for(int m=0;m<additions.size()&&m<t;++m) {
							Addition cur = additions.get(m);
							if (cur.x < xone || cur.x > xtwo || cur.y > ytwo || cur.y < yone) {
								continue;
							}
							answer += cur.a;
						}
					}
					if (t < 0) {
						for(int m=0;m<additions.size()+t;++m) {
							Addition cur = additions.get(m);
							if (cur.x < xone || cur.x > xtwo || cur.y > ytwo || cur.y < yone) {
								continue;
							}
							answer += cur.a;
						}
					}
					System.out.println(answer);
					c = answer;
				}
			}
		}
	}
}

class Addition {
	int x,y,a;
	public Addition(int x, int y, int a) {
		this.x = x;
		this.y = y;
		this.a = a;
	}
}