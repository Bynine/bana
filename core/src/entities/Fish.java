package entities;

public class Fish extends Entity{
	
	private boolean flag_INIT;
	
	public Fish(float x, float y) {
		super(x, y);
		setImage("sprites/fish.PNG");
		setCorpse("sprites/fishdead.PNG");
		is_ENEMY = true;
		collision = Collision.GHOST;
		fallSpeed = 0;
		updateSpeed = 0.8f;
		maxSpeed = 4;
		density = 0.6f;
		flag_INIT = false;
	}
	
	public void reactToHero(Entity hero){
		if (!flag_INIT) {
			flag_INIT = true;
			if (hero.getPosition().x > position.x) image.flip(true, false);
			else updateSpeed *= -1;
		}
		if (isThisCloseTo(hero, 480)) velocity.x += updateSpeed;
	}
}
