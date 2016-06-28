package entities;

import com.badlogic.gdx.Gdx;

public class Coin extends Collectible {

	public Coin(float x, float y, Type type) {
		super(x, y);
		collect = Gdx.audio.newSound(Gdx.files.internal("sfx/coincollect.mp3"));
		fallSpeed = 0;
		switch(type){
		case COIN: value = 1; setImage("sprites/coin.PNG"); break;
		case GEM: value = 10; setImage("sprites/gem.PNG"); collect = Gdx.audio.newSound(Gdx.files.internal("sfx/artifact.mp3")); break;
		default: value = 0; setImage("sprites/coin.PNG"); break;
		}
	}
	
	@Override
	void behavior(Hero hero) {
		hero.addMoney(value);
	}

	public enum Type{
		COIN, GEM
	}
}
