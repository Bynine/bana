package entities;

public class Angerball extends Entity{

	public Angerball(float x, float y) {
		super(x, y);
		setImage("sprites/fish.PNG");
		is_ENEMY = true;
		fallSpeed = 0;
	}

}
