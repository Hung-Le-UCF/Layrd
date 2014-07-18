package com.Voltronics.game;

import com.badlogic.gdx.Game;

public class LayrdGame extends Game {
	
	private enum LayrdGameState{PLAYING, PAUSE};
	private LayrdGameState gameState = LayrdGameState.PAUSE;
	
	private LayrdScreenMainMenu mainMenu;

	@Override
	public void create() {

		

		
		setScreen(new LayrdScreenMainMenu());
		
	}
	
	public void playing(){
		
	}
	
	
	
	@Override
	public void dispose() {
		mainMenu.dispose();
	}
}
