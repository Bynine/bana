package entities;

import com.badlogic.gdx.Gdx;

public class Food extends Collectible{

	public Food(float x, float y, Type type) {
		super(x, y);
		collect = Gdx.audio.newSound(Gdx.files.internal("sfx/eat.mp3"));
		switch(type){
		case LIGHT: value = 2; setImage("sprites/apple.PNG"); break;
		case HEAVY: value = 999; setImage("sprites/meat.PNG"); break;
		default: value = 0; setImage("sprites/fishdead.PNG"); break;
		}
	}

	public enum Type{
		LIGHT, HEAVY
	}
}
