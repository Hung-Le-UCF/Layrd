package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class LayrdScreenMainMenu implements Screen {
	
	LayrdGame game;
	
	SpriteBatch batch;
	Texture img;
	OrthographicCamera guiCam;
	ShapeRenderer sRender;
	Vector3 touchPoint;
	Rectangle playBounds;
	Rectangle achievementBounds;
	Rectangle highScoreBounds;
	
	public LayrdScreenMainMenu(LayrdGame aGame)
	{
		game = aGame;
		guiCam = new OrthographicCamera(480,320);
		//guiCam.setToOrtho(true);
		guiCam.position.set(480/2,320/2,0);
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		sRender = new ShapeRenderer();
		touchPoint = new Vector3();
		
		playBounds = new Rectangle(110, 170, 240, 40);
		highScoreBounds = new Rectangle(110,130, 240, 40);
		achievementBounds = new Rectangle(110, 90, 240, 40);
		
	}
	
	public void handleInput()
	{
		System.out.println(Gdx.input.getX() + "----" + Gdx.input.getY());
	}
	
	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) 
			{
				game.setScreen(new LayrdScreenGame(game));
				return;
			}
			else if (OverlapTester.pointInRectangle(highScoreBounds, touchPoint.x, touchPoint.y))
			{
				//game.gameInterface.getLeaderboardGPGS();
				return;
			}
			else if (OverlapTester.pointInRectangle(achievementBounds, touchPoint.x, touchPoint.y))
			{
				//game.gameInterface.getAchievementsGPGS();
				return;
			}
		}
	}
	
	@Override
	public void render(float delta) {
		
		update(delta);
		
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batch.setProjectionMatrix(guiCam.combined);
		sRender.setProjectionMatrix(guiCam.combined);
		

		batch.begin();
		//batch.draw(img, 0, 0);
		batch.draw(Assets.mainMenu, 80, 100);
		batch.end();
		
		 sRender.begin(ShapeType.Filled);
		 sRender.setColor(0, 1, 0, 1);
		 sRender.rect(110,130, 240, 40);
		 sRender.end();
		
	
		
		
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
