package com.Voltronics.game;

import com.badlogic.gdx.Game;

public class LayrdGame extends Game {
	private LayrdScreenMainMenu mainMenu;
	private LayrdEventBus eventBus;
	
	private LayrdComponent input;
	private LayrdComponent physics;
	private LayrdComponent logic;
	private LayrdComponent graphics;
	private LayrdComponent sound;
	
	@Override
	public void create() {
		eventBus = new LayrdEventBus();
		mainMenu = new LayrdScreenMainMenu();
		
		input = new LayrdInput();
		physics = new LayrdPhysics();
		logic = new LayrdLogic();
		graphics = new LayrdGraphics();
		sound = new LayrdSound();
		
		eventBus.subscribe(logic, LayrdEventType.INPUT);
		eventBus.subscribe(logic, LayrdEventType.COLLISION);
		eventBus.subscribe(physics, LayrdEventType.ENTITY_CREATE);
		eventBus.subscribe(physics, LayrdEventType.ENTITY_DESTROY);
		eventBus.subscribe(sound, LayrdEventType.SOUND);
		
		setScreen(mainMenu);
	}
	
	@Override
	public void dispose() {
		eventBus.dispose();
		mainMenu.dispose();
		
		input.dispose();
		physics.dispose();
		logic.dispose();
		graphics.dispose();
		sound.dispose();
	}
}
