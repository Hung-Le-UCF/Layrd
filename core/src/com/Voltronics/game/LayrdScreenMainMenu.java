package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class LayrdScreenMainMenu implements Screen {
	
	LayrdGame game;
	
	private SpriteBatch batch;
	private Texture img;
	private OrthographicCamera guiCam;
	private ShapeRenderer sRender;
	private Vector3 touchPoint;
	private Rectangle playBounds;
	private Rectangle achievementBounds;
	private Rectangle highScoreBounds;
    private Texture backGroundImg;
    private TextureRegion backGround;

    public LayrdScreenMainMenu(LayrdGame aGame)
	{
		System.out.println("mainmenu Loaded");
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

			if (LayrdPhysics.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) 
			{
				game.setScreen(new LayrdScreenGame(game));
				return;
			}
			else if (LayrdPhysics.pointInRectangle(highScoreBounds, touchPoint.x, touchPoint.y))
			{
				game.googleGameInterface.getLeaderboardGPGS();
				return;
			}
			else if (LayrdPhysics.pointInRectangle(achievementBounds, touchPoint.x, touchPoint.y))
			{
				game.googleGameInterface.getAchievementsGPGS();
				return;
			}
		}
	}
	
	@Override
	public void render(float delta) {
		
		update(delta);
		draw(delta);

	}

    public void draw(float delta){
        backGroundImg = new Texture(Gdx.files.internal("background.png"));
        backGround = new TextureRegion(backGroundImg, 0, 0, 480, 320);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        batch.setProjectionMatrix(guiCam.combined);
        sRender.setProjectionMatrix(guiCam.combined);

        batch.disableBlending();
        batch.begin();
        batch.draw(backGround, 0, 0, 480, 320);
        batch.end();

        batch.enableBlending();
        batch.begin();
        //batch.draw(img, 0, 0);
        
        TextureRegion mainMenu = new TextureRegion(LayrdGraphics.getTexture("items"), 0, 224, 300, 110);
        batch.draw(mainMenu, 80, 100);
        
        
        batch.end();
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
