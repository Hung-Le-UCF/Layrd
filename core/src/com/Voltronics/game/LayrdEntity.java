/*
 * This Interface represents an object or player within the game.
 */
package com.Voltronics.game;

import com.badlogic.gdx.math.Vector2;

public interface LayrdEntity {
	Vector2 getPosition();
	Vector2 getVelocity();
	Vector2 getAcceleration();
}
