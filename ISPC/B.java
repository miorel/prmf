class B {
	public static void main(String[] arg) {
		int n = 600;
		char[][] grid = new char[n][n];
		for(int i = 0; i < n; ++i) for(int j = 0; j < n; ++j) grid[i][j] = '.';
		grid[n / 2][n / 2] = 'S';
		grid[0][0] = 'T';
		System.out.println(n + " " + n);
		for(char[] row: grid) System.out.println(row);
	}
}
