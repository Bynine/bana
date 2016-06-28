package entities;

import java.util.List;

import maps.Room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Whump extends Entity{

	private boolean flag_FALLING;
	float initY;
	Timer groundedTimer;
	Sprite normal, falling;

	public Whump(float x, float y) {
		super(x, y);
		normal = new Sprite(new Texture(Gdx.files.internal("sprites/whump.PNG")));
		falling = new Sprite(new Texture(Gdx.files.internal("sprites/whumpfalling.PNG")));
		setImage(normal);
		initY = y;
		is_ENEMY = true;
		is_IMMUNE = true;
		flag_FALLING = false;
		acceleration = 0.45f;
		damage = 2;
		density = 2;
		fallSpeed = 0;
		groundedTimer = new Timer(60);
	}

	@Override
	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, Room room, int deltaTime){
		this.deltaTime = deltaTime;
		setupRectangles(mapRectangleList, entityList);
		if (flag_FALLING && velocity.y <= 0) {
			velocity.y -= acceleration*room.getGravity();
			if (velocity.y < terminalVelocity*room.getGravity()) velocity.y = terminalVelocity*room.getGravity();
		}
		if (state == State.GROUND) {
			flag_FALLING = false;
			if (groundedTimer.stopped()) {
				groundedTimer.set();
			}
			if (groundedTimer.timed()){
				groundedTimer.reset();
				velocity.y = acceleration*2f;
			}
		}
		if (position.y >= initY-1) { // stop rising
			state = State.JUMP;
			if (velocity.y > 0) velocity.y = 0;
		}
		checkWalls(tempRectangleList);
		checkGrounded(tempRectangleList);
		checkDiagonal(tempRectangleList);
	}
	
	@Override
	public void reactToHero(Hero hero){
		if (Math.abs(position.x - hero.getPosition().x) < TILE && position.y > hero.getPosition().y) {
			if (velocity.y >= -0.1) flag_FALLING = true;
		}
	}
	
	@Override
	public void updateImage(){
		super.updateImage();
		if (flag_FALLING || state == State.GROUND) changeImage(falling);
		else if (velocity.y > 0) changeImage(normal);
		else changeImage(normal);
	}
}
