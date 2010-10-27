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
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Miorel-Lucian Palii
 */
public class Logo {
	public static void main(String[] arg) {
		int size;
		if(arg.length > 0) {
			size = Integer.parseInt(arg[0]);
			if(size < 128) {
				System.err.println("Warning: small size. Expect errors!");
				System.err.println("Recommend rendering a larger image then scaling down.");
			}
		}
		else {
			System.err.println("No image size specified, using default.");
			size = 512;
		}

		Color backgroundColor = new Color(0, 0, 0, 0);
		Color circleColor = Color.RED;
		Color rectangleColor = Color.BLUE;
		Color textColor = Color.WHITE;

		int circleDiameter = (int) Math.round(((float) size) * 5 / 6);
		int circleThickness = (int) Math.round(((float) size) / 7);
		int rectangleHeight = (int) Math.round(((float) size) / 5);

		String title = "PRMF";

		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("SaccoVanzettiWebBold.ttf")).deriveFont(((float) size) * 5 / 32);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) img.getGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));

		g.setColor(backgroundColor);
		g.fillRect(0, 0, size, size);

		int outerDiameter = circleDiameter;
		int innerDiameter = outerDiameter - 2 * circleThickness;

		drawCenteredCircle(g, outerDiameter, circleColor, size, size);
		drawCenteredCircle(g, innerDiameter, backgroundColor, size, size);

		g.setColor(rectangleColor);
		g.fillRect(0, (size - rectangleHeight) / 2, size, rectangleHeight);

		g.setColor(textColor);
		g.setFont(font);

		FontMetrics fm = g.getFontMetrics();
		Rectangle2D r = fm.getStringBounds(title, g);
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();

		g.drawString(title, (size - width) / 2, (size - height) / 2 + fm.getAscent());

		try {
			ImageIO.write(img, "png", new File("logo.png"));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static void drawCenteredCircle(Graphics2D g2d, int diameter, Color color, int imageWidth, int imageHeight) {
		int x = (imageWidth - diameter) / 2;
		int y = (imageHeight - diameter) / 2;
		g2d.setColor(color);
		g2d.fillOval(x, y, diameter, diameter);
	}
}
