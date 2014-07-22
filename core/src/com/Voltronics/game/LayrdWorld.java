package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class LayrdWorld implements GestureListener{

	public enum worldState{READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER}
	public worldState state = worldState.READY;

	private Player player;

	////////////////////////////////////////////////////////
	public OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera camera;
	private Box2DDebugRenderer b2dr;

	private World world;
	private TiledMap map;
	private TiledMapTileLayer collisionLayer;

	private ListenerClass listener;
	private Body playerBody;

	private float tileSize;
	private float mapX;
	/////////////////////////////////////////////////////////


	private GestureDetector gestureDect;

	private LayrdGraphics graphicsLib;
	private LayrdSound soundsLib;
	private LayrdLogic gameLogic;

	public LayrdWorld(String tileMapDirectory){

		// must initiate component lib first
		gameComponentsInitialize(tileMapDirectory);








	}

	private void playerInitialize(){	
		// initialize player and game objects (if any)

		// TODO replace player hard-coded number with variables (screen size OR pre-defined constants)
		player = new Player(50, 800 / 2 - 64 / 2, 50, 128);

		// TODO require investigate of multiple objects using the same texture
		// be aware that the texture here is originate in graphics library
		// any direct change to texture MAY OR MAY NOT cause change to all objects using it
		player.sprite.set(new Sprite (graphicsLib.getSpriteTexture("player")) );


		System.out.println("Player Loaded Sucessful");

		player.sprite.setSize(50, 128);
	}

	private void gameComponentsInitialize(String tileMapDirectory){
		// initialize library
		graphicsLib = new LayrdGraphics();
		soundsLib = new LayrdSound();
		gameLogic = new LayrdLogic();

		world = new World(new Vector2(0, 0), true);
		b2dr = new Box2DDebugRenderer();
		listener = new ListenerClass();

		world.setContactListener(listener);
		

		// TODO LayrdInput is not completed
		//gestureDect = new GestureDetector(new LayrdInput());
		gestureDect = new GestureDetector(this);
		Gdx.input.setInputProcessor(gestureDect);


		map = new TmxMapLoader().load(tileMapDirectory);
		if(map == null){
			map = new TmxMapLoader().load("testMap.tmx");
		}
		//TODO 
		graphicsLib.loadSprite("player", "sprite.png");
		playerInitialize();

		mapInitialize();


		// initialize camera
		renderer = new OrthogonalTiledMapRenderer(map);
		camera = new OrthographicCamera();


		// initialize map
		collisionLayer = (TiledMapTileLayer) map.getLayers().get(1);

	}


	public void mapInitialize(){

		mapX = 0;

		// gets layer that the obstacles are in
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("obstacles");
		tileSize = layer.getTileWidth();

		// creates the bodys and fixtures
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		//ChainShape shape = new ChainShape();
		PolygonShape square = new PolygonShape();


		// making the box for player
		//bdef.position.set(player.getX() + player.getWidth() / 2,
		//		player.getY() + player.getHeight() / 2);

		bdef.position.set(player.position.x + player.rectBounds.width / 2,
				player.position.y + player.rectBounds.height / 2);


		// setup player
		bdef.position.set(0, 0);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);

		square.setAsBox(player.rectBounds.width / 2,
				player.rectBounds.height / 2,
				new Vector2(0, 100), 0);

		fdef.shape = square;
		
		//fdef.isSensor = true;
		fdef.filter.categoryBits = 4;
		fdef.filter.maskBits = 2;
		playerBody.createFixture(fdef).setUserData("player");



		//	goes through map and makes the boxes to deal with collisions
		for(int row = 0; row < layer.getHeight(); row++){
			for(int col = 0; col < layer.getWidth(); col++){
				Cell cell = layer.getCell(col, row);

				if(cell == null)continue;
				if(cell.getTile() == null)continue;

				bdef.type = BodyType.StaticBody;
				bdef.position.set(
						(col + 0.5f) * tileSize,
						(row + 0.5f) * tileSize
						);

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[5];
				v[0] = new Vector2(-tileSize / 2, -tileSize / 2);
				v[1] = new Vector2(-tileSize / 2, tileSize /2);
				v[2] = new Vector2(tileSize / 2, tileSize / 2);
				v[3] = new Vector2(tileSize/2, -tileSize/2);
				v[4] = new Vector2(-tileSize/2, -tileSize/2);
				cs.createChain(v);

				fdef.shape = cs;
				fdef.filter.categoryBits = 2;
				fdef.filter.maskBits = 4;

				world.createBody(bdef).createFixture(fdef);
			}
		}

	}


	// this method check for the state of the game and update if necessary
	public void stateMachine(float delta){

		state = worldState.PLAYING;

		switch(state){
		case READY:
			break;
		case PLAYING:
			statePlaying(delta);
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

		System.out.println("Game in Progress: Playing");

		// update
		world.step(1/60f, 6, 2);

		// 
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		// update camera and player position
		camera.position.set(mapX, player.position.y + player.rectBounds.height, 0);
		player.setPos(mapX, player.position.y);

		playerBody.setTransform(player.position.x + player.rectBounds.height / 5,
				player.position.y - player.rectBounds.height / 4, 0);

		// update map offset
		mapX += delta*60;
		camera.update();

		renderer.setView(camera);

		renderer.render();
		b2dr.render(world, camera.combined);

		renderer.getSpriteBatch().begin();

		player.sprite.draw(renderer.getSpriteBatch());

		renderer.getSpriteBatch().end();

	}

	public boolean updatePlayer(float deltaX, float deltaY){
		float tempX , tempY;

		player.oldPosition.y = player.position.y;
		//going down
		if(deltaY > 0){
			tempY = player.position.y;
			tempY -= deltaY;
			player.setPos(player.position.x, tempY);
		}
		//going up
		else if(deltaY < 0){
			tempY = player.position.y;
			tempY -= deltaY;
			player.setPos(player.position.x, tempY);
		}

		return true;
	}





	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public void dispose(){
		map.dispose();
		renderer.dispose();
		b2dr.dispose();
	}



	class ListenerClass implements ContactListener {

		@Override
		public void beginContact(Contact contact) {
			// TODO Auto-generated method stub
			System.out.println("contact detected");
			Fixture fa = contact.getFixtureA();
			Fixture fb = contact.getFixtureB();

			if(fa.getUserData() != null && fa.getUserData().equals("player")){
				System.out.println("playerA colliding");
			}
			if(fb.getUserData() != null && fb.getUserData().equals("player")){
				System.out.println("playerB colliding");
			}

		}

		@Override
		public void endContact(Contact contact) {
			// TODO Auto-generated method stub
			System.out.println("contact ended");

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {
			// TODO Auto-generated method stub

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {
			// TODO Auto-generated method stub

		}

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
		
		return updatePlayer(deltaX, deltaY);
		//return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
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
