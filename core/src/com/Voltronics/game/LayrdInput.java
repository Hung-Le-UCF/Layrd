/*
 * Responsible for reading all input events from libGDX and converting them
 * into their corresponding logic event. Both touch and mouse clicks should
 * be equivalent to each other.
 */
package com.Voltronics.game;

import com.badlogic.gdx.InputProcessor;

public class LayrdInput implements InputProcessor, LayrdComponent {

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void receiveEvent(LayrdEvent event) {
		/* no point for input to receive 'input' events */
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStartup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub
		// Clean up all used resources
	}

}
