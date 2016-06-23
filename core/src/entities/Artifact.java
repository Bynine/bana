package entities;

import com.badlogic.gdx.Gdx;

public class Artifact extends Collectible  {

	public Artifact(float x, float y, Type type) {
		super(x, y);
		collect = Gdx.audio.newSound(Gdx.files.internal("sfx/artifact.mp3"));
		switch(type){
		case CHEST: setImage("sprites/chest.PNG"); break;
			default: setImage("sprites/artifactUnknown.PNG"); break;
		}
		// TODO: give some indication of rarity. put in chest, make sparkle?
	}

	public enum Type{
		CHEST, URN, RELIC, GOLD
	}
	
	public void reactToHero(Entity hero){
		super.reactToHero(hero);
		if (isTouching(hero)) flag_GOTLEVELARTIFACT = true;
	}
}
