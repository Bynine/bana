package maps;

import com.badlogic.gdx.Gdx;

import entities.Hero;

public class Room_Test extends Room{

	public Room_Test(Level superLevel) {
		super(superLevel);
		map = tmxMapLoader.load("maps/Town1.tmx");
		setStartPosition(TILE*4, TILE*3);
		gravity = 1f;
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/win.mp3"));
		setup();
	}

	@Override
	public void initEntities(Hero hero) {
		super.initEntities(hero);
		setup();
	}
}
