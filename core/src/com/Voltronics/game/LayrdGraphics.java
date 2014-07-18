/*
 * Graphics component which will draw all game objects on the screen.
 */
package com.Voltronics.game;

import java.util.HashMap;
import java.util.Map;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.scenes.scene2d.ui.List;

public class LayrdGraphics {

	private Map<String, Texture> spritesManager;
	private Map<String, Texture> BGsManager;
	
	
	public LayrdGraphics(){
		spritesManager = new HashMap<String, Texture>();
		BGsManager = new HashMap<String, Texture>();
		
		
	}
	
	public void loadSprites(String[] keys, String[] paths){
		for(int i = 0; i < paths.length; i++)
			loadSprite(keys[i], paths[i]);
	}
	
	public void loadSprite(String key, String path){
		spritesManager.put(key, new Texture( Gdx.files.internal(path) ));
	}
	
	
	public Texture getSprite(String spriteName){
		if(spritesManager.containsKey(spriteName)){
			return spritesManager.get(spriteName);
		}
		
		// no texture found, return null? or return plain texture
		return null;
		//return new Texture("");
	}
	
	
	public void loadBGs(String[] keys, String[] paths){
		for(int i = 0; i < paths.length; i++)
			loadBG(keys[i], paths[i]);
	}
	
	public void loadBG(String key, String path){
		BGsManager.put(key, new Texture( Gdx.files.internal(path) ));
	}
	
	
	public Texture getBG(String spriteName){
		if(BGsManager.containsKey(spriteName)){
			return BGsManager.get(spriteName);
		}
		
		// no texture found, return null? or return plain texture
		return null;
		//return new Texture("");
	}
}
