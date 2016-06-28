package maps;

import com.badlogic.gdx.Gdx;

import entities.*;

public class Room_OasisMiddle extends Room{
	public Room_OasisMiddle(Level superLevel){
		super(superLevel);
		map = tmxMapLoader.load("maps/OasisMiddle.tmx");
		setStartPosition(TILE*2, TILE*3);
		gravity = 1f;
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/temple.mp3"));
		setup();
	}
	
	@Override
	public void initEntities(Hero hero) {
		super.initEntities(hero);
		entityList.add(new Door(TILE*2, TILE*3, superLevel.getRoom(0), TILE*93, TILE*3));
		entityList.add(new Door(TILE*28, TILE*3, superLevel.getRoom(2), TILE*3, TILE*3));
		entityList.add(Level_Oasis.getArtifact());
		addObjects(superLevel.getRoomCollectibles(1));
		Trapdoor trapdoor = new Trapdoor(TILE*14, TILE*7);
		entityList.add(trapdoor);
		entityList.add(new Switch(TILE*7, TILE*6, trapdoor));
		entityList.add(new Whump(TILE*25, TILE*7.5f));
		setup();
	}
}
