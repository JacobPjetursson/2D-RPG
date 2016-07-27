package entity;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.enemies.Enemy;
import handlers.Keys;
import handlers.MouseEvents;
import tileMap.TileMap;

public class Player extends Entity {

	// player stuff
	// private int mana;
	// private int maxMana;
	// private int level;

	// movement
	private int mouseX;
	private int mouseY;
	private boolean bottomSide;
	private boolean topSide;
	private boolean leftSide;
	private boolean rightSide;
	
	// fighting
	private boolean basicattacking;
	private boolean hit;
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 6, 6, 6, 6, 13, 13, 13, 13, 6 };
	// animation actions
	private static final int SPELL_NORTH = 0;
	private static final int SPELL_WEST = 1;
	private static final int SPELL_SOUTH = 2;
	private static final int SPELL_EAST = 3;
	private static final int THRUST_NORTH = 4;
	private static final int THRUST_WEST = 5;
	private static final int THRUST_SOUTH = 6;
	private static final int THRUST_EAST = 7;
	private static final int WALK_NORTH = 8;
	private static final int WALK_WEST = 9;
	private static final int WALK_SOUTH = 10;
	private static final int WALK_EAST = 11;
	private static final int ATTACK_NORTH = 12;
	private static final int ATTACK_WEST = 13;
	private static final int ATTACK_SOUTH = 14;
	private static final int ATTACK_EAST = 15;
	private static final int BOW_NORTH = 16;
	private static final int BOW_WEST = 17;
	private static final int BOW_SOUTH = 18;
	private static final int BOW_EAST = 19;
	private static final int KNOCKUP = 20;

	public Player(TileMap tm) {
		super(tm);

		width = 120;
		height = 120;
		cwidth = 80;
		cheight = 80;
		walkingcWidth = 55;
		walkingcHeight = 40;
		south = true;
		moveSpeed = 2.4;
		health = maxHealth = 5;

		// load sprites
		try {
			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites_Player/player_nothing.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < 20; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		currentAction = WALK_SOUTH;
		animation.setFrames(sprites.get(WALK_SOUTH));
		animation.setDelay(100);
		/*
		 * JukeBox.load("/SFX/jump.wav", "jump");
		 * JukeBox.load("/SFX/fireball.wav", "fireball");
		 * JukeBox.load("/SFX/scratch.wav", "scratch");
		 */
	}

	// BELOW IS FUNCTIONS USED IN UPDATE
	public void checkAttackAndCollision(ArrayList<Enemy> enemies) {
		leftSide = rightSide = topSide = bottomSide = false;
		Line2D.Double left = new Line2D.Double(x - walkingcWidth / 2, y - walkingcHeight / 2 + 5, x - walkingcWidth / 2, y + walkingcHeight / 2 - 5);
		Line2D.Double right = new Line2D.Double(x + walkingcWidth / 2, y - walkingcHeight / 2 + 5, x + walkingcWidth / 2, y + walkingcHeight / 2 - 5);
		Line2D.Double top = new Line2D.Double(x - walkingcWidth / 2 + 5, y - walkingcHeight / 2, x + walkingcWidth / 2 - 5, y - walkingcHeight / 2);
		Line2D.Double bottom = new Line2D.Double(x - walkingcWidth / 2 + 5, y + walkingcHeight / 2, x + walkingcWidth / 2 - 5, y + walkingcHeight / 2);

		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(walkingIntersects(e)) {
				getDirectionToCoords(mouseX, mouseY);
				if(e.getWalkingRectangle().intersectsLine(bottom)) {
					bottomSide = true;
				}
				else if(e.getWalkingRectangle().intersectsLine(top)) {
					topSide = true;
				}
				else if(e.getWalkingRectangle().intersectsLine(left)) {
					leftSide = true;
				}
				else if(e.getWalkingRectangle().intersectsLine(right)) {
					rightSide = true;
				}
			}
			if(basicattacking && !hit) {
					Line2D.Double basicattackLeft = new Line2D.Double(x+20*Math.sin(direction),(y-20*Math.cos(direction)),
							x+45*Math.cos(direction)+20*Math.sin(direction), y+45*Math.sin(direction)-20*Math.cos(direction));
					
					Line2D.Double basicattackRight = new Line2D.Double(x-20*Math.sin(direction),(y+20*Math.cos(direction)),
							x+45*Math.cos(direction)-20*Math.sin(direction), y+45*Math.sin(direction)+20*Math.cos(direction));
					
					Line2D.Double basicattackTop = new Line2D.Double(x+45*Math.cos(direction)+20*Math.sin(direction), y+45*Math.sin(direction)-20*Math.cos(direction),
							x+45*Math.cos(direction)-20*Math.sin(direction), y+45*Math.sin(direction)+20*Math.cos(direction));
					
					if(e.getWalkingRectangle().intersectsLine(basicattackLeft) || e.getWalkingRectangle().intersectsLine(basicattackRight) ||
							e.getWalkingRectangle().intersectsLine(basicattackTop)) {
						e.hit(2);
						hit = true;
					}
			}
		}	
	}
	
	// TODO - Make a function to define all the possible ways of attacking into 1 single variable: "attacking"
	
	// Moves the player after the mouseclick
	private void movePlayer() {
		if((!leftSide ||  Math.abs(direction) < Math.PI/2) && (!rightSide || Math.abs(direction) > Math.PI/2)) {
			dx = (moveSpeed * Math.cos(direction));
			x+=dx;
		}
		if((!topSide ||  direction > 0) && (!bottomSide || direction < 0)) {
			dy = (moveSpeed * Math.sin(direction));	
			y+=dy;
		}
	}

	// Determines if the player is close to the mouse (30 pixes)
	private boolean closeToMouse() {
		return x + 30 > mouseX && x - 30 < mouseX && y + 30 > mouseY && y - 30 < mouseY;
	}

	// Determines player direction to mouse
	private void getDirectionToCoords(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		float deltaX = (float) (this.mouseX - this.x);
		float deltaY = (float) (this.mouseY - this.y);
		direction = Math.atan2(deltaY, deltaX);

		if (Math.abs(direction) <= Math.PI / 4) {
			east = true;
			west = south = north = false;
		} else if (Math.abs(direction) > Math.PI * 3 / 4) {
			west = true;
			south = north = east = false;
		} else if (direction > Math.PI / 4 && direction <= Math.PI * 3 / 4) {
			south = true;
			west = north = east = false;
		} else {
			north = true;
			east = south = west = false;
		}
	}

	public void handleInput() {
		// move to mouse
		if ((MouseEvents.isPressed(MouseEvents.LEFTCLICK) && !Keys.keyState[Keys.SHIFT]) && !attacking) {
			getDirectionToCoords((int)(MouseEvents.mouseX - xmap),(int) (MouseEvents.mouseY - ymap));
			if (!closeToMouse()) {
				moving = true;
			}
		}
		if(Keys.keyState[Keys.SHIFT] && MouseEvents.isPressed(MouseEvents.LEFTCLICK) && !attacking){
			getDirectionToCoords((int)(MouseEvents.mouseX - xmap),(int) (MouseEvents.mouseY - ymap));	
			attacking = basicattacking = true;
			hit = false;
			moving = false;
		}

	}
	// Set the animations according to the current action performed
	public void setAnimation() {
		// SOUTH ANIMATIONS
		 if (south) {
			if (basicattacking) {
				if (currentAction != ATTACK_SOUTH) {
					currentAction = ATTACK_SOUTH;
					animation.setFrames(sprites.get(ATTACK_SOUTH));
					animation.setDelay(100);
				}
			}
			else {
				if(currentAction != WALK_SOUTH) {
					currentAction = WALK_SOUTH;
					animation.setFrames(sprites.get(WALK_SOUTH));
					animation.setDelay(100);		
				}
			}
		}
		// WEST ANIMATIONS
		 else if (west) {
				if (basicattacking) {
					if (currentAction != ATTACK_WEST) {
						currentAction = ATTACK_WEST;
						animation.setFrames(sprites.get(ATTACK_WEST));
						animation.setDelay(100);
					}
				}
				else {
					if(currentAction != WALK_WEST) {
						currentAction = WALK_WEST;
						animation.setFrames(sprites.get(WALK_WEST));
						animation.setDelay(100);		
					}
				}
			}
		// EAST ANIMATIONS
		else if (east) {
			if (basicattacking) {
				if (currentAction != ATTACK_EAST) {
					currentAction = ATTACK_EAST;
					animation.setFrames(sprites.get(ATTACK_EAST));
					animation.setDelay(100);
				}
			}
			else {
				if(currentAction != WALK_EAST) {
					currentAction = WALK_EAST;
					animation.setFrames(sprites.get(WALK_EAST));
					animation.setDelay(100);		
				}
			}
		}
		// NORTH ANIMATIONS
		else if (north) {
			if (basicattacking) {
				if (currentAction != ATTACK_NORTH) {
					currentAction = ATTACK_NORTH;
					animation.setFrames(sprites.get(ATTACK_NORTH));
					animation.setDelay(100);
				}
			}
			else {
				if(currentAction != WALK_NORTH) {
					currentAction = WALK_NORTH;
					animation.setFrames(sprites.get(WALK_NORTH));
					animation.setDelay(100);		
				}
			}
		}
	}
	private boolean idle() {
		return !moving && !attacking;
	}
	public void update() {
		handleInput();
		//checkTileMapCollision();
		setAnimation();
		// update player position
		if (moving) {
			movePlayer();
			if (closeToMouse()) {
				moving = false;
			}
		}
		// check attack has stopped
		if (attacking) {
			if(animation.hasPlayedOnce()) {
				attacking = basicattacking = false;
				setAnimation();
			}
		}
		// TODO - get an idle animation! Temp fix: only update when not standing still
		if(!idle()) {
			animation.update();	
		}
	}
	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
		
	}
	public void setBasicAttacking(boolean b) {
		basicattacking = b;
	}
}
