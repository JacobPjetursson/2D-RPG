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
	// fighting
	private boolean hitPlayer;

	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = { 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 6, 6, 6, 6, 11, 11, 11, 11, 6 };

	int SPELL = 0;
	int THRUST = 1;
	int WALK = 2;
	int ATTACK = 3;
	int BOW = 4;
	int KNOCKUP_SOUTH = 5;

	public Orc(TileMap tm, Player player) {
		super(tm, player);
		fsMachine = new int[][] { { 0, 4, 8, 12, 16, -1 }, { 1, 5, 9, 13, 17, -1 },
			{ 2, 6, 10, 14, 18, 20 }, { 3, 7, 11, 15, 19, -1 } };
		cAction = WALK;
		// TODO - Make randomized direction, or input
		cState = south;

		width = 120;
		height = 120;
		cwidth = 80;
		cheight = 80;
		walkingcWidth = 55;
		walkingcHeight = 40;
		moveSpeed = 1.8;
		health = maxHealth = 5;

		attackRange = 50;
		attackWidth = 20;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO
					.read(getClass().getResourceAsStream("/Sprites_Enemies/orc_nothing.gif"));
			sprites = new ArrayList<BufferedImage[]>();
			int imgsinWidth = 13;
			int imgsinHeight = 21;
			imgHeight = spritesheet.getHeight()/imgsinHeight;
			imgWidth = spritesheet.getWidth()/imgsinWidth;
			for (int i = 0; i < 20; i++) {
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
		animation.setDelay(80);
		/*
		 * JukeBox.load("/SFX/jump.wav", "jump");
		 * JukeBox.load("/SFX/fireball.wav", "fireball");
		 * JukeBox.load("/SFX/scratch.wav", "scratch");
		 */
	}

	private void checkAttack() {
		if (inAttackRange(player) && !attacking) {
			attacking = true;
			getDirectionToCoords(player.getx(), player.gety());
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

			if (player.getWalkingRectangle().intersectsLine(meleeAttackLeft)
					|| player.getWalkingRectangle().intersectsLine(meleeAttackRight)
					|| player.getWalkingRectangle().intersectsLine(meleeAttackTop)) {
				player.hit(1);
			}
		}
	}
	// BELOW IS FUNCTIONS USED IN UPDATE

	// Set the animations according to the current action performed
	private void setAnimation() {
		if (!(sprites.get(fsMachine[pState][pAction]) == sprites.get(fsMachine[cState][cAction]))) {
			animation.setFrames(sprites.get(fsMachine[cState][cAction]));
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
				cAction = ATTACK;
				moving = false;
			} else if (moving) {
				cAction = WALK;
				attacking = false;
			}
		}
	}
	// TODO - change to this method is called here instead of in the pathfinding algorithm!
	public void setWalking(int x, int y) {
		if(!inAttackRange(player) && !attacking) {
		getDirectionToCoords(x,y);
		moveEnemy();
		moving = true;
		}
	}
	public void update() {
		checkAttack();
		setState();
		setAnimation();
		
		// check attack has stopped
		if (animation.hasPlayedOnce()) {
			animation.setFrames(sprites.get(fsMachine[cState][cAction]));
			attacking = false;
			hitPlayer = false;
		}

		if (!idle) {
			animation.update();
		}
	}

	@Override
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}
}
