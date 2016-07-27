package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class HUD {

	private Player player;
	private Font font;
	public HUD(Player p) {
		player = p;
		font = new Font("Arial", Font.PLAIN, 10);
	}
	public void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(Color.RED);
		g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
	}
	// LUL
}
