package main;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Game {

	public static void main(String[] args) {
		JFrame window = new JFrame("The Lands of Mystery");
		window.add(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		Image image = new ImageIcon(window.getClass().getResource("/icons/cursor.gif")).getImage();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor c = toolkit.createCustomCursor(image , new Point(window.getX(), window.getY()), "img");
		window.setCursor (c);
		
		window.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		window.setUndecorated(true);
		
		
		window.setVisible(true);
	}
}
