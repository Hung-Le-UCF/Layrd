/*
 * Used by the game components to send messages to each other.
 */
package com.Voltronics.game;

import java.util.HashMap;
import java.util.Map;

public class LayrdEventBus {
	private Map<LayrdComponentType, LayrdComponent> subscribers;
	
	public LayrdEventBus() {
		subscribers = new HashMap<LayrdComponentType, LayrdComponent>();
	}
	
	public void subscribe(LayrdComponent component, LayrdComponentType type) {
		subscribers.put(type, component);
	}
	
	public void publish(LayrdEvent event) {
		LayrdComponentType type = event.getTargetComponent();
		LayrdComponent subscriber = subscribers.get(type);
		subscriber.receiveEvent(event);
	}
}
