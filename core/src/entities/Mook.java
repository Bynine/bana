package entities;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

public class Mook extends Entity {

	Type ai;

	public Mook(float x, float y, Type ai) {
		super(x, y);
		this.ai = ai;
		setImage("sprites/mook.PNG");
		setCorpse("sprites/mookdead.PNG");
		is_ENEMY = true;
		friction = 1;
		fallSpeed = .5f;
		maxSpeed = 3f;
		updateSpeed = 0.6f;
		damage = 1;
		health = 1;
		friction = 0.8f;
	}

	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, float gravity, int deltaTime){
		if (ai == Type.WALK && stunTimer.stopped()) walk(tempRectangleList, gravity, deltaTime);
		super.updateVelocity(mapRectangleList, entityList, gravity, deltaTime);
	}
	private void walk(List<Rectangle> mapRectangleList, float gravity, int deltaTime){
		if (velocity.x == 0 && health > 0) {
			if (flag_GORIGHT) {
				flag_GOLEFT = true; 
				flag_GORIGHT = false;
			}
			else {
				flag_GORIGHT = true;
				flag_GOLEFT = false;
			}
			image.flip(true, false);
		}
	}

	public void reactToHero(Entity hero){
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

	public enum Type{
		WALK, FOLLOW
	}
}
