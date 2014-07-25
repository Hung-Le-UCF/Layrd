package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {
	
	public static Texture menuItems;
	public static TextureRegion mainMenu;
	public static TiledMap	currentLevel;
	
	public static Texture loadTexture (String file)
	{
		return new Texture(Gdx.files.internal(file));
	}
	
	public static void loadLevel(int level){
		switch(level){
		case 0:
			currentLevel = new TmxMapLoader().load("level1.tmx");
			break;
		case 1:
			
			break;
		}
	}
	
	public static void load()
	{
		menuItems = loadTexture("items.png");
		mainMenu = new TextureRegion(menuItems, 0, 224, 300, 110);
		
	}

}
