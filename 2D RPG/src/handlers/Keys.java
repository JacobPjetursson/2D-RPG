package handlers;
import java.awt.event.KeyEvent;

//this class contains a boolean array of current and previous key states
//for the 9 keys that are used for this game.
//a key k is down when keyState[k] is true.
public class Keys {
public static final int NUM_KEYS = 11;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
	
	public static int Spell_1 = 0;
	public static int Spell_2 = 1;
	public static int Spell_3 = 2;
	public static int Spell_4 = 3;
	public static int SPACE = 4;
	public static int TALENTS = 5;
	public static int CHARPAGE = 6;
	public static int BAGPACK = 7;
	public static int ESCAPE = 8;
	public static int SHIFT = 9;
	public static int KEYPRESSED = 10;
	
	
	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_Q) keyState[Spell_1] = b;
		else if(i == KeyEvent.VK_W) keyState[Spell_2] = b;
		else if(i == KeyEvent.VK_E) keyState[Spell_3] = b;
		else if(i == KeyEvent.VK_R) keyState[Spell_4] = b;
		else if(i == KeyEvent.VK_SPACE) keyState[SPACE] = b;
		else if(i == KeyEvent.VK_K) keyState[TALENTS] = b;
		else if(i == KeyEvent.VK_C) keyState[CHARPAGE] = b;
		else if(i == KeyEvent.VK_B) keyState[BAGPACK] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESCAPE] = b;
		else if(i == KeyEvent.VK_SHIFT) keyState[SHIFT] = b;
		else if(i == KeyEvent.KEY_PRESSED) keyState[KEYPRESSED] = b;
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
		
	}
	
}
