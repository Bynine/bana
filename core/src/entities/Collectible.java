package entities;

import main.Bana;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public abstract class Collectible extends Entity{
	
	Sound collect = Gdx.audio.newSound(Gdx.files.internal("sfx/coincollect.mp3"));
	int value;
	
	Collectible(float x, float y) {
		super(x, y);
		does_MOVE = false;
	}

	@Override
	public void reactToHero(Hero hero){
		if (isTouching(hero)) {
			flag_TODESTROY = true;
			behavior(hero);
			collect.play(Bana.getVolume());
		}
	}
	
	public int getValue(){
		return value;
	}
	
	abstract void behavior(Hero hero);
}
