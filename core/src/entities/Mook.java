package entities;

import java.util.List;

import maps.Room;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class Mook extends Entity {

	Type ai;
	Animation walk = makeAnimation("sprites/mook.PNG", 4, 1, 15f, PlayMode.LOOP);

	public Mook(float x, float y, Type ai) {
		super(x, y);
		this.ai = ai;
		setImage(walk.getKeyFrame(0));
		setCorpse("sprites/mookdead.PNG");
		is_ENEMY = true;
		friction = 1;
		fallSpeed = .5f;
		maxSpeed = 3f;
		acceleration = 0.6f;
		damage = 1;
		health = 1;
		friction = 0.8f;
	}

	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, Room room, int deltaTime){
		if (ai == Type.WALK && stunTimer.stopped()) walk(tempRectangleList, room.getGravity(), deltaTime);
		super.updateVelocity(mapRectangleList, entityList, room, deltaTime);
	}
	private void walk(List<Rectangle> mapRectangleList, float gravity, int deltaTime){
		if (velocity.x == 0 && health > 0) {
			if (flag_GORIGHT) {
				flag_GOLEFT = true; 
				flag_GORIGHT = false;
				facing = Facing.LEFT;
			}
			else {
				flag_GORIGHT = true;
				flag_GOLEFT = false;
				facing = Facing.RIGHT;
			}
			image.flip(true, false);
		}
	}

	@Override
	public void reactToHero(Hero hero){
		if (ai == Type.FOLLOW && isThisCloseTo(hero, 240)){
			if ((position.x - hero.getPosition().x) < -TILE/2) {
				if (flag_GOLEFT) image.flip(true, false);
				flag_GOLEFT = false;
				flag_GORIGHT = true;
			}
			if ((position.x - hero.getPosition().x) > TILE/2){
				if (flag_GORIGHT) image.flip(true, false);
				flag_GORIGHT = false;
				flag_GOLEFT  =  true;
			}
			if ((position.y - hero.getPosition().y) < -TILE && state != State.JUMP)    jump();
		}
		if (!isThisCloseTo(hero, 640)) {
			velocity.x = 0; velocity.y = 0;
		}
	}
	
	@Override
	public void updateImage(){
		super.updateImage();
		if (flag_GOLEFT || flag_GORIGHT) changeImage(walk.getKeyFrame(deltaTime));
	}

	public enum Type{
		WALK, FOLLOW
	}
}
