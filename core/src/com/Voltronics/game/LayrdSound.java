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

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.assets.AssetManager;

public class LayrdSound implements LayrdComponent {
	private static final LayrdComponentType type = LayrdComponentType.SOUND;
	private Deque<LayrdEvent> events;
	private AssetManager manager;
	
	@Override
	public void handleEvent(LayrdEvent event) {
		events.add(event);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		events = new LinkedList<LayrdEvent>();
		manager = new AssetManager();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		// Clean up all used resources
		manager.dispose();
	}

	@Override
	public LayrdComponentType getType() {
		return type;
	}
}
