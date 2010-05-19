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
public class Tile {
	private final Map map;
	private final Terrain terrain;
	private final int x;
	private final int y;
	
	private Entity entity;
	
	public Tile(Map map, int x, int y, Terrain terrain) {
		this.map = map;
		this.x = x;
		this.y = y;
		this.terrain = terrain;
	}
	
	public Map getMap() {
		return this.map;
	}
	
	public Tile getNeighbor(int dx, int dy) {
		return this.map.getTile(this.x + dx, this.y + dy);
	}
	
	public Entity getEntity() {
		return this.entity;
	}
	
	public void setEntity(Entity e) {
		if(this.terrain.isPassable(e)) {
			Entity newEntity = e;
			Entity oldEntity = this.entity;
			
			if(oldEntity != newEntity) {
				this.entity = newEntity;
				
				if(oldEntity != null)
					oldEntity.setTile(null);
				if(newEntity != null)
					newEntity.setTile(this);
			}
		}
	}
	
	public Terrain getTerrain() {
		return this.terrain;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
