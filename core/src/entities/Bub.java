package entities;

public class Bub extends Mook{

	public Bub(float x, float y, Type ai) {
		super(x, y, ai);
		setImage("sprites/bub.PNG");
		setCorpse("sprites/bubdead.PNG");
		maxSpeed = 0.5f;
		updateSpeed = 0.2f;
		damage = 0;
		fallSpeed = 0.8f;
		density = 1.2f;
		health = 2;
	}

}
