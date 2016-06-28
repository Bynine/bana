package entities;

import main.Bana;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Trapdoor extends Entity{
	
	private boolean flag_ACTIVATED;
	private Sound activate = Gdx.audio.newSound(Gdx.files.internal("sfx/break.mp3"));

	public Trapdoor(float x, float y) {
		super(x, y);
		setImage("sprites/trapdoorclosed.PNG");
		does_MOVE = false;
		collision = Collision.SOLID;
	}
	
	void activate(){
		if (!flag_ACTIVATED){
			position.x -= 14;
			setImage("sprites/trapdooropen.PNG");
			activate.play(Bana.getVolume());
			flag_ACTIVATED = true;
		}
	}

}
