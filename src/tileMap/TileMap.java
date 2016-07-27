package tileMap;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import entity.enemies.Enemy;
import main.GamePanel;

import java.io.*;
// TODO - Find a way to integrate offset, xmin,xmax, etc. into getMapOnScreen();
// TODO - AStarPathing in bottom of map doesn't work at all
public class TileMap {
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	// AStarPathing
	int[][] mapOnScreen;
	
	public TileMap(String tileSet, String mapName) {
		tileSize = 60;
		loadMap(mapName);
		loadTiles(tileSet);
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		mapOnScreen = new int[numRowsToDraw*2][numColsToDraw*2];

	}
	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(col* tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage,Tile.NORMAL);
				subimage = tileset.getSubimage(col*tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.NORMAL);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		
		try {
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	public int getTileSize() {return tileSize;}
	public double getx() {return x;}
	public double gety() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	public int[][] getMap() { return map; }
	public int getNumRowsToDraw() { return numRowsToDraw; }
	public int getNumColsToDraw() { return numColsToDraw; }
	
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	public void setPosition(double x, double y) {
		this.x+=(x-this.x) ;
		this.y+=(y-this.y) ;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	public void draw(Graphics2D g) {
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if (row>=numRows) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				if(col>=numCols) break;
				
				if(map[row][col] == 0) continue;
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
			}
		}
	}
	public int[][] getMapOnScreen() {
		for(int i=0; i < mapOnScreen.length; i++) {
			for(int j = 0; j < mapOnScreen[i].length; j++) {
				mapOnScreen[i][j] = map[(int) (i-y/(tileSize/2))][(int) (j-(x/(tileSize/2)))];
			}
		}
		return mapOnScreen;
	}
}
