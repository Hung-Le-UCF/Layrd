package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LayrdScreenGame implements Screen{

	//private enum gameState{ READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER}
	//private gameState state;

	private LayrdGame game;
	private LayrdWorld world;

	private SpriteBatch batch;

	private float difficulty = 1;
	private String levelName = "level";
	private String tileMapExtension = ".tmx";

	private float gameScore = 0;



	public LayrdScreenGame(LayrdGame game){
		this.game = game;

	}


	public void gameStateMachine(float delta){

		switch(world.state){
		case PLAYING:
			// keep update the game
			world.statePlaying(delta);
			break;
			
		case PAUSED:
			// no update while paused
			world.statePaused();
			break;
		
		case LEVELFINISH:
			// the level finished, update score
			// increase difficulty level, disposed old world, and load new world
			world.stateFinishLevel(delta);
			difficulty++;
			world.dispose();
			
			world = new LayrdWorld(levelName + (int)difficulty + tileMapExtension, difficulty);
			break;

		case GAMEOVER:
			// game over, wait for touch to exit
			world.stateGameover(delta);
			if (Gdx.input.isTouched()) {
				game.setScreen(new LayrdScreenMainMenu(game));
			}
			break;
		default:
			break;
		}
	}


	@Override
	public void render(float delta) {		
		gameStateMachine(delta);
	}

	@Override
	public void resize(int width, int height) {
		world.camera.viewportWidth = width / 2.5f;
		world.camera.viewportHeight = height / 2.5f;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		world = new LayrdWorld(levelName + (int)difficulty + tileMapExtension, difficulty);
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
