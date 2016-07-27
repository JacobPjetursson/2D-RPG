package entity.enemies;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.Animation;
import entity.Player;
import tileMap.TileMap;

public class Orc extends Enemy {
	//fighting
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

	public Orc(TileMap tm, Player player) {
		super(tm, player);

		width = 120;
		height = 120;
		cwidth = 80;
		cheight = 80;
		walkingcWidth = 55;
		walkingcHeight = 40;
		// TODO - Make randomized direction, or input
		south = true;
		
		moveSpeed = 1.8;
		health = maxHealth = 5;
		
		attackRange = 50;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites_Enemies/orc_nothing.gif"));
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
		animation.setDelay(80);
		/*
		 * JukeBox.load("/SFX/jump.wav", "jump");
		 * JukeBox.load("/SFX/fireball.wav", "fireball");
		 * JukeBox.load("/SFX/scratch.wav", "scratch");
		 */
	}
	public void checkAttack() {
		if(inAttackRange(player) && !attacking) {
			basicattacking = attacking = true;
			getDirectionToCoords(player.getx(), player.gety());
			Line2D.Double basicattackLeft = new Line2D.Double(x+20*Math.sin(direction),(y-20*Math.cos(direction)),
					x+45*Math.cos(direction)+20*Math.sin(direction), y+45*Math.sin(direction)-20*Math.cos(direction));
				
			Line2D.Double basicattackRight = new Line2D.Double(x-20*Math.sin(direction),(y+20*Math.cos(direction)),
					x+45*Math.cos(direction)-20*Math.sin(direction), y+45*Math.sin(direction)+20*Math.cos(direction));
				
			Line2D.Double basicattackTop = new Line2D.Double(x+45*Math.cos(direction)+20*Math.sin(direction), y+45*Math.sin(direction)-20*Math.cos(direction),
					x+45*Math.cos(direction)-20*Math.sin(direction), y+45*Math.sin(direction)+20*Math.cos(direction));
				
			if(player.getWalkingRectangle().intersectsLine(basicattackLeft) || player.getWalkingRectangle().intersectsLine(basicattackRight) ||
					player.getWalkingRectangle().intersectsLine(basicattackTop)) {
				player.hit(1);
			}
		}
	}	
	// BELOW IS FUNCTIONS USED IN UPDATE
	public void handleInput() {
		
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
		checkAttack();
		handleInput();
		setAnimation(); 
		// check attack has stopped
		if (attacking) {
			if(animation.hasPlayedOnce()) {
				basicattacking = attacking = false;
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
