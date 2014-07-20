package com.Voltronics.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameObject {

	public Vector2 oldPosition;
	public Vector2 position;
	public Rectangle rectBounds;
	public Vector2 velocity;
	public Sprite sprite;

	public GameObject (float x, float y, float width, float height) {
		this.oldPosition = new Vector2(x, y);
		this.position = new Vector2(x, y);
		this.rectBounds = new Rectangle(x - width / 2, y - height / 2, width, height);
		velocity = new Vector2();
		sprite = new Sprite();
	}

}
