package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TutorialScreen implements Screen, InputProcessor{

    private LayrdGame aGame;
    private OrthographicCamera guiCam;
    private SpriteBatch batch;
    private Texture tutorialImg1;
    private TextureRegion tutorial1;
    private Texture tutorialImg2;
    private TextureRegion tutorial2;
    private Texture tutorialImg3;
    private TextureRegion tutorial3;
    private int imgCount = 1;

    public TutorialScreen(LayrdGame game){

        aGame = game;
        guiCam = new OrthographicCamera(480,320);
        guiCam.position.set(480/2,320/2,0);
        batch = new SpriteBatch();

        //  loads the different tutorial pages and sets them
        tutorialImg1 = new Texture(Gdx.files.internal("tutorial1.png"));
        tutorialImg2 = new Texture(Gdx.files.internal("tutorial2.png"));
        tutorialImg3 = new Texture(Gdx.files.internal("tutorial3.png"));

        tutorial1 = new TextureRegion(tutorialImg1, 0, 0, 480, 320);
        tutorial2 = new TextureRegion(tutorialImg2, 0, 0, 480, 320);
        tutorial3 = new TextureRegion(tutorialImg3, 0, 0, 480, 320);

        //  sets the input processor
        Gdx.input.setInputProcessor(this);
    }

    //  function to draw different screens depending on the imgCount index
    private void draw(){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        batch.setProjectionMatrix(guiCam.combined);
        batch.begin();
        if(imgCount == 1)batch.draw(tutorial1, 0, 0, 480, 320);
        else if(imgCount == 2)batch.draw(tutorial2, 0, 0, 480, 320);
        else if(imgCount == 3)batch.draw(tutorial3, 0, 0, 480,320);
        batch.end();


    }

    @Override
    public void render(float delta) {
        draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        tutorial3.getTexture().dispose();
        tutorial2.getTexture().dispose();
        tutorial1.getTexture().dispose();


    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //  if the imgCount is at 3 reset it to 1 and go back to the main menu
        //  else update the tutorial screen img
        if(imgCount == 3){
            imgCount = 1;
            aGame.setScreen(new LayrdScreenMainMenu(aGame));
        }
        else imgCount++;

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
