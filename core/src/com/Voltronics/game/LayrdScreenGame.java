package com.Voltronics.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LayrdScreenGame implements Screen{

	private enum gameState{ READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER}
	gameState state;
	
	LayrdGame game;
	LayrdWorld world;
	
	SpriteBatch batch;
	
	public LayrdScreenGame(LayrdGame game){
		this.game = game;
		state = gameState.READY;
		
		batch = new SpriteBatch();
		//world = new LayrdWorld();
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
