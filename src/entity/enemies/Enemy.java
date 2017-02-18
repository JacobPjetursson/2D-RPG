package entity.enemies;

import java.awt.Color;
import java.awt.Graphics2D;

import entity.Entity;
import entity.Player;
import tileMap.TileMap;

public abstract class Enemy extends Entity {
	protected Player player;
	public Enemy(TileMap tm, Player p) {
		super(tm);
		this.player = p;
	}
	public void displayMissingHealth(Graphics2D g) {
		if(health != maxHealth) {
			double healthPercent = health/maxHealth;
			double greenValue = 200 * (healthPercent);
			g.setColor(new Color(75, (int)greenValue, 0));
			g.fillRect((int) (x-cwidth/4+xmap),(int) (y-cheight+ymap), (int) (healthPercent*cwidth/2), 7);
			
			g.setColor(Color.BLACK);
			g.drawRect((int) (x-cwidth/4+xmap),(int) (y-cheight+ymap), cwidth/2, 7);
			
		}
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
		displayMissingHealth(g);
	}
	
	public void getDirectionToCoords(int destX, int destY) {
		float deltaX = (float) (destX - this.x);
		float deltaY = (float) (destY - this.y);
		direction = Math.atan2(deltaY, deltaX);

		if (Math.abs(direction) <= Math.PI / 4) {
			cState = east;
		} else if (Math.abs(direction) > Math.PI * 3 / 4) {
			cState = west;
		} else if (direction > Math.PI / 4 && direction <= Math.PI * 3 / 4) {
			cState = south;
		} else {
			cState = north;
		}
	}
	public void moveEnemy() {
		dx = (moveSpeed * Math.cos(direction));
		dy = (moveSpeed * Math.sin(direction));	
		x+=dx;
		y+=dy;
			
	}	
	public abstract void setWalking(int x, int y);
	
	public boolean inAttackRange(Entity e) {
		int ex = e.getx();
		int ey = e.gety();
		return x + attackRange > ex && x - attackRange < ex && y + attackRange > ey && y - attackRange < ey;
	}
}
