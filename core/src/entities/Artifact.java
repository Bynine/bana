package entities;

import com.badlogic.gdx.Gdx;

public class Artifact extends Collectible  {
	
	boolean flag_OBTAINED;

	public Artifact(float x, float y, Type type) {
		super(x, y);
		collect = Gdx.audio.newSound(Gdx.files.internal("sfx/artifact.mp3"));
		switch(type){
		case CHEST: setImage("sprites/chest.PNG"); break;
		default: setImage("sprites/artifactUnknown.PNG"); break;
		}
		does_MOVE = true;
	}

	@Override
	void behavior(Hero hero) {
		flag_OBTAINED = true;	
	}
	
	public boolean isObtained(){
		return flag_OBTAINED;
	}

	public enum Type{
		CHEST, URN, RELIC, GOLD
	}
}
