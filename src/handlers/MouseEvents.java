package handlers;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class MouseEvents {
	public static final int NUM_BUTTONS = 2;

	public static boolean buttons[] = new boolean[NUM_BUTTONS];
	public static boolean prevButtons[] = new boolean[NUM_BUTTONS];
	public static int mouseX;
	public static int mouseY;

	public static int LEFTCLICK = 0;
	public static int RIGHTCLICK = 1;

	public static void buttonSet(int i, boolean b) {
		if (i == MouseEvent.BUTTON1)
			buttons[LEFTCLICK] = b;
		else if (i == MouseEvent.BUTTON2)
			buttons[RIGHTCLICK] = b;
	}

	public static void update() {
		for (int i = 0; i < NUM_BUTTONS; i++) {
			prevButtons[i] = buttons[i];
		}
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public static boolean mouseHovered(Rectangle r) {
		return r.contains(mouseX,mouseY);
	}
	public static boolean isPressed(int i) {
		return buttons[i];
	}
}
