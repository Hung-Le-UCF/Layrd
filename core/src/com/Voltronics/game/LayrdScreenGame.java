package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class LayrdScreenGame implements Screen{

	private enum gameState{ READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER}
	gameState state;
	
	LayrdGame game;
	LayrdWorld world;
	
	SpriteBatch batch;
	
	
	
	public LayrdScreenGame(LayrdGame game){
		this.game = game;
		
	}
	
	
	
	
	
	
	
	
	@Override
	public void render(float delta) {
		
		// update game world
		// world.stateMachine(Gdx.graphics.getDeltaTime());
		world.stateMachine(delta);
		
		
	}

	@Override
	public void resize(int width, int height) {
		world.camera.viewportWidth = width / 2.5f;
		world.camera.viewportHeight = height / 2.5f;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		state = gameState.READY;
		
		//batch = new SpriteBatch();
		world = new LayrdWorld("testMap.tmx");
		

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {

        world.dispose();
	}

}
