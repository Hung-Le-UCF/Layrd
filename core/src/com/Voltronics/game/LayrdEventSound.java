package com.Voltronics.game;

public class LayrdEventSound implements LayrdEvent {

	@Override
	public LayrdEventType getType() {
		return LayrdEventType.SOUND;
	}
	
	public String whatSound() {
		return "dog.mp3";
	}

}
