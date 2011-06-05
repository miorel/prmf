import java.util.*;

public class B1 {
	public static void main(String[] args) {
		//divide map in quarters over and over with path, make everything else a wall
		int MAX = 600;
		char[][] board = new char[MAX][MAX];
		for(int i=0;i<MAX;++i) Arrays.fill(board[i], '#');
		List<Point> points = new LinkedList<Point>();
		points.add(new Point((MAX-1)/2,(MAX-1)/2));
		
		int lineDist = 149;
		for(int i=0;i<14;++i) {
			//13 splits to 5000!
			int pointCount = points.size();
			for(int k=pointCount-1;k>=0;--k) {
				Point cur = points.get(k);
				int curx = cur.x;
				int cury = cur.y;
				points.remove(cur);
				if (i%2 == 0) {
					Point toAdd1 = new Point(curx+lineDist,cury);
					Point toAdd2 = new Point(curx-lineDist,cury);
					for(int m=toAdd2.x;m<=toAdd1.x;++m) {
						board[cury][m] = '.';
					}
					points.add(toAdd1);
					points.add(toAdd2);
				}
				else {
					Point toAdd1 = new Point(curx,cury+lineDist);
					Point toAdd2 = new Point(curx,cury-lineDist);
					for(int m=toAdd2.y;m<=toAdd1.y;++m) {
						board[m][curx] = '.';
					}
					points.add(toAdd1);
					points.add(toAdd2);
				}
			}
			if (i%2 == 1) {
				lineDist = (lineDist)/2;
				/*if (lineDist == 3) {
					lineDist = 5;
				}*/
			}
		}
		board[(MAX-1)/2][(MAX-1)/2] = 'S';
		board[0][0] = 'T';
		for(int i=0;i<MAX;++i) {
			for(int k=0;k<MAX;++k) {
				System.out.print(board[i][k]);
			}
			System.out.println();
		}
		//split(board,0,MAX-1,0,MAX-1);
		//board[300][300] = 'S';
		//board[0][0] = 'T';
	}
	
	public static void split(char[][] board, int xstart, int xend, int ystart, int yend) {
		//int midx = (xstart+xend)/2;
		//int midy = (ystart+yend)/2;
	}
}

class Point {
	int x, y;
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}