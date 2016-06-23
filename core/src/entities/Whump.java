package entities;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public class Whump extends Entity{

	private boolean flag_FALLING;
	float initY;
	Timer groundedTimer;

	public Whump(float x, float y) {
		super(x, y);
		setImage("sprites/whump.PNG");
		initY = y;
		is_ENEMY = true;
		is_IMMUNE = true;
		flag_FALLING = false;
		updateSpeed = 0.6f;
		damage = 3;
		fallSpeed = 0;
		groundedTimer = new Timer(60);
	}

	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, float gravity, int deltaTime){
		this.deltaTime = deltaTime;
		setupRectangles(mapRectangleList, entityList);
		if (flag_FALLING && velocity.y <= 0) {
			velocity.y -= updateSpeed*gravity;
			if (velocity.y < terminalVelocity*gravity) velocity.y = terminalVelocity*gravity;
		}
		if (state == State.GROUND && groundedTimer.stopped()) groundedTimer.set();
		if (state == State.GROUND && groundedTimer.timed()){
			flag_FALLING = false;
			velocity.y = updateSpeed*2f;
		}
		if (position.y >= initY) { // stop rising
			state = State.JUMP;
			if (velocity.y > 0) velocity.y = 0;
		}
		checkWalls(tempRectangleList);
		checkGrounded(tempRectangleList);
		checkDiagonal(tempRectangleList);
	}
	public void reactToHero(Entity hero){
		if (Math.abs(position.x - hero.getPosition().x) < TILE && position.y > hero.getPosition().y) {
			flag_FALLING = true;
		}
	}
}
