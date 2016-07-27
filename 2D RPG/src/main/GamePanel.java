package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import gameState.GameStateManager;
import handlers.Keys;
import handlers.MouseEvents;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	// dimensions
	private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int WIDTH = gd.getDisplayMode().getWidth();
	public static final int HEIGHT = gd.getDisplayMode().getHeight();
	
	//public static final int WIDTH = 320;
	//public static final int HEIGHT = 240;
	//public static final int SCALE = 4;
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// imgage
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		
	}
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			thread.start();
		}
	}
	
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
	}
	public void run() {
		init();
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while (running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void update() {
		gsm.update();
		Keys.update();
	}
	
	private void draw() {
		gsm.draw(g);
	}
	
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image,0,0,WIDTH, HEIGHT, null);
		g2.dispose();
	}
	
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		MouseEvents.buttonSet(e.getButton(), true);
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		MouseEvents.buttonSet(e.getButton(), false);
		
	}
	public void mouseDragged(MouseEvent e) {
		MouseEvents.mouseX = e.getX();
		MouseEvents.mouseY = e.getY();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		MouseEvents.mouseX = e.getX();
		MouseEvents.mouseY = e.getY();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	// UNUSED BELOW HERE
	public void keyTyped(KeyEvent key) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
}
