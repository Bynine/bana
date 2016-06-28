package entities;

import com.badlogic.gdx.Gdx;

public class Block extends Entity{
	
	public Block(float x, float y) {
		super(x, y);
		setImage("sprites/blockobject.PNG");
		setCorpse("sprites/blockdestroyed.PNG");
		collision = Collision.SOLID;
		does_MOVE = false;
		knockback = 0.5f;
		is_DESTRUCTABLE = true;
		fallSpeed = 0;
		hurt = Gdx.audio.newSound(Gdx.files.internal("sfx/break.mp3"));
	}
}
