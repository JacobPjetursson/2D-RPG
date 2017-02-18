package entity.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import entity.MapObject;
import tileMap.TileMap;

public class Arrow extends MapObject {
	private BufferedImage img;
	private double direction;
	private double speed;
	private int length;
	private double scale;
	private int yOffSet;
	
	public Arrow(TileMap tm, int x, int y, int mouseX, int mouseY) {
		super(tm);
		setMapPosition();
		cwidth = cheight = 8;
		speed = 5;
		scale = 2.0;
		yOffSet = 40;
		try {
			img = ImageIO
					.read(getClass().getResourceAsStream("/Misc/arrow.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		length = height =(int) (img.getHeight()*scale);
		width = (int) (img.getWidth()*scale);
		this.x = x;
		this.y = y-yOffSet;
		direction = getDirectionToMouse(mouseX, mouseY);
	}
		// TODO - fix the arrow movement to take player movement into consideration
		private void shootArrow() {
				dx = (speed * Math.cos(direction));
				x += dx;
				dy = (speed * Math.sin(direction));
				y += dy;
		}
	private double getDirectionToMouse(int mouseX, int mouseY) {
		float deltaX = (float) (mouseX - x);
		float deltaY = (float) (mouseY - y);
		direction = Math.atan2(deltaY, deltaX);
		return direction;
	}
	public void update() {
		shootArrow();
	}
	public void draw(Graphics2D g) {
		AffineTransform identity = new AffineTransform();
		AffineTransform trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(x+xmap, y+ymap);
		//trans.setToTranslation(x, y);
		trans.scale(scale,scale);
		trans.rotate(direction-(Math.PI/2));
		g.drawImage(img, trans, null);
		
		//g.draw(getRectangle());
		//g.drawRect((int)(x + (length) * Math.cos(direction))+(int)xmap, (int)(y + (length) * Math.sin(direction))+(int)ymap, 10, 10);

		
	}
	// TODO - Fix the hitbox - is under the arrowhead when shooting to the left. Seems to be the image that is off
	@Override
	public Rectangle getRectangle() {
		return new Rectangle((int)(x + (length-cwidth) * Math.cos(direction)), (int)(y + (length-cwidth) * Math.sin(direction)), cwidth, cheight);
	}
}
