/*
 * Sound component which receives sound events form the Logic component and
 * then plays the sound effect.
 * 
 * We need to generate a mapping or another way to store sounds and music as
 * they are loaded in the game. We then can use that mapping access the
 * generated sounds `eg Gdx.audio.newSound(Gdx.files.internal("mp3.mp3"));`
 * and play them directly.
 * 
 * Do not forget to dispose of the sounds as the game exits.
 */
package com.Voltronics.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;


public class LayrdSound {
	private static Map<String, Sound> soundManager = new HashMap<String, Sound>();
	private static Map<String, Sound> musicManager = new HashMap<String, Sound>();
	
	
	public static void loadSounds(String[] keys, String[] paths){
		for(int i = 0; i < paths.length; i++)
			loadSound(keys[i], paths[i]);
	}
	
	public static void loadSound(String key, String path){
		soundManager.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
	}
	
	
	public static Sound getSound(String soundName){
		if(soundManager.containsKey(soundName)){
			return soundManager.get(soundName);
		}
		
		return null;
	}
	
	
	public static void loadMusics(String[] keys, String[] paths){
		for(int i = 0; i < paths.length; i++)
			loadMusic(keys[i], paths[i]);
	}
	
	public static void loadMusic(String key, String path){
		musicManager.put(key, Gdx.audio.newSound(Gdx.files.internal(path)));
	}
	
	
	public static Sound getMusic(String soundName){
		if(musicManager.containsKey(soundName)){
			return musicManager.get(soundName);
		}
		
		// no sound found, return null?
		return null;
	}
	
	
	
}
