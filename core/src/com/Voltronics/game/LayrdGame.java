package com.Voltronics.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

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
		//Assets.load();
		
		loadAssets();
		
		setScreen(new LayrdScreenMainMenu(this));
		//setScreen(new LayrdScreenGame(this));
	}
	
	private void loadAssets(){
		// load the game graphics
		
		LayrdGraphics.loadTexture("background", "background.png");
		
		LayrdGraphics.loadTexture("tutorialImg1", "tutorial1.png");
		LayrdGraphics.loadTexture("tutorialImg2", "tutorial2.png");
		LayrdGraphics.loadTexture("tutorialImg3", "tutorial3.png");
		
		LayrdGraphics.loadTexture("player", "ship.png");
		LayrdGraphics.loadTexture("items", "items.png");
		
		// load the game audio
		
		
	}
	
	
	@Override
	public void dispose() {

	}
}
