package com.Voltronics.game;


public class Player extends GameObject{


    public enum state{IDLE, MOVING, DYING};
	public state plState = state.IDLE;
	
	public Player(float x, float y, float width, float height){


        super(Math.max(x/2, x),
				Math.max(y/2, y),
				Math.max(1, width),
				Math.max(1, height));
	}
	
	
	public void setPos(float x, float y){
		this.position.x = Math.max(this.rectBounds.x/2, x);
		this.position.y = Math.max(this.rectBounds.y/2, y);
		
		sprite.setPosition(x, y);
	}

}
