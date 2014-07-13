/*
 * Graphics component which will draw all game objects on the screen.
 */
package com.Voltronics.game;

public class LayrdGraphics implements LayrdComponent {
	private final static LayrdComponentType type = LayrdComponentType.GRAPHICS;
	
	@Override
	public void handleEvent(LayrdEvent event) {
		/* there should be no events needed by graphics */
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Graphics stuff
		// Clean up all used resources
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		// Clean up all used resources
	}

	@Override
	public LayrdComponentType getType() {
		return type;
	}

}
