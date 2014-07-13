/*
 * Base for all events that will be used within the game. Just enough for
 * EventBus to route the event through the system.
 */
package com.Voltronics.game;

public interface LayrdEvent {
	public LayrdEventType getType();
}
