/*
 * Used by the game components to send messages to each other.
 */
package com.Voltronics.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayrdEventBus {
	private Map<LayrdEventType, List<LayrdComponent>> eventSubscribers;
	
	public LayrdEventBus() {
		eventSubscribers = new HashMap<LayrdEventType, List<LayrdComponent>>();
	}
	
	public void subscribe(LayrdComponent component, LayrdEventType type) {
		if (!eventSubscribers.containsKey(type)) {
			eventSubscribers.put(type, new ArrayList<LayrdComponent>());
		}
		
		List<LayrdComponent> subscribers = eventSubscribers.get(type);
		subscribers.add(component);
	}
	
	public void publish(LayrdEvent event) {
		LayrdEventType type = event.getType();
		List<LayrdComponent> subscribers = eventSubscribers.get(type);
		for (LayrdComponent subscriber : subscribers) {
			subscriber.handleEvent(event);
		}
	}
	
	public void dispose() {
		for (List<LayrdComponent> list : eventSubscribers.values()) {
			list.clear();
		}
		eventSubscribers.clear();
	}
}
