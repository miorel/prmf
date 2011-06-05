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
			
			boolean bottomLeft, bottomRight, topLeft, temp1, temp2;
			bottomLeft = bottomRight = topLeft = false;
			
			if((r * c) % 2 == 1) System.out.println("IMPOSSIBLE");
			else {
				if(!( (ri - 1) == (r - ri) || (ci - 1) == (r - ci) || (ri > ci && rj == c && ri < r/2) || (ri < ci && rj == 0 && ri < r/2))) {
					for(int i = rj; i > 0; --i) if(grid[ri- 1][i - 1] == '.'){ //iterated left from initial
							grid[ri - 1][i - 1] = 'R';
							grid[r - ri][c - i] = 'B';
						}
						for(int i = c - cj; i > 0; --i) if(grid[r - ci][i] == '.'){
							grid[r - ci][i] = 'R';
							grid[ci - 1][c - i - 1] = 'B';
						}
						for(int i = 0; i < r; ++i) if(grid[i][0] == '.') {
							grid[i][0] = 'R';
							grid[r - i - 1][c - 1] = 'B';
						}
				} else if(!(ri < ci && rj == 0 && ri < r/2)) {
					for(int i = ri; i > 0; --i) if(grid[i - 1][rj - 1] == '.'){ //iterating up
							grid[i - 1][rj - 1] = 'R';
							grid[r - i][c - rj] = 'B';
						}
						for(int i = ci; i < r; ++i) if(grid[i - 1][cj - 1] == '.') {
							grid[i - 1][cj - 1] = 'B';
							grid[r - i][c - cj] = 'R';
						}
						for(int i = 0; i < c; ++i) if(grid[0][i] == '.') {
							grid[0][i] = 'R';
							grid[r - 1][c - i - 1] = 'B';
						}
				} else {
						for(int i = rj; i < c; ++i) if(grid[ri- 1][i - 1] == '.'){ //iterated right from initial
							grid[ri - 1][i - 1] = 'R';
							grid[r - ri][c - i] = 'B';
						}
						for(int i = c - cj; i < c; ++i) if(grid[r - ci][i] == '.'){
							grid[r - ci][i] = 'R';
							grid[ci - 1][c - i - 1] = 'B';
						}
						for(int i = 0; i < r; ++i) if(grid[i][c-1] == '.') {
							grid[i][c-1] = 'R';
							grid[r - i + 1][0] = 'B';
						}
				}
				/*if(ri < ci && rj < cj) { // red below and left of blue
					if(r % 2 == 0) { //even number of rows
						for(int i = rj; i < c; ++i) if(grid[ri- 1][i - 1] == '.'){ //iterated right from initial
							grid[ri - 1][i - 1] = 'R';
							grid[r - ri][c - i] = 'B';
						}
						for(int i = c - cj; i < c; ++i) if(grid[r - ci][i] == '.'){
							grid[r - ci][i] = 'R';
							grid[ci - 1][c - i - 1] = 'B';
						}
						for(int i = 0; i < r; ++i) if(grid[i][c-1] == '.') {
							grid[i][c-1] = 'R';
							grid[r - i + 1][0] = 'B';
						}
						
						//check bottomRight corner
						temp2 = (grid[r-1][c-1] == 'R');
						
						if(temp2)
							bottomRight = true;
						else 
							topLeft = true;
							
					} else { //even number of columns
						for(int i = ri; i > 0; --i) if(grid[i - 1][rj - 1] == '.'){ //iterating up
							grid[i - 1][rj - 1] = 'R';
							grid[r - i][c - rj] = 'B';
						}
						for(int i = ci; i < r; ++i) if(grid[i - 1][cj - 1] == '.') {
							grid[i - 1][cj - 1] = 'B';
							grid[r - i][c - cj] = 'R';
						}
						for(int i = 0; i < c; ++i) if(grid[0][i] == '.') {
							grid[0][i] = 'R';
							grid[r - 1][c - i + 1] = 'B';
						}
						
						//check topLeft corner
						topLeft = (grid[0][0] == 'R');
						
						if(!temp1)
							bottomRight = true;
					}
				} else if(ri == ci && rj < cj) { //red directly left of blue
					if(r % 2 == 0) { //even number of rows
						for(int i = rj; i > 0; --i) if(grid[ri- 1][i - 1] == '.'){ //iterated left from initial
							grid[ri - 1][i - 1] = 'R';
							grid[r - ri][c - i] = 'B';
						}
						for(int i = c - cj; i > 0; --i) if(grid[r - ci][i] == '.'){
							grid[r - ci][i] = 'R';
							grid[ci - 1][c - i - 1] = 'B';
						}
						for(int i = 0; i < r; ++i) if(grid[i][0] == '.') {
							grid[i][0] = 'R';
							grid[r - i + 1][c - 1] = 'B';
						}
						
						//check topLeft corner
						temp2 = (grid[0][0] == 'R');
						
						if(temp2)
							topLeft = true;
						else 
							bottomRight = true;
							
					} else { //even number of columns
						for(int i = ri; i > 0; --i) if(grid[i - 1][rj - 1] == '.'){ //iterating up
							grid[i - 1][rj - 1] = 'R';
							grid[r - i][c - rj] = 'B';
						}
						for(int i = ci; i < r; ++i) if(grid[i - 1][cj - 1] == '.') {
							grid[i - 1][cj - 1] = 'B';
							grid[r - i][c - cj] = 'R';
						}
						for(int i = 0; i < c; ++i) if(grid[0][i] == '.') {
							grid[0][i] = 'R';
							grid[r - 1][c - i + 1] = 'B';
						}
						
						//check topLeft corner
						topLeft = (grid[0][0] == 'R');
						
						if(!temp1)
							bottomRight = true;
					}
				} else if(ri > ci && rj < cj) { //red above and left of blue
					if(r % 2 == 0) { //even number of rows
						for(int i = rj; i > 0; --i) if(grid[ri- 1][i - 1] == '.'){ //iterated left from initial
							grid[ri - 1][i - 1] = 'R';
							grid[r - ri][c - i] = 'B';
						}
						for(int i = c - cj; i > 0; --i) if(grid[r - ci][i] == '.'){
							grid[r - ci][i] = 'R';
							grid[ci - 1][c - i - 1] = 'B';
						}
						for(int i = 0; i < r; ++i) if(grid[i][0] == '.') {
							grid[i][0] = 'R';
							grid[r - i + 1][c - 1] = 'B';
						}
						
						//check topLeft corner
						temp2 = (grid[0][0] == 'R');
						
						if(temp2)
							topLeft = true;
						else 
							bottomRight = true;
					} else { //even number of columns
						for(int i = ri; i > 0; --i) if(grid[i - 1][rj - 1] == '.'){ //iterating up
							grid[i - 1][rj - 1] = 'R';
							grid[r - i][c - rj] = 'B';
						}
						for(int i = ci; i < r; ++i) if(grid[i - 1][cj - 1] == '.') {
							grid[i - 1][cj - 1] = 'B';
							grid[r - i][c - cj] = 'R';
						}
						for(int i = 0; i < c; ++i) if(grid[0][i] == '.') {
							grid[0][i] = 'R';
							grid[r - 1][c - i + 1] = 'B';
						}
						
						//check topLeft corner
						topLeft = (grid[0][0] == 'R');
						
						if(!temp1)
							bottomRight = true;
					}
				} else if(ri > ci && rj == cj) { //red directly above blue
					if(c % 2 == 0) { //even number of columns
						for(int i = ri; i > 0; --i) if(grid[i - 1][rj - 1] == '.'){ //iterating up
							grid[i - 1][rj - 1] = 'R';
							grid[r - i][c - rj] = 'B';
						}
						for(int i = ci; i < r; ++i) if(grid[i - 1][cj - 1] == '.') {
							grid[i - 1][cj - 1] = 'B';
							grid[r - i][c - cj] = 'R';
						}
						for(int i = 0; i < c; ++i) if(grid[0][i] == '.') {
							grid[0][i] = 'R';
							grid[r - 1][c - i + 1] = 'B';
						}
						
						//check topLeft corner
						topLeft = (grid[0][0] == 'R');
						
						if(!temp1)
							bottomRight = true;
					} else { //odd number of columns 
						
					}
				} else if(ri > ci && rj > cj) { //red above and right of blue
					if(r % 2 == 0) { //even number of rows
					
					} else { //even number of columns
					
					}
				} else if(ri == ci && rj > cj) { // red directly right of blue
					if(r % 2 == 0) { //even number of rows
					
					} else { //even number of columns
					
					}
				} else if(ri < ci && rj > cj) { //red below and right of blue
					if(r % 2 == 0) { //even number of rows
					
					} else { //even number of columns
					
					}
				} else { //red directly below blue
					if(r % 2 == 0) { //even number of rows
					
					} else { //even number of columns
					
					}
				}*/
				
				if(grid[0][0] == 'R') {
					for(int i = 0; i < r; ++i) for(int j = 0; j < c; ++j) if(grid[i][j] == '.') {
						grid[i][j] = 'R';
						grid[r - 1 - i][c - 1 - j] = 'B';
					}
				} else if(grid[0][c-1] == 'R') {
					for(int i = 0; i < r; ++i) for(int j = c-1; j >= 0; --j) if(grid[i][j] == '.') {
						grid[i][j] = 'R';
						grid[r - 1 - i][c - 1 - j] = 'B';
					}
				} else if(grid[r-1][0] == 'R') {
					for(int i = r-1; i >= 0; --i) for(int j = 0; j < c; ++j) if(grid[i][j] == '.') {
						grid[i][j] = 'R';
						grid[r - 1 - i][c - 1 - j] = 'B';
					}
				} else {
					for(int i = r-1; i >= 0; --i) for(int j = c-1; j >= 0; --j) if(grid[i][j] == '.') {
						grid[i][j] = 'R';
						grid[r - 1 - i][c - 1 - j] = 'B';
					}
				}
				for(char[] row: grid) System.out.println(row);
			}
			if(t != 0) System.out.println();
		}
	}
}
