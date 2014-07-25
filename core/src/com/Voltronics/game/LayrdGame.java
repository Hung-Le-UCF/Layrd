package com.Voltronics.game;

import com.badlogic.gdx.Game;

public class LayrdGame extends Game {
	
	private enum LayrdGameState{PLAYING, PAUSE};
	private LayrdGameState gameState = LayrdGameState.PAUSE;
	
	private LayrdScreenMainMenu mainMenu;

	@Override
	public void create() {

		System.out.println("Game Create");
		Assets.load();
		
		setScreen(new LayrdScreenMainMenu(this));
		//setScreen(new LayrdScreenGame(this));
	}
	
	public void playing(){
		
	}
	
	
	
	@Override
	public void dispose() {

	}
}
