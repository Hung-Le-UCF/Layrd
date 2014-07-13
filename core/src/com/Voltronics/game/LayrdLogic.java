/*
 * Logic component which receives events from across the game and decides
 * what action should take place. eg Collision should make an explosion and
 * remove the two entities involved.
 */
package com.Voltronics.game;

import java.util.Deque;
import java.util.LinkedList;

public class LayrdLogic implements LayrdComponent {
	private final static LayrdComponentType type = LayrdComponentType.LOGIC;
	private Deque<LayrdEvent> events;

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
		// Initialize resources
		events = new LinkedList<LayrdEvent>();
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
