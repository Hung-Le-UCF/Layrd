package com.Voltronics.game;

import com.badlogic.gdx.Game;

public class LayrdGame extends Game {
	private LayrdScreenMainMenu mainMenu;
	
	@Override
	public void create() {
		mainMenu = new LayrdScreenMainMenu();
		
		setScreen(mainMenu);
	}
}
