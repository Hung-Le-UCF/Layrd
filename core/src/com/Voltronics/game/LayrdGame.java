package com.Voltronics.game;

import com.badlogic.gdx.Game;

public class LayrdGame extends Game {
	
	LayrdGoogleGameInterface googleGameInterface;
	
	private enum LayrdGameState{PLAYING, PAUSE};
	private LayrdGameState gameState = LayrdGameState.PAUSE;
	
	private LayrdScreenMainMenu mainMenu;

	public LayrdGame(LayrdGoogleGameInterface googleInterface)
	{
		googleGameInterface = googleInterface;
	}
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
