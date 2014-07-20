package com.Voltronics.game;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class LayrdWorld {

	public enum worldState{READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER}
	public worldState state = worldState.READY;

	private Player player;
	private TiledMapTileLayer collisionLayer;

	private GestureDetector gestureDect;

	private LayrdGraphics graphicsLib;
	private LayrdSound soundsLib;
	private LayrdLogic gameLogic;

	public LayrdWorld(TiledMapTileLayer collisionLayer){

		playerInitialize();

		// initialize map
		this.collisionLayer = collisionLayer;



	}

	private void playerInitialize(){	
		// initialize player and game objects (if any)
		player = new Player();

		player.sprite.setSize(50, 128);
	}

	private void gameComponentsInitialize(){
		// initialize component
		graphicsLib = new LayrdGraphics();
		soundsLib = new LayrdSound();
		gameLogic = new LayrdLogic();

	}


	// this method check for the state of the game and update if necessary
	public void stateMachine(float delta){

		switch(state){
		case READY:
			break;
		case PLAYING:
			break;
		case PAUSED:
			break;
		case LEVELFINISH:
			break;
		case GAMEOVER:
			break;
		default:
			break;
		}
	}
	
	public void statePlaying(float delta){
		
		// update tilemap
		
		// update 
		
		
	}
	




	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}
}
