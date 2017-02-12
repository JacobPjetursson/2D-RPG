package gameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import pathfindingAlgorithm.Path;
import entity.Entity;
import entity.Player;
import entity.enemies.Enemy;
import entity.enemies.Orc;
import main.GamePanel;
import tileMap.TileMap;
import pathfindingAlgorithm.AStarPathFinder;


public class World1State extends GameState {
	private TileMap tileMap;
	private ArrayList<Enemy> enemies;
	private Player player;
	private AStarPathFinder finder;
	private Path path;
	//private HUD hud;
	
	public World1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
		
	}

	@Override
	public void init() {
		
		tileMap = new TileMap("/Tilesets/grasstileset.gif","/Maps/World1.lua");
		tileMap.setPosition(0, 0);
		
		// spawn player
		player = new Player(tileMap);
		player.setPosition(100,100);
		
		// spawn enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		/* Adding the hud lel kek PogChamp LUL
		hud = new HUD(player);
		
		JukeBox.load("/Music/MainLevel.wav", "mainlevel");
		JukeBox.loop("mainlevel");
		*/
	}
	private void populateEnemies() {
		Orc o;
		Point[] points = new Point[] {
				new Point(500,500),
				new Point(600, 500),
				new Point(500, 300),
				new Point(500,700),
				new Point(1000,1000),
			};
		for(int i = 0; i < points.length; i++) {
			o = new Orc(tileMap, player);
			o.setPosition(points[i].x,points[i].y);
			enemies.add(o);
		}
	}
	private void findPathfromEnemiesToPlayer() {
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			if(!e.notOnScreen()) {
				finder = new AStarPathFinder(tileMap, 5000);
				int destX = (int) ((player.getx() + tileMap.getx()) / (tileMap.getTileSize()/2));
				int destY = (int) ((player.gety() + tileMap.gety()) / (tileMap.getTileSize()/2));
				path  = finder.findPath(e,destX,destY);
				if(path != null) {
					e.setWalking((int) (path.getX(1)*(tileMap.getTileSize()/2)-tileMap.getx()),
							(int)(path.getY(1)*(tileMap.getTileSize()/2)-tileMap.gety()));	
				}
			}
			
		}
	}
	@Override
	public void update() {
		// update player
		player.update();
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
		// Find path from all enemies to player
		findPathfromEnemiesToPlayer();
		// attack enemies
		player.checkAttackAndCollision(enemies);
		
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Entity e = enemies.get(i);
			if(e.isDead()) {
				enemies.remove(i);
				i--;
			}
			e.update();
		}
		
	}

	@Override
	public void draw(Graphics2D g) {
		// draw tilemap
		tileMap.draw(g);
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		// draw player
		player.draw(g);
		// create depth compared to player (draw certain enemies twice)
		for(int i = 0; i < enemies.size(); i++) {
			if(player.intersects(enemies.get(i)) && enemies.get(i).gety() > player.gety()){
				enemies.get(i).draw(g);
			}	
		}
	
		// draw hud
		//hud.draw(g);
		
	}

}
