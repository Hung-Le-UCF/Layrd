package com.Voltronics.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject implements GestureListener {



	public int health;



    public enum state{IDLE, MOVING, DYING};
	public state plState = state.IDLE;
	
	public Player(float x, float y, float width, float height){


        super(Math.max(x/2, x),
				Math.max(y/2, y),
				Math.max(1, width),
				Math.max(1, height));
        GestureDetector gestureDect = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDect);
	}
	
	
	public void setPos(float x, float y){
		this.position.x = Math.max(this.rectBounds.x/2, x);
		this.position.y = Math.max(this.rectBounds.y/2, y);
		
		sprite.setPosition(x, y);
	}

    //  method to move the player up and down
    public boolean updatePlayer(float deltaX, float deltaY){
        float tempY;

        //player.oldPosition.y = player.position.y;
        //going down
        if(deltaY > 0){
            tempY = this.position.y;
            tempY -= deltaY;
            this.setPos(this.position.x, tempY);
        }
        //going up
        else if(deltaY < 0){
            tempY = this.position.y;
            tempY -= deltaY;
            this.setPos(this.position.x, tempY);
        }

        return true;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    //  if the user pans his finger up or down it calls the method to preform movement
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return updatePlayer(deltaX , deltaY);
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
	
	
}
