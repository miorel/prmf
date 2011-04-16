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

/**
 * @author Miorel-Lucian Palii
 */
public class Entity {
	private Tile tile;
	
	public Entity() {
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public void setTile(Tile tile) {
		Tile newTile = tile;
		Tile oldTile = this.tile;
		
		if(oldTile != newTile) {
			this.tile = newTile;
			
			if(oldTile != null)
				oldTile.setEntity(null);
			if(newTile != null)
				newTile.setEntity(this);
		}
	}
	
	public void move(int dx, int dy) {
		Tile destination = this.tile.getNeighbor(dx, dy);
		if(destination.getTerrain().isPassable(this))
			destination.setEntity(this);
	}
}
