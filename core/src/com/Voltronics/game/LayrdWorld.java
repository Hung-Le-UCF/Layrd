package com.Voltronics.game;

import com.badlogic.gdx.Game;
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

public class LayrdWorld extends Game implements ContactListener{

	public enum worldState{READY, PLAYING, PAUSED, LEVELFINISH, GAMEOVER}
	public worldState state = worldState.READY;

	private Player player;

	////////////////////////////////////////////////////////
	public OrthogonalTiledMapRenderer renderer;
	public OrthographicCamera camera;
	private Box2DDebugRenderer b2dr;

	private World world;
	private TiledMap map;

    //private ListenerClass listener;
	private Body playerBody;
    private Body endingBody;

	private float tileSize;
	private float mapX;

    private float difficulty;

    private Texture items;
    private TextureRegion gameOver;
    private SpriteBatch batcher;
	/////////////////////////////////////////////////////////


	private GestureDetector gestureDect;

	private LayrdGraphics graphicsLib;
	private LayrdSound soundsLib;
	private LayrdLogic gameLogic;

	public LayrdWorld(String tileMapDirectory){

        //stateMachine();
		// must initiate component lib first
        //initializing so that we know the first level loads with no issues
        difficulty = 0;

        //  makes the sprite for gameOver Screen
        //  TODO make sprites for READY/ PAUSED screen
        batcher = new SpriteBatch();
        items = new Texture(Gdx.files.internal("items.png"));
        gameOver = new TextureRegion(items, 352, 256,160, 96);
		gameComponentsInitialize(tileMapDirectory);





	}

	private void playerInitialize(){	
        // initialize player and game objects (if any)

        // TODO replace player hard-coded number with variables (screen size OR pre-defined constants)
        player = new Player(50, 800 / 2 - 64 / 2, 44, 66);

        //  makes player spawn at bottom of level
        //  without this there was issues with player not able to reach bottom of screen
       // player = new Player(-100, 0, 44, 66);

        //  makes player jump to middle of screen for starting
       // player.setPos(player.position.x, 800 / 2 - 64 / 2);


        // TODO require investigate of multiple objects using the same texture
        // be aware that the texture here is originate in graphics library
        // any direct change to texture MAY OR MAY NOT cause change to all objects using it
        player.sprite.set(new Sprite (graphicsLib.getSpriteTexture("player")) );


        System.out.println("Player Loaded Sucessful");


        player.sprite.setSize(44, 66);
	}

	private void gameComponentsInitialize(String tileMapDirectory){
		//  raises difficulty by 1 each time game is loading new level
        difficulty++;
        System.out.println("diff:" + difficulty);

        // initialize library
        graphicsLib = new LayrdGraphics();
        soundsLib = new LayrdSound();
        gameLogic = new LayrdLogic();

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        //listener = new ListenerClass();

        TmxMapLoader mapLoader = new TmxMapLoader();

        world.setContactListener(this);


        // TODO LayrdInput is not completed
        //gestureDect = new GestureDetector(new LayrdInput());
        //gestureDect = new GestureDetector(this);
        //Gdx.input.setInputProcessor(gestureDect);


        //  loads level depending on difficulty
        if(map == null || difficulty == 1){
            map = mapLoader.load("testMap.tmx");

        }
        else if(map != null && difficulty == 2){
            map = mapLoader.load("level2.tmx");

        }
        else if(map!= null && difficulty == 3){
            map = mapLoader.load("level3.tmx");
        }


        //TODO
        graphicsLib.loadSprite("player", "sprite.png");
        playerInitialize();

        mapInitialize();


        // initialize camera
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera(480, 320);
        camera.zoom = 1;

        //  makes the camera zoom only the first time
        camera.zoom *= 2.5f;
        System.out.println("camera.zom" + camera.zoom);


        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        state = worldState.READY;
        stateMachine(difficulty);



	}

	public void mapInitialize(){

        //items = new Texture(Gdx.files.internal("items.png"));
        //gameOver = new TextureRegion(items, 352, 256,160, 96);

		mapX = 0;
        System.out.println("map === " + map.getLayers().get(0).getName());
		// gets layer that the obstacles are in
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("obstacles");
        TiledMapTileLayer finish = (TiledMapTileLayer) map.getLayers().get("end");
        tileSize = layer.getTileWidth();


		// creates the bodys and fixtures
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
        BodyDef bdef2 = new BodyDef();
        FixtureDef fdef2 = new FixtureDef();
		//ChainShape shape = new ChainShape();
		PolygonShape square = new PolygonShape();
        PolygonShape square2 = new PolygonShape();


		// making the box for player
		//bdef.position.set(player.getX() + player.getWidth() / 2,
		//		player.getY() + player.getHeight() / 2);

		bdef.position.set(player.position.x + player.rectBounds.width ,
				player.position.y );


		// setup player
		//bdef.position.set(player.position.x, player.position.y);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);

		square.setAsBox(player.rectBounds.width / 2,
				player.rectBounds.height / 2,
				new Vector2(0, 66), 0);



		fdef.shape = square;


		//fdef.isSensor = true;
		fdef.filter.categoryBits = 4;
		fdef.filter.maskBits = 2;
		playerBody.createFixture(fdef).setUserData("player");



        //  seperate fixutre around player to make interactions with ending panel
        bdef2.position.set(player.position.x + player.rectBounds.width ,
                player.position.y );


        // setup player
        //bdef.position.set(player.position.x, player.position.y);
        bdef2.type = BodyType.DynamicBody;
        endingBody = world.createBody(bdef2);

        square2.setAsBox(player.rectBounds.width / 2,
                player.rectBounds.height / 2,
                new Vector2(0, 66), 0);



        fdef2.shape = square2;


        fdef2.filter.categoryBits = 8;
        fdef2.filter.maskBits = 6;
        endingBody.createFixture(fdef2).setUserData("endRegion");



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

        //  makes the boxes around the finishing portion
        for(int row = 0; row < finish.getHeight(); row++){
            for(int col = 0; col < finish.getWidth(); col++){
                Cell cell = finish.getCell(col, row);

                if(cell == null)continue;
                if(cell.getTile() == null)continue;

                bdef2.type = BodyType.StaticBody;
                bdef2.position.set(
                        (col + 0.5f) * tileSize,
                        (row + 0.5f) * tileSize
                );

                ChainShape cs2 = new ChainShape();
                Vector2[] v = new Vector2[5];
                v[0] = new Vector2(-tileSize / 2, -tileSize / 2);
                v[1] = new Vector2(-tileSize / 2, tileSize /2);
                v[2] = new Vector2(tileSize / 2, tileSize / 2);
                v[3] = new Vector2(tileSize/2, -tileSize/2);
                v[4] = new Vector2(-tileSize/2, -tileSize/2);
                cs2.createChain(v);

                fdef2.shape = cs2;
                fdef2.filter.categoryBits = 6;
                fdef2.filter.maskBits = 8;

                world.createBody(bdef2).createFixture(fdef2);



            }
        }


	}


	// this method check for the state of the game and update if necessary
	public void stateMachine(float delta){

        //System.out.println("STATE" + state.toString());
		//state = worldState.PLAYING;

		switch(state){
		case READY:
			stateReady(delta);
            break;
		case PLAYING:
			statePlaying(delta);
			break;
		case PAUSED:
			break;
		case LEVELFINISH:
            stateFinishLevel(delta);
			break;
		case GAMEOVER:
            stateGameover(delta);
			break;
		default:
			break;
		}

	}

    private void stateReady(float delta) {
        Gdx.gl.glClearColor(1, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //System.out.println("System Paused");
        camera.update();
        renderer.render();
        if(Gdx.input.isTouched()){
            state = worldState.PLAYING;
            stateMachine(difficulty);
        }

    }

    public void statePlaying(float delta){

        //System.out.println("System Running");
        camera.update();
		//System.out.println("Game in Progress: Playing");


        // update
        world.step(1/60f, 6, 2);

		// 
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		//System.out.println(world.getContactCount());

		// update camera and player position
		camera.position.set(mapX, player.position.y + player.rectBounds.height, 0);
		player.setPos(mapX, player.position.y);
        //player.sprite.setPosition(mapX, player.position.y);

		playerBody.setTransform(player.position.x + player.rectBounds.height / 3,
				player.position.y - player.rectBounds.width / 1.375f, 0);
        endingBody.setTransform(player.position.x + player.rectBounds.height / 3,
                player.position.y - player.rectBounds.width / 1.375f, 0);


		// update map offset
		//  increases as the difficulty increases
        if(difficulty == 1) mapX += delta*60;

        if(difficulty == 2 ) mapX += delta*120;

        if (difficulty == 3) mapX += delta*240;

        camera.update();

		renderer.setView(camera);

		renderer.render();
		b2dr.render(world, camera.combined);

		renderer.getSpriteBatch().begin();

		player.sprite.draw(renderer.getSpriteBatch());

		renderer.getSpriteBatch().end();



	}

    public void stateGameover(float delta){
       // batcher = new SpriteBatch();
       // items = new Texture(Gdx.files.internal("items.png"));
       // gameOver = new TextureRegion(items, 352, 256,160, 96);

        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        batcher.draw(gameOver,
                (float) (Gdx.graphics.getWidth() / 2) - gameOver.getRegionWidth() * 1.5f,
                Gdx.graphics.getHeight() / 2 - gameOver.getRegionHeight() * 2,
                    500, 500);
        batcher.end();
        map.dispose();
        difficulty = 0;




    }

    public void stateFinishLevel(float delta){
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //cs.dispose();
        //cs2.dispose();
        //difficulty++;
        //System.out.println("waiting for nextLevel");

        if(Gdx.input.isTouched()) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            gameComponentsInitialize("newLevel");
        }

    }
/*
	public boolean updatePlayer(float deltaX, float deltaY){
		float tempY;

		//player.oldPosition.y = player.position.y;
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
	}*/

    @Override
    public void create() {

    }

    public void dispose(){
		map.dispose();
		renderer.dispose();
		b2dr.dispose();
		world.dispose();
		player.sprite.getTexture().dispose();
        batcher.dispose();

		
	}





    @Override
    public void beginContact(Contact contact) {
        // TODO Auto-generated method stub
        //System.out.println("contact detected");
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        fa.setUserData("endRegion");
        //fb.setUserData("player");


        if(fa.getUserData() != null && fa.getUserData().equals("endRegion")){
           // System.out.println("end Collide Collide");
            if(state != worldState.GAMEOVER ){
                //difficulty = 1;
               // System.out.println("diff=" + difficulty);
            }

            state = worldState.LEVELFINISH;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("player")){
            //System.out.println("playerB colliding");
            if(state != worldState.LEVELFINISH ){
                difficulty = 0;
               // System.out.println("diff=" + difficulty);
            }

            state = worldState.GAMEOVER;

        }

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
/*
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
	}*/
}
