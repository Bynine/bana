package entities;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public class Bub extends Mook{

	public Bub(float x, float y, Type ai) {
		super(x, y, ai);
		walk = makeAnimation("sprites/bub.PNG", 4, 1, 20f, PlayMode.LOOP);
		setImage(walk.getKeyFrame(0));
		setCorpse("sprites/bubdead.PNG");
		maxSpeed = 0.5f;
		acceleration = 0.2f;
		damage = 0;
		fallSpeed = 0.8f;
		density = 1.2f;
		health = 2;
		bounce = 1.1f;
	}

}
