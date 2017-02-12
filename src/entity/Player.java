package entity;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import entity.enemies.Enemy;
import entity.objects.Arrow;
import handlers.Keys;
import handlers.MouseEvents;
import tileMap.TileMap;

public class Player extends Entity {

	// player stuff
	// private int mana;
	// private int maxMana;
	// private int level;
	
	// movement
	// mouse coords (not updated constantly)
	private int mouseX;
	private int mouseY;
	// Collision detectors, to prevent walking into enemies
	private boolean bottomSide;
	private boolean topSide;
	private boolean leftSide;
	private boolean rightSide;
	
	// fighting
	private boolean hitEnemy;
	private int cEquip;
	private ArrayList<Arrow> arrows;
	// animations
	private ArrayList<BufferedImage[]> sprites;
	// # of frames per animation
	private final int[] numFrames = { 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 6, 6, 6, 6, 11, 11, 11, 11, 6 };
	
	// ACTIONS
	int SPELL = 0;
	int THRUST = 1;
	int WALK = 2;
	int ATTACK = 3;
	int BOW = 4;
	int KNOCKUP_SOUTH = 5;
	// 6 actions as columns, 4 states as rows
	// If I wanted to change the order of the numbers, i would have to change
	// the spritesheet or the row/column order. This will do
	
	// EQUIPS
	int E_MELEE = 0;
	int E_BOW = 1;

	public Player(TileMap tm) {
		super(tm);
		fsMachine = new int[][] { { 0, 4, 8, 12, 16, -1 }, { 1, 5, 9, 13, 17, -1 }, { 2, 6, 10, 14, 18, 20 },
				{ 3, 7, 11, 15, 19, -1 } };
				
		cAction = WALK;
		cState = south;
		cEquip = E_BOW;
		width = 120;
		height = 120;
		cwidth = 80;
		cheight = 80;
		walkingcWidth = 55;
		walkingcHeight = 40;
		moveSpeed = 2.4;
		health = maxHealth = 5;

		attackRange = 50;
		attackWidth = 20;
		arrows = new ArrayList<Arrow>();
		// load sprites
		// TODO - Make it so bow/arrow is supported, for example
		try {
			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites_Player/player_bow.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			int imgsinWidth = 13;
			int imgsinHeight = 21;
			imgHeight = spritesheet.getHeight()/imgsinHeight;
			imgWidth = spritesheet.getWidth()/imgsinWidth;
			for (int i = 0; i < numFrames.length; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for (int j = 0; j < numFrames[i]; j++) {
					bi[j] = spritesheet.getSubimage(j * imgWidth, i * imgHeight, imgWidth, imgHeight);
				}
				sprites.add(bi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		animation = new Animation();
		animation.setFrames(sprites.get(fsMachine[cState][cAction]));
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
		Line2D.Double left = new Line2D.Double(x - walkingcWidth / 2, y - walkingcHeight / 2 + 5, x - walkingcWidth / 2,
				y + walkingcHeight / 2 - 5);
		Line2D.Double right = new Line2D.Double(x + walkingcWidth / 2, y - walkingcHeight / 2 + 5,
				x + walkingcWidth / 2, y + walkingcHeight / 2 - 5);
		Line2D.Double top = new Line2D.Double(x - walkingcWidth / 2 + 5, y - walkingcHeight / 2,
				x + walkingcWidth / 2 - 5, y - walkingcHeight / 2);
		Line2D.Double bottom = new Line2D.Double(x - walkingcWidth / 2 + 5, y + walkingcHeight / 2,
				x + walkingcWidth / 2 - 5, y + walkingcHeight / 2);

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if (walkingIntersects(e)) {
				getDirectionToCoords(mouseX, mouseY);
				if (e.getWalkingRectangle().intersectsLine(bottom)) {
					bottomSide = true;
				}
				if (e.getWalkingRectangle().intersectsLine(top)) {
					topSide = true;
				}
				if (e.getWalkingRectangle().intersectsLine(left)) {
					leftSide = true;
				}
				if (e.getWalkingRectangle().intersectsLine(right)) {
					rightSide = true;
				}
			}
			if (attacking && !hitEnemy) {
				// Bottom is not needed, since it
				Line2D.Double meleeAttackLeft = new Line2D.Double(x + attackWidth * Math.sin(direction),
						(y - attackWidth * Math.cos(direction)),
						x + attackRange * Math.cos(direction) + attackWidth * Math.sin(direction),
						y + attackRange * Math.sin(direction) - attackWidth * Math.cos(direction));

				Line2D.Double meleeAttackRight = new Line2D.Double(x - attackWidth * Math.sin(direction),
						(y + attackWidth * Math.cos(direction)),
						x + attackRange * Math.cos(direction) - attackWidth * Math.sin(direction),
						y + attackRange * Math.sin(direction) + attackWidth * Math.cos(direction));

				Line2D.Double meleeAttackTop = new Line2D.Double(
						x + attackRange * Math.cos(direction) + attackWidth * Math.sin(direction),
						y + attackRange * Math.sin(direction) - attackWidth * Math.cos(direction),
						x + attackRange * Math.cos(direction) - attackWidth * Math.sin(direction),
						y + attackRange * Math.sin(direction) + attackWidth * Math.cos(direction));

				if (e.getWalkingRectangle().intersectsLine(meleeAttackLeft)
						|| e.getWalkingRectangle().intersectsLine(meleeAttackRight)
						|| e.getWalkingRectangle().intersectsLine(meleeAttackTop)) {
					e.hit(2);
					hitEnemy = true;
				}
			}
		}
	}

	// Moves the player after the mouseclick
	private void movePlayer() {
		if ((!leftSide || Math.abs(direction) < Math.PI / 2) && (!rightSide || Math.abs(direction) > Math.PI / 2)) {
			dx = (moveSpeed * Math.cos(direction));
			x += dx;
		}
		if ((!topSide || direction > 0) && (!bottomSide || direction < 0)) {
			dy = (moveSpeed * Math.sin(direction));
			y += dy;
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
			cState = east;
		} else if (Math.abs(direction) > Math.PI * 3 / 4) {
			cState = west;
		} else if (direction > Math.PI / 4 && direction <= Math.PI * 3 / 4) {
			cState = south;
		} else {
			cState = north;
		}
	}

	public void handleInput() {
		// move to mouse. Don't wanna move if i'm attacking.
		if ((MouseEvents.isPressed(MouseEvents.LEFTCLICK) && !Keys.keyState[Keys.SHIFT]) && !attacking) {
			getDirectionToCoords((int) (MouseEvents.mouseX - xmap), (int) (MouseEvents.mouseY - ymap));
			if (!closeToMouse()) {
				// TODO - constantly update moving and attacking, so that if one
				// if false, the other is true, etc.)
				moving = true;
			}
		}
		// Dont wanna attack if im already attacking
		if (Keys.keyState[Keys.SHIFT] && MouseEvents.isPressed(MouseEvents.LEFTCLICK) && !attacking) {
			getDirectionToCoords((int) (MouseEvents.mouseX - xmap), (int) (MouseEvents.mouseY - ymap));
			attacking = true;
			if(cEquip == E_BOW) {
				arrows.add(new Arrow(tileMap, (int)x, (int)y, direction));
			}
		}

	}

	// Set the animations according to the current action performed
	public void setAnimation() {
		if (!(sprites.get(fsMachine[pState][pAction]) == sprites.get(fsMachine[cState][cAction]))) {
			animation.setFrames(sprites.get(fsMachine[cState][cAction]));
			if(cAction == BOW) {
				animation.setDelay(70);
			}
			pState = cState;
			pAction = cAction;
		}

	}

	private void setState() {
		if (!moving && !attacking) {
			idle = true;
		} else {
			idle = false;

			if (attacking) {
				if(cEquip == E_BOW) {
					cAction = BOW;
				}
				else if(cEquip == E_MELEE) {
					cAction = ATTACK;	
				}
				moving = false;
			} else if (moving) {
				cAction = WALK;
				attacking = false;
			}
		}
	}

	public void update() {
		handleInput();
		// checkTileMapCollision();
		setAnimation();
		setState();
		// update player position
		if (moving) {
			movePlayer();
			if (closeToMouse()) {
				moving = false;
			}
		}
		// check attack has stopped
		if (animation.hasPlayedOnce()) {
			animation.setFrames(sprites.get(fsMachine[cState][cAction]));
			attacking = false;
			hitEnemy = false;
		}

		if (!idle) {
			animation.update();
		}
		System.out.println(arrows.size());
		// UPDATE ARROWS
		// TODO - when spells arrive, make list of MapObjects instead,
		for(Arrow a : arrows) {
			if(a.notOnScreen()) {
				arrows.remove(a);
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
		
		// DRAW ARROWS
		for(Arrow a : arrows) {
			a.draw(g);
		}

	}
}
