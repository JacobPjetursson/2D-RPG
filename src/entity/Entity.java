package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import tileMap.TileMap;

public abstract class Entity extends MapObject {
	// Fighting stuff
	protected double health;
	protected double maxHealth;
	protected boolean dead;
	protected int attackRange;
	protected int attackWidth;
	// actions
	protected boolean moving;
	protected boolean attacking;
	protected boolean idle;
	protected int[][] fsMachine;
	// states
	protected int north = 0;
	protected int west = 1;
	protected int south = 2;
	protected int east = 3;
	
	// States/Actions for the automata
	protected  int cState;
	protected int cAction;
	protected int pState;
	protected int pAction;
		
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
	/*
 	@Override
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth/2, (int)y-cheight/2, cwidth, cheight);
	}
	*/
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
	public void setWest(int i) {west = i; }
	public void setEast(int i) {east = i; }
	public void setNorth(int i) {north = i; }
	public void setSouth(int i) {south = i; }
	public boolean isDead() {return dead; }
	public double getHealth() {return health; }
	public double getMaxHealth() {	return maxHealth; }
	
	public abstract void handleInput();
	public void update() {
		
	}
	public void draw(Graphics2D g) {
		g.drawImage(animation.getImage(), (int)(x+xmap - width / 2), (int) (y+ymap-height/2)-40, width, height, null);
		
		g.draw(getRectangle());
	}
	

}
