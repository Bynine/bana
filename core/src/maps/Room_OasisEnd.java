package maps;

import com.badlogic.gdx.Gdx;

import entities.*;

public class Room_OasisEnd extends Room {
	public Room_OasisEnd(Level superLevel){
		super(superLevel);
		map = tmxMapLoader.load("maps/OasisEnd.tmx");
		setStartPosition(TILE*3, TILE*3);
		gravity = 1f;
		roomMusic = Gdx.audio.newMusic(Gdx.files.internal("music/desert.mp3"));
		setup();
	}
	
	@Override
	public void initEntities(Hero hero) {
		super.initEntities(hero);
		entityList.add(new Door(TILE*3, TILE*3, superLevel.getRoom(1), TILE*12, TILE*3));
		entityList.add(new Door(TILE*91, TILE*3, superLevel.getRoom(1), TILE*12, TILE*3));
		entityList.addAll(superLevel.getRoomContents(2));
		entityList.add(new Spikes(TILE*20, TILE*3, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*20, TILE*4, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*20, TILE*5, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*3, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*4, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*5, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*24, TILE*4, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*24, TILE*5, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*6, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*7, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*8, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*9, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*10, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*11, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*12, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*13, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*22, TILE*12, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*22, TILE*13, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*23, TILE*14, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*4, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*5, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*6, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*61, TILE*5, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*61, TILE*6, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*7, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*8, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*9, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*59, TILE*8, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*59, TILE*9, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*10, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*11, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*12, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*61, TILE*11, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*61, TILE*12, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*13, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*14, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*60, TILE*15, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*59, TILE*14, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*59, TILE*15, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*68, TILE*9, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*68, TILE*10, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*68, TILE*11, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*68, TILE*12, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*68, TILE*13, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*69, TILE*12, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*69, TILE*13, Spikes.Type.CACTUS));
		entityList.add(new Spikes(TILE*68, TILE*14, Spikes.Type.CACTUS));
		entityList.add(new Block(TILE*67, TILE*3));
		entityList.add(new Block(TILE*67, TILE*4));
		entityList.add(new Block(TILE*67, TILE*5));
		entityList.add(new Block(TILE*68, TILE*3));
		entityList.add(new Block(TILE*68, TILE*4));
		entityList.add(new Block(TILE*68, TILE*5));
		entityList.add(new Block(TILE*82, TILE*3));
		entityList.add(new Block(TILE*82, TILE*4));
		entityList.add(new Block(TILE*82, TILE*5));
		entityList.add(new Block(TILE*83, TILE*3));
		entityList.add(new Block(TILE*83, TILE*4));
		entityList.add(new Block(TILE*83, TILE*5));
		entityList.add(new Bub(TILE*36, TILE*3, Mook.Type.WALK));
		entityList.add(new Bub(TILE*72, TILE*8, Mook.Type.WALK));
		entityList.add(new Bub(TILE*77, TILE*8, Mook.Type.WALK));
		entityList.add(new Mook(TILE*70, TILE*3, Mook.Type.WALK));
		entityList.add(new Mook(TILE*74, TILE*3, Mook.Type.WALK));
		entityList.add(new Mook(TILE*28, TILE*15, Mook.Type.WALK));
		setup();
	}
}
