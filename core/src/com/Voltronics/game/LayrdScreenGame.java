package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class LayrdScreenGame implements Screen{

	private LayrdGame game;
	private LayrdWorld world;

	private float difficulty = 1;
	private String levelName = "level";
	private String tileMapExtension = ".tmx";

	private int gameScore;
	private float playTime = 0;
	private int loseCount = 0;

    private String yourScoreName;
    BitmapFont yourBitmapFontName;

    private SpriteBatch batch;

	

	public LayrdScreenGame(LayrdGame game){
		this.game = game;

	}


	public void gameStateMachine(float delta){

		switch(world.state){
		case READY:
			// TODO
			// this state display rather sketchy
			world.stateReady(delta);
            if(Gdx.input.isTouched()){
                //world.state = LayrdRandomWorld.worldState.PLAYING;
                world.state = LayrdWorld.worldState.PLAYING;
            }
			break;
		
		case PLAYING:
			// keep update the game
			world.statePlaying(delta);
			
			// for now game score are measure by time when user playing the game
			// without losing of course
            //  added times the difficulty so that the score will go up faster when farther in level

            gameScore += (int) (1 * difficulty );

            yourScoreName = "Score: " + gameScore;
            playTime += delta;
			
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
			
			// we win, let reset lose counter
			loseCount = 0;
			
			world = new LayrdWorld(levelName + (int)difficulty + tileMapExtension, difficulty);
			break;

		case GAMEOVER:
			// game over, wait for touch to exit
			world.stateGameover(delta);
            if (!world.isPanning){
                if (Gdx.input.isTouched()) {
                    // TODO
                    // update game score to leader board here



                    // reset score, increment loseCount
                    loseCount++;

                    if(loseCount > LayrdLogic.LOSE_COUNT_WARNING){
                        // TODO
                        // display lose count warning here, suggest user to take a rest before continues
                    }
                    else if(playTime > LayrdLogic.PLAY_TIME_WARNING){
                        // TODO
                        // display play time warning here, suggest user to take a rest before continues

                    }



                    // TODO
                    // may want to add play again in conjunction with exit to menu
                    game.googleGameInterface.submiteScoreGPGS(gameScore);
                    gameScore = 0;
                    game.setScreen(new LayrdScreenMainMenu(game));
                }
            }

			
			break;
		default:
			break;
		}
	}


	@Override
	public void render(float delta) {
		
		gameStateMachine(delta);

        batch.begin();
        yourBitmapFontName.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        yourBitmapFontName.draw(batch, yourScoreName, 50, 300);
        batch.end();


	}

	@Override
	public void resize(int width, int height) {
		world.camera.viewportWidth = width / 2.5f;
		world.camera.viewportHeight = height / 2.5f;
	}

	@Override
	public void show() {
		
		world = new LayrdWorld(levelName + (int)difficulty + tileMapExtension, difficulty);

        batch = new SpriteBatch();
        gameScore = 0;
        yourScoreName = "score: 0";
        yourBitmapFontName = new BitmapFont();
        yourBitmapFontName.setScale(3);

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
