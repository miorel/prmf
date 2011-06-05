import java.util.*;

class D {
	public static void main(String[] arg) {
		Scanner sc = new Scanner(System.in);
		for(int t = sc.nextInt(); t-->0;) {
			int r = sc.nextInt();
			int c = sc.nextInt();
			char[][] grid = new char[r][c];
			for(int i = 0; i < r; ++i) for(int j = 0; j < c; ++j) grid[i][j] = '.';
			int ri = sc.nextInt();
			int rj = sc.nextInt();
			grid[ri - 1][rj - 1] = 'R';
			grid[r - ri][c - rj] = 'B';
			int ci = sc.nextInt();
			int cj = sc.nextInt();
			grid[ci - 1][cj - 1] = 'B';
			grid[r - ci][c - cj] = 'R';
			if((r * c) % 2 == 1) System.out.println("IMPOSSIBLE");
			else {
				for(int i = 0; i < r; ++i) for(int j = 0; j < c; ++j) if(grid[i][j] == '.') {
					grid[i][j] = 'R';
					grid[r - 1 - i][c - 1 - j] = 'B';
				}
				for(char[] row: grid) System.out.println(row);
			}
			if(t != 0) System.out.println();
		}
	}
}
