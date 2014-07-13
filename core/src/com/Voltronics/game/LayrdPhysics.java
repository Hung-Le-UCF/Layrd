package com.Voltronics.game;

import java.util.Deque;
import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class LayrdPhysics implements LayrdComponent {
	private final static LayrdComponentType type = LayrdComponentType.PHYSICS;
	private Deque<LayrdEvent> events;
	private World world;
	
	@Override
	public void handleEvent(LayrdEvent event) {
		events.add(event);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Do physics stuff
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		events = new LinkedList<LayrdEvent>();
		world = new World(new Vector2(0, 10), true);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		// Clean up all used resources
		world.dispose();
	}

	@Override
	public LayrdComponentType getType() {
		return type;
	}

}
