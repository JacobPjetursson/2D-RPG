package entity.objects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
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
	private Rectangle cRect;
	AffineTransform trans;

	public Arrow(TileMap tm, int x, int y, int mouseX, int mouseY) {
		super(tm);
		setMapPosition();
		cwidth = 6;
		cheight = 6;
		speed = 5;
		scale = 2.0;
		yOffSet = 40;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/Misc/arrow.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		length = height = (int) (img.getHeight() * scale);
		width = (int) (img.getWidth() * scale);
		this.x = x;
		this.y = y - yOffSet;
		direction = getDirectionToMouse(mouseX, mouseY);
		setRotation();
	}

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
		setMapPosition();
		setRotation();
		shootArrow();
	}

	public void draw(Graphics2D g) {
		
		//g.draw(cRect);
		g.drawImage(img, trans, null);

	}

	private void setRotation() {
		AffineTransform identity = new AffineTransform();
		trans = new AffineTransform();
		trans.setTransform(identity);
		trans.translate(x + xmap, y + ymap);
		trans.scale(scale, scale);
		trans.rotate(direction - (Math.PI / 2));

		Rectangle rTemp = new Rectangle(0, (int) (length / scale - cheight), cwidth, cheight);
		Shape cShape = trans.createTransformedShape(rTemp);
		// Rotation does not take xmap and ymap into account, so I need to substract these values when making my collision box
		cRect = new Rectangle((int) (cShape.getBounds().getX() - xmap), (int) (cShape.getBounds().getY() - ymap),
				(int) cShape.getBounds().getWidth(), (int) cShape.getBounds().getHeight());

	}

	@Override
	public Rectangle getRectangle() {
		return cRect;
	}
}
