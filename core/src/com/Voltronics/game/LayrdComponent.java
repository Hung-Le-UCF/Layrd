/*
 * Base for all components within the game.
 */
package com.Voltronics.game;

public interface LayrdComponent {
	public void receiveEvent(LayrdEvent event);
	public void run();
	public void onStartup();
	public void onShutdown();
}
