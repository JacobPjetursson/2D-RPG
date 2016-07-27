package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import handlers.MouseEvents;
import main.GamePanel;
import tileMap.Background;
// TODO - Fix the mess with the rectangle bounds on the strings

public class MenuState extends GameState {
	private int sWidth = GamePanel.WIDTH;
	private int sHeight = GamePanel.HEIGHT;
	private String[] options = {"Start Game", "Settings", "Quit"};
	
	private Rectangle Start = new Rectangle(0,0,0,0);
	private Rectangle Settings = new Rectangle(0,0,0,0);
	private Rectangle Quit = new Rectangle(0,0,0,0);
	private Rectangle[] rectangles = {Start, Settings, Quit};
	
	private Background bg;
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	@Override
	public void update() {
		bg.update();
		handleInput();
	}
	public void init() {
		try {
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1,  0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 90);
			font = new Font("Arial", Font.PLAIN, 50);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.setFont(font);
		drawCenteredString(g, "The Lands of Mystery", sWidth, sHeight/7 , titleFont);
		
		for(int i = 0; i < rectangles.length; i++) {
			if(MouseEvents.mouseHovered(rectangles[i])) {
				g.setColor(Color.BLACK);
			}
			else {
				g.setColor(Color.RED);
			}
			rectangles[i] = drawCenteredStringWithBounds(g, options[i], sWidth, sHeight/3+i*150, font);
		}
	}
	
	private void drawCenteredString(Graphics g, String text, int width, int height, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = (width - metrics.stringWidth(text)) / 2;
	    int y = height;
	    g.setFont(font);
	    g.drawString(text, x, y);
	}
	private Rectangle drawCenteredStringWithBounds(Graphics g, String text, int width, int height, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
	    int x = (width - metrics.stringWidth(text)) / 2;
	    int y = height;
	    g.setFont(font);
	    g.drawString(text, x, y);
	   return new Rectangle(x, y-font.getSize()+10, metrics.stringWidth(text), font.getSize());
	}
	
	public void handleInput() {
		for(int i = 0; i < rectangles.length; i++) {
			if(MouseEvents.mouseHovered(rectangles[i])) {
				if(MouseEvents.isPressed(MouseEvents.LEFTCLICK)) {
					if(i == 0) {
						gsm.setState(GameStateManager.WORLD1STATE);
						
					}
					if(i == 1) {
						// settings
					}
					if(i == 2) {
						System.exit(0);
					}
				}
			}
		}
	}
}
