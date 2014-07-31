package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class LayrdWorld implements ContactListener, GestureListener{


	public enum worldState{READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER, LOADINGMENU}
	public worldState state = worldState.READY;

	private Player player;
	public float score = 0;

	////////////////////////////////////////////////////////
	public OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera camera;
    //private Box2DDebugRenderer b2dr;

	private World world;
	private TiledMap map;

	private Body playerBody;
	private Body endingBody;

	private float mapX;

	private float difficulty;

	private Texture items;
	private TextureRegion gameOver;
	private TextureRegion ready;
    private SpriteBatch batcher;

    boolean isPanning;

	/////////////////////////////////////////////////////////


	public LayrdWorld(String levelName, float difficulty ){

		// initializing difficulty level 
		this.difficulty = difficulty;

		// must initiate component lib first
		gameComponentsInitialize(levelName);

		System.out.println("Components Loaded");
		
		state = worldState.READY;

	}

	private void gameComponentsInitialize(String tileMapDirectory){

		// check out difficulty
		System.out.println("diff:" + difficulty);

		// initialize library
		loadGraphics();

		world = new World(new Vector2(0, 0), true);

		//
		world.setContactListener(this);

		// 
		GestureDetector gestureDect = new GestureDetector(this);
		Gdx.input.setInputProcessor(gestureDect);


		//  loads level, if fail to load desire map, load testMap.tmx
		try{
			map = new TmxMapLoader().load(tileMapDirectory);
			System.out.println("Map Loaded");
		}
		catch(Exception e){
			map = new TmxMapLoader().load("testMap.tmx");
			System.out.println("Map Fail to load, load testMap.tmx");
		}



		// must initialize player before initialize map
		playerInitialize();
		System.out.println("Player initialized");

		mapInitialize();
		System.out.println("Map initialized");

		// initialize camera
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera(480, 320);
//        b2dr = new Box2DDebugRenderer();


		//  makes the camera zoom only the first time
		camera.zoom = 1;
		camera.zoom *= 2.5f;
		camera.update();

		// clear screen
		//Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClearColor(0.74f, 0.76f, 0.78f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	// TODO
	// this load all graphical items this world will need
	// should have load this on separate statics class and only call here when needed
	private void loadGraphics(){
		//  makes the sprite for gameOver Screen
		//  TODO make sprites for READY/ PAUSED screen
		batcher = new SpriteBatch();
		items = LayrdGraphics.getTexture("items");
		gameOver = new TextureRegion(items, 352, 256,160, 96);
        ready = new TextureRegion(items, 320 , 224, 192, 32);

	}

	private void playerInitialize(){
		// initialize player and game objects (if any)

		// TODO replace player hard-coded number with variables (screen size OR pre-defined constants)
		// player = new Player(50, 800 / 2 - 64 / 2, 44, 66);

		//  makes player spawn at bottom of level
		//  without this there was issues with player not able to reach bottom of screen
		player = new Player(0, 100, 44, 66);

		//  makes player jump to middle of screen for starting
		player.setPos(player.position.x, 800 / 2 - 64 / 2);

		// TODO require investigate of multiple objects using the same texture
		// be aware that the texture here is originate in graphics library
		// any direct change to texture MAY OR MAY NOT cause change to all objects using it
		player.sprite.set(new Sprite (LayrdGraphics.getTexture("player")) );

		// TODO replace hard coded numbers to variable/constant if possible
		player.setSize(44, 66);
	}

	private void mapInitialize(){

		//items = new Texture(Gdx.files.internal("items.png"));
		//gameOver = new TextureRegion(items, 352, 256,160, 96);

		mapX = 0;
		// gets layer that the obstacles are in
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("obstacles");
		TiledMapTileLayer finish = (TiledMapTileLayer) map.getLayers().get("end");
		float tileSize = layer.getTileWidth();


		// creates the bodies and fixtures
		BodyDef bdefPlayer = new BodyDef();
		FixtureDef fdefPlayer = new FixtureDef();
		BodyDef bdefEndRegion = new BodyDef();
		FixtureDef fdefEndRegion = new FixtureDef();
		PolygonShape squarePlayer = new PolygonShape();
		PolygonShape squareEndRegion = new PolygonShape();


		// setup player
		bdefPlayer.position.set(player.position.x + player.rectBounds.width ,
				player.position.y );
		bdefPlayer.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdefPlayer);

		// making the box for player
		squarePlayer.setAsBox(player.rectBounds.width / 2,
				player.rectBounds.height / 2,
				new Vector2(0, 66), 0);

		fdefPlayer.shape = squarePlayer;
		fdefPlayer.filter.categoryBits = 4;
		fdefPlayer.filter.maskBits = 2;
		playerBody.createFixture(fdefPlayer).setUserData("player");



		//  separate fixture around player to make interactions with ending panel


		// setup End Region
		bdefEndRegion.position.set(player.position.x + player.rectBounds.width,
				player.position.y);
		bdefEndRegion.type = BodyType.DynamicBody;
		endingBody = world.createBody(bdefEndRegion);

		squareEndRegion.setAsBox(player.rectBounds.width / 2,
				player.rectBounds.height / 2,
				new Vector2(0, 66), 0);

		fdefEndRegion.shape = squareEndRegion;
		fdefEndRegion.filter.categoryBits = 8;
		fdefEndRegion.filter.maskBits = 6;
		endingBody.createFixture(fdefEndRegion).setUserData("endRegion");


		//	goes through map and makes the boxes to deal with collisions
		for(int row = 0; row < layer.getHeight(); row++){
			for(int col = 0; col < layer.getWidth(); col++){
				Cell cell = layer.getCell(col, row);

				if(cell == null)continue;
				if(cell.getTile() == null)continue;

				bdefPlayer.type = BodyType.StaticBody;
				bdefPlayer.position.set(
						(col + 0.5f) * tileSize,
						(row + 0.5f) * tileSize
						);

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2, -tileSize / 2);
				v[1] = new Vector2(-tileSize / 2, tileSize /2);
				v[2] = new Vector2(tileSize / 2, tileSize / 2);
				v[3] = new Vector2(tileSize /2, -tileSize /2);
				v[4] = new Vector2(-tileSize /2, -tileSize /2);
				cs.createChain(v);

				fdefPlayer.shape = cs;
				fdefPlayer.filter.categoryBits = 2;
				fdefPlayer.filter.maskBits = 4;

				world.createBody(bdefPlayer).createFixture(fdefPlayer).setUserData("obstacle");


			}
		}

		//  makes the boxes around the finishing portion
		for(int row = 0; row < finish.getHeight(); row++){
			for(int col = 0; col < finish.getWidth(); col++){
				Cell cell = finish.getCell(col, row);

				if(cell == null)continue;
				if(cell.getTile() == null)continue;

				bdefEndRegion.type = BodyType.StaticBody;
				bdefEndRegion.position.set(
						(col + 0.5f) * tileSize,
						(row + 0.5f) * tileSize
						);

				ChainShape cs2 = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2, -tileSize / 2);
				v[1] = new Vector2(-tileSize / 2, tileSize /2);
				v[2] = new Vector2(tileSize / 2, tileSize / 2);
				v[3] = new Vector2(tileSize /2, -tileSize /2);
				v[4] = new Vector2(-tileSize /2, -tileSize /2);
				cs2.createChain(v);

				fdefEndRegion.shape = cs2;
				fdefEndRegion.filter.categoryBits = 6;
				fdefEndRegion.filter.maskBits = 8;

				world.createBody(bdefEndRegion).createFixture(fdefEndRegion).setUserData("finishWall");

			}
		}

		squarePlayer.dispose();
		squareEndRegion.dispose();

	}

	public void stateReady(float delta) {
        Gdx.gl.glClearColor(0.74f, 0.76f, 0.78f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        batcher.draw(ready,
                (float) (Gdx.graphics.getWidth() / 2) - ready.getRegionWidth(),
                Gdx.graphics.getHeight() / 2 - ready.getRegionHeight() * 2,
                250, 200);
        batcher.end();

		//System.out.println("System Paused");
		//camera.update();
		//renderer.render();
		/*if(Gdx.input.isTouched()){
			state = worldState.PLAYING;
		}*/
	}

	public void statePlaying(float delta){

		camera.update();

		// update
		//world.step(1/60f, 6, 2);
		world.step(delta, 6, 2);

		collisionDetection();


        // clean screen
        Gdx.gl.glClearColor(0.74f, 0.76f, 0.78f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // update map offset
        //  increases as the difficulty increases
        if(difficulty == 1){
            mapX += delta * 60 * difficulty * 3;
        }
        else{
            mapX += delta * 60 * difficulty * 2;
        }


        // update camera and player position
        camera.position.set(mapX, player.position.y + player.rectBounds.height, 0);
        if (difficulty == 1){
            player.setPos(player.position.x + delta * 60 * difficulty * 3, player.position.y);

        }
        else{
            player.setPos(player.position.x + delta * 60 * difficulty * 2, player.position.y);

        }

		playerBody.setTransform(player.position.x + player.rectBounds.height / 3,
				player.position.y - player.rectBounds.width / 1.375f, 0);
		endingBody.setTransform(player.position.x + player.rectBounds.height / 3,
				player.position.y - player.rectBounds.width / 1.375f, 0);


		camera.update();

		renderer.setView(camera);

		renderer.render();
//		b2dr.render(world, camera.combined);

		renderer.getSpriteBatch().begin();

		player.sprite.draw(renderer.getSpriteBatch());

		renderer.getSpriteBatch().end();
	}

	public void statePaused(){
        Gdx.gl.glClearColor(0.74f, 0.76f, 0.78f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        /*
        if (Gdx.input.isTouched()) {
			state = worldState.PLAYING;
		}*/
	}

	public void stateGameover(float delta){

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batcher.begin();
		batcher.draw(gameOver,
				(float) (Gdx.graphics.getWidth() / 2) - gameOver.getRegionWidth() * 1.5f,
				Gdx.graphics.getHeight() / 2 - gameOver.getRegionHeight() * 2,
				500, 500);
		batcher.end();
		map.dispose();

		world.destroyBody(playerBody);
		world.destroyBody(endingBody);
	}

	public void stateFinishLevel(float delta){

        Gdx.gl.glClearColor(0.74f, 0.76f, 0.78f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// update score


	}

	public boolean updatePlayer(float deltaX, float deltaY, float x, float y){
		float tempY;

		//player.oldPosition.y = player.position.y;
		//going down
/*
		if(deltaY > 0){
			tempY = player.position.y;
			tempY += deltaY;
			player.setPos(player.position.x, tempY);
		}
		//going up
		else if(deltaY < 0){
			tempY = player.position.y;
			tempY += deltaY;
			player.setPos(player.position.x, tempY);
		}
*/
        if( LayrdPhysics.pointInRectangle(player.rectBounds, x, y) ) {
            player.setPos(player.position.x + deltaX, player.position.y);
        }else{
            player.setPos(player.position.x, player.position.y - deltaY);
        }


		return true;
	}

	private void collisionDetection(){
		if(world.getContactCount() > 0){

			for (Contact contact : world.getContactList()) {
				Fixture fixtureA = contact.getFixtureA();
				Fixture fixtureB = contact.getFixtureB();

				if(fixtureA.getUserData() == "obstacle" && fixtureB.getUserData() == "player")
					state = worldState.GAMEOVER;
				else if (fixtureA.getUserData() == "finishWall" && fixtureB.getUserData() == "endRegion")
					state = worldState.LEVELFINISH;
			}
		}
	}

	public void dispose(){
		map.dispose();
		renderer.dispose();
//		b2dr.dispose();
		world.dispose();
		batcher.dispose();


	}


	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		//System.out.println("contact detected");
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		//System.out.println("contact ended");
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub

        isPanning = true;
		if(state == worldState.PLAYING)
			return updatePlayer(deltaX, deltaY, x, y);

		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
        isPanning = false;
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
