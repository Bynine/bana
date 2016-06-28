package maps;

import com.badlogic.gdx.Gdx;

import entities.*;

public class Room_OasisStart extends Room {
	public Room_OasisStart(Level superLevel){
		super(superLevel);
		map = tmxMapLoader.load("maps/OasisStart.tmx");
		setStartPosition(TILE*6, TILE*3);
		gravity = 1f;
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/desert.mp3"));
		setup();
	}
	
	@Override
	public void initEntities(Hero hero) {
		super.initEntities(hero);
		entityList.add(new Door(TILE*93, TILE*3, superLevel.getRoom(1), TILE*2, TILE*3));
		addObjects(superLevel.getRoomCollectibles(0));
		entityList.add(new Bub(TILE*22, TILE*3, Mook.Type.WALK));
		entityList.add(new Bub(TILE*37, TILE*3, Mook.Type.WALK));
		entityList.add(new Bub(TILE*84, TILE*3, Mook.Type.WALK));
		entityList.add(new Bub(TILE*88, TILE*3, Mook.Type.WALK));
		entityList.add(new Block(TILE*27, TILE*5));
		entityList.add(new Block(TILE*27, TILE*6));
		entityList.add(new Block(TILE*27, TILE*7));
		entityList.add(new Block(TILE*28, TILE*7));
		entityList.add(new Block(TILE*29, TILE*7));
		entityList.add(new Block(TILE*30, TILE*7));
		entityList.add(new Block(TILE*31, TILE*7));
		entityList.add(new Block(TILE*31, TILE*6));
		entityList.add(new Block(TILE*31, TILE*5));
		entityList.add(new Block(TILE*41, TILE*7));
		entityList.add(new Block(TILE*41, TILE*8));
		entityList.add(new Block(TILE*41, TILE*9));
		entityList.add(new Block(TILE*55, TILE*7));
		entityList.add(new Block(TILE*55, TILE*8));
		entityList.add(new Block(TILE*55, TILE*9));
		entityList.add(new Mook(TILE*48, TILE*7, Mook.Type.WALK));
		entityList.add(new Mook(TILE*75, TILE*4, Mook.Type.WALK));
		setup();
	}
}
