package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;
import tileMap.TileMap;

public abstract class MapObject {
	//Size
	protected int width;
	protected int height;
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	//position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	//dimensions
	protected int imgWidth;
	protected int imgHeight;
	
	// collison box
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	
	//constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth/2, (int)y-cheight/2, cwidth, cheight);
	}
	// TODO - do tile collision 
	/*
	public void checkTileMapCollision() {
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x,ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol+1)*tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x,ydest+1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	*/
	public void setPosition(int xtemp, int ytemp) {
		x = xtemp;
		y = ytemp;
	}
	public int getx() {return (int)x; }
	public int gety() {return (int)y; }
	public int getWidth() {return imgWidth; }
	public int getHeight() {return imgHeight; }
	public int getCWidth() {return cwidth; }
	public int getCHeight() {return cheight; }
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public boolean notOnScreen() {
		return x + xmap + 10 < 0 ||
			y + ymap + 10 < 0 ||
			x + xmap > GamePanel.WIDTH ||
			y + ymap - 10 > GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), (int)(x+xmap - imgWidth / 2), (int) (y+ymap-imgHeight/2), null);
	}
}
