package entity.objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import entity.MapObject;
import tileMap.TileMap;

public class Arrow extends MapObject {
	private BufferedImage img;
	private double direction;
	private double speed;
	public Arrow(TileMap tm, int x, int y, double direction) {
		super(tm);
		this.x = x; 
		this.y = y;
		speed = 5;
		width = 60;
		height = 10;
		this.direction = direction;
		try {
			img = ImageIO
					.read(getClass().getResourceAsStream("/Misc/arrow.gif"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img, (int)(x+xmap), (int) (y+ymap), width, height, null);
		System.out.println("eh");
	}

}
