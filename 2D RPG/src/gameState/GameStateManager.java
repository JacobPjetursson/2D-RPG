package gameState;

//import Audio.JukeBox;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 2;
	public static final int MENUSTATE = 0;
	public static final int WORLD1STATE = 1;
	
	public GameStateManager() {
		//JukeBox.init();
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE) {
			gameStates[state] = new MenuState(this);
		}
		if(state == WORLD1STATE) {
			gameStates[state] = new World1State(this);
		}
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		gameStates[currentState].init();
	}
	public void update() {
		if(gameStates[currentState] != null) {
			gameStates[currentState].update();
		}
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(gameStates[currentState] != null) {
			gameStates[currentState].draw(g);	
		}
	}
}
