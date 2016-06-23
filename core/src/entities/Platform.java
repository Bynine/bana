package entities;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public class Platform extends Entity{

	private Movement movement; 
	
	public Platform(float x, float y, Movement movement) {
		super(x, y);
		this.movement = movement;
		setImage("sprites/platform.PNG");
		collision = Collision.SOLID;
		fallSpeed = 0;
		friction = 0;
		if (this.movement == Movement.SIDETOSIDE) velocity.x += 2;
	}
	
	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, float gravity, int deltaTime, float mapWidth){
		setupRectangles(mapRectangleList, entityList);
		checkGrounded(tempRectangleList);
		checkDiagonal(tempRectangleList);
		if (this.movement == Movement.SIDETOSIDE) {
			if (deltaTime%40 == 0) velocity.x*=-1;
		}
	}
	
	public void reactToAll(Entity en){
		if (en.isAtop(this)) {
			if (Math.abs(en.velocity.x) < Math.abs(velocity.x) && en.friction != 0) en.velocity.x = velocity.x*en.friction;
		}
	}

	public enum Movement{
		STILL, SIDETOSIDE
	}
}
