/*
 * Base for all components within the game.
 */
package com.Voltronics.game;

public interface LayrdComponent {
	public LayrdComponentType getType();
	public void handleEvent(LayrdEvent event);
	public void run();
	public void create();
	public void dispose();
}
