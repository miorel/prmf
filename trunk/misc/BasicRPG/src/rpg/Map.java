/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package rpg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Miorel-Lucian Palii
 */
public class Map {
	private Tile[][] map;
	
	public Map(InputStream stream) {
		Scanner s = new Scanner(stream);
		List<Tile[]> tiles = new ArrayList<Tile[]>();
		while(s.hasNextLine()) {
			char[] arr = s.nextLine().toCharArray();
			int n = arr.length;
			
			Tile[] tileArr = new Tile[n];
			for(int i = 0; i < n; ++i)
				switch(arr[i]) {
				case ' ':
					tileArr[i] = new Tile(this, tiles.size(), i, Water.getInstance());
					break;
				case '.':
					tileArr[i] = new Tile(this, tiles.size(), i, Grass.getInstance());
					break;
				case 'X':
					tileArr[i] = new Tile(this, tiles.size(), i, Mountain.getInstance());
					break;
				default:
					throw new RuntimeException("Bad char in map!");
				}
			tiles.add(tileArr);
		}
		this.map = tiles.toArray(new Tile[tiles.size()][]);
	}
	
	public Tile getTile(int x, int y) {
		Tile ret = null;
		
		if(x >= 0 && x < this.map.length) {
			Tile[] arr = this.map[x];
			if(y >= 0 && y < arr.length)
				ret = arr[y];
		}
		
		if(ret == null)
			ret = new Tile(this, x, y, Water.getInstance());
		
		return ret;
	}
}
