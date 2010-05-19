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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * @author Miorel-Lucian Palii
 */
public class View {
	private final Model model;
	private final Component component;
	
	private final Dimension dim;
	
	private final java.util.Map<String,BufferedImage> images;
	private final int tileWidth;
	private final int tileHeight;
	
	public View(Model model, Component component) throws IOException {
		this.model = model;
		this.component = component;
		
		this.dim = new Dimension();
		this.images = new HashMap<String,BufferedImage>();
		
		this.images.put("grass", ImageIO.read(getClass().getResourceAsStream("grass.png")));
		this.images.put("mountain", ImageIO.read(getClass().getResourceAsStream("mountain.png")));
		this.images.put("water", ImageIO.read(getClass().getResourceAsStream("water.png")));
		this.images.put("entity", ImageIO.read(getClass().getResourceAsStream("fighter.png")));
		
		this.tileWidth = this.images.get("water").getWidth();
		this.tileHeight = this.images.get("water").getHeight();
	}
	
	public void paint() {
		this.component.getSize(this.dim);
		Image img = new BufferedImage(this.dim.width, this.dim.height, BufferedImage.TYPE_INT_ARGB);
		
		Tile center = this.model.getEntity().getTile();
		int centerHoriz = center.getX();
		int centerVert = center.getY();
		
		int sizeHoriz = dim.width / tileWidth;
		int sizeVert = dim.height / tileHeight;
		
		int minHoriz = centerHoriz - (sizeHoriz - 1) / 2 - 1;
		int maxHoriz = centerHoriz + (sizeHoriz - 1) / 2 + 1;
		int minVert = centerVert - (sizeVert - 1) / 2 - 1;
		int maxVert = centerVert + (sizeVert - 1) / 2 + 1;

		Map map = this.model.getMap();
		for(int i = minHoriz; i <= maxHoriz; ++i) {
			for(int j = minVert; j <= maxVert; ++j) {
				Tile tile = map.getTile(i, j);
				
				if(tile.getTerrain() instanceof Mountain) {
					img.getGraphics().drawImage(this.images.get("mountain"), (this.dim.width - tileWidth) / 2 - (centerHoriz - i) * tileWidth, (this.dim.height - tileHeight) / 2 - (centerVert - j) * tileHeight, null);
				}
				if(tile.getTerrain() instanceof Water) {
					img.getGraphics().drawImage(this.images.get("water"), (this.dim.width - tileWidth) / 2 - (centerHoriz - i) * tileWidth, (this.dim.height - tileHeight) / 2 - (centerVert - j) * tileHeight, null);
				}
				if(tile.getTerrain() instanceof Grass) {
					img.getGraphics().drawImage(this.images.get("grass"), (this.dim.width - tileWidth) / 2 - (centerHoriz - i) * tileWidth, (this.dim.height - tileHeight) / 2 - (centerVert - j) * tileHeight, null);
				}
				
				if(tile.getEntity() != null) {
					img.getGraphics().drawImage(this.images.get("entity"), (this.dim.width - tileWidth) / 2 - (centerHoriz - i) * tileWidth, (this.dim.height - tileHeight) / 2 - (centerVert - j) * tileHeight, null);
				}
			}	
		}
		
		Graphics g = this.component.getGraphics();
		if(g != null)
			g.drawImage(img, 0, 0, null);
	}
}
