package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import tileMap.TileMap;

public abstract class Entity extends MapObject {
	// Fighting stuff
	protected double health;
	protected double maxHealth;
	protected boolean dead;
	protected boolean attacking;
	
	// movement
	protected boolean moving;
	protected boolean west;
	protected boolean east;
	protected boolean north;
	protected boolean south;
	
		
	// movement attributes
	protected double moveSpeed;
	protected double direction;
	protected int walkingcWidth;
	protected int walkingcHeight;
	
		// constructor
	public Entity(TileMap tm) {
		super(tm);
	}
	public boolean walkingIntersects(Entity e) {
		Rectangle r1 = getWalkingRectangle();
		Rectangle r2 = e.getWalkingRectangle();
		return r1.intersects(r2);
	}
	public Rectangle getWalkingRectangle() {
		return new Rectangle((int)x - walkingcWidth/2, (int)y-walkingcHeight/2, walkingcWidth, walkingcHeight);
	}
	public void hit(int damage) {
		health -= damage;
		if (health < 0)
			health = 0;
		if (health == 0) {
			dead = true;
		}
	}
	public void setMoving(boolean b) {moving = b; }
	public boolean getMoving() {return moving; }
	public void setWest(boolean b) {west = b; }
	public void setEast(boolean b) {east = b; }
	public void setNorth(boolean b) {north = b; }
	public void setSouth(boolean b) {south = b; }
	public boolean isDead() {return dead; }
	public double getHealth() {return health; }
	public double getMaxHealth() {	return maxHealth; }
	
	public abstract void handleInput();
	public void update() {
		
	}
	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), (int)(x+xmap - width / 2), (int) (y+ymap-height/2)-40, null);
	}
	

}
