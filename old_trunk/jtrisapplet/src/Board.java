/* JTrisApplet - Play a popular tetromino game inside a Java applet
   Version 4
   Copyright (C) 2010 Brian Nezvadovitz
   
   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>. */

import java.util.ArrayList;

// The game board

public class Board {
	
	private static final int width = 10;
	private static final int height = 21;
	private int curX;
	private int curY;
	public Shape shape = new Shape(0);
	private int board[] = new int[width * height];
	protected Boolean gameOver = false;
	
	public Board() {
		super();
	}
	
	public int[] getCurXy() {
		int xy[] = { curX, curY };
		return xy;
	}

	public int getCoords(int x, int y) {
		return board[(y * width) + x];
	}
	
	public void setCoords(int x, int y, final int shape) {
		board[(y * width) + x] = shape;
	}
	
	public boolean addPiece(final int piece) {
		int x = width / 2;
		int y = 1;
		final Shape shape = new Shape(piece);
		if(isPossibleMove(x, y, shape, false)) {
			this.shape = shape;
			curX = x;
			curY = y;
			return true;
		} else {
			return false;
		} // end if
	}
	
	public boolean isPossibleRotate(String direction) {
		shape.rotate(direction);
		if(isPossibleMove(0, 0, shape, true)) {
			return true;
		} else {
			if(direction.equals("right")) {
			shape.rotate("left");
			} else if(direction.equals("left")) {
			shape.rotate("right");
			}
			return false;
		}
	}
	
	public boolean isPossibleMove(int x, int y, Shape shape, boolean xyIsOffset) {
		int movedX, movedY;
		for(int i = 0; i < 4; i++) {
			if(xyIsOffset) {
				movedX = curX + shape.getXCoord(i) + x;
				movedY = curY + shape.getYCoord(i) + y;
			} else {
				movedX = shape.getXCoord(i) + x;
				movedY = shape.getYCoord(i) + y;
			}
			if(movedX < 0 || movedY >= width) {
				return false;
			} else if(movedY < 0 || movedY >= height) {
				return false;
			} else if(getCoords(movedX, movedY) != 0) {
				return false;
			}
		} // end for
		return true;
	}
	
	public void doMove(int x, int y) {
		curX += x;
		curY += y;
	}
	
	public int clearLines() {
		int rowsCleared = 0;
		int i;
		for(int y = 0; y < height; y++) {
			i = 0;
			for(int x = 0; x < width; x++) {
				if(getCoords(x, y) != 0) {
					i++;
				} else {
					break;
				}
			} // end for x
			if(i == width) {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				for(int j = 0; j < board.length; j++) {
					temp.add(board[j]);
				}
				for(int x = 0; x < width; x++) {
					temp.remove(y * width);
				}
				for(int x = 0; x < width; x++) {
					temp.add(0, 0);
				}
				for(int j = 0; j < board.length; j++) {
					board[j] = temp.get(j).intValue();
				}
				rowsCleared++;
			}
		} // end for y
		return rowsCleared;
	}

	public void merge() {
		int mergeX, mergeY;
		for(int i = 0; i < 4; i++) {
			mergeX = curX + shape.getXCoord(i);
			mergeY = curY + shape.getYCoord(i);
			board[(mergeY * width) + mergeX] = shape.getShapeNum();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setGameOver() {
		this.gameOver = true;
	}

}
