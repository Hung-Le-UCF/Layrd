/*
 * Graphics component which will draw all game objects on the screen.
 */
package com.Voltronics.game;

import java.util.HashMap;
import java.util.Map;




import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.scenes.scene2d.ui.List;

public class LayrdGraphics {

	private static Map<String, Texture> spritesManager = new HashMap<String, Texture>();
	private static Map<String, Texture> BGsManager = new HashMap<String, Texture>();;
	
	
	public static void loadSprites(String[] keys, String[] paths){
		for(int i = 0; i < paths.length; i++)
			loadSprite(keys[i], paths[i]);
	}
	
	public static void loadSprite(String key, String path){
		spritesManager.put(key, new Texture( Gdx.files.internal(path) ));
	}
	
	
	public static Texture getTexture(String textureName){
		if(spritesManager.containsKey(textureName)){
			return spritesManager.get(textureName);
		}
		
		// no texture found, return plain texture
		System.out.println("Texture: " + textureName + " cannot be found");
		return new Texture("");
		//return new Texture("");
	}
	
	public static Sprite getSprite(String spriteName){
		return new Sprite(getTexture(spriteName));
	}
	
	
	public static void loadBGs(String[] keys, String[] paths){
		for(int i = 0; i < paths.length; i++)
			loadBG(keys[i], paths[i]);
	}
	
	public static void loadBG(String key, String path){
		BGsManager.put(key, new Texture( Gdx.files.internal(path) ));
	}
	
	
	public static Texture getBG(String spriteName){
		if(BGsManager.containsKey(spriteName)){
			return BGsManager.get(spriteName);
		}
		
		// no texture found, return plain texture
		return new Texture("");
		//return new Texture("");
	}
}
