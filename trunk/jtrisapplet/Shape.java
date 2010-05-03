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

// A class that morphs into the appropriate shape for use in the Board class.

public class Shape {
    
    private static final int pieceCoords[][][] = {
		{{0, 0},	 {0, 0},	 {0, 0},	 {0, 0}},	// 0: blank
		{{0, -1},	 {0, 0},	 {0, 1},	 {0, 2}},	// 1: line
		{{1, -1},	 {0, -1},	 {0, 0},	 {0, 1}},	// 2: inverted-L
		{{-1, -1},	 {0, -1},	 {0, 0},	 {0, 1}},	// 3: normal-L
		{{0, 0},	 {1, 0},	 {0, 1},	 {1, 1}},	// 4: square
		{{-1, 0},	 {0, 0},	 {1, 0},	 {0, 1}},	// 5: T-shape
		{{0, -1},	 {0, 0},	 {1, 0},	 {1, 1}},	// 6: normal-S
		{{0, -1},	 {0, 0},	 {-1, 0},	 {-1, 1}}	// 7: inverted-S
    };
    
    private int coords[][] = new int[4][2];
    private int shapeNum;
    
    public Shape(final int shapeNum) {
		this.shapeNum = shapeNum;
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 2; y++) {
				coords[x][y] = pieceCoords[this.shapeNum][x][y];
			}
		} // end for
    }

    public void rotate(String direction) {
		if(shapeNum == 4) {
			return;
		} else if(direction.equals("right")) {
			for(int i = 0; i < 4; i++) {
				setXyValues(i, -getYCoord(i), getXCoord(i));
			}
		} else if(direction.equals("left")) {
			for(int i = 0; i < 4; i++) {
				setXyValues(i, getYCoord(i), -getXCoord(i));
			}
		}
    }

    public int getXCoord(int pointNumber) {
		return coords[pointNumber][0];
    }

    public int getYCoord(int pointNumber) {
		return coords[pointNumber][1];
    }

    public void setXyValues(int pointNumber, int newXValue, int newYValue) {
		coords[pointNumber][0] = newXValue;
		coords[pointNumber][1] = newYValue;
    }

    public int getShapeNum() {
		return shapeNum;
    }

}
