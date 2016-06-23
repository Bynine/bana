package maps;

import java.util.ArrayList;
import java.util.List;

import main.Bana;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import entities.Entity;
import entities.Hero;

public abstract class Room {
	TiledMap map;
	MapObjects mapObjects;
	TmxMapLoader tmxMapLoader = new TmxMapLoader();
	List<Rectangle> rectangleList = new ArrayList<>();
	List<Entity> entityList = new ArrayList<>();
	Vector2 startPosition = new Vector2(); // TODO: change start position depending on the door you went into to get there
	float gravity = 1f;
	float wind = 0f; // TODO: implement wind (?) based on density (??)
	Music roomMusic;
	Level superLevel;
	static final int TILE = Bana.TILE;
	public Room(Level superLevel){
		this.superLevel = superLevel;
	}
	void setup(){
		mapObjects = map.getLayers().get(map.getLayers().getCount()-1).getObjects(); // gets the top layer
		for(RectangleMapObject mapObject: mapObjects.getByType(RectangleMapObject.class)){
			rectangleList.add(mapObject.getRectangle());
		}
		roomMusic.setVolume(Bana.getVolume());
	}
	public TiledMap getMap(){
		return map;
	}
	public List<Rectangle> getRectangleList(){
		return rectangleList;
	}
	public List<Entity> getEntityList(){
		return entityList;
	}
	public Vector2 getStartPosition(){
		return startPosition;
	}
	public float getGravity(){
		return gravity;
	}
	public void stopMusic(){
		roomMusic.stop();
	}
	public Music getMusic(){
		return roomMusic;
	}

	public void initEntities(Hero hero){
		// TODO: Change the way this works such that all things are declared when a level is first made, and most things return a new version of themselves
		// upon completion, except for one-time collectibles like coins
		clearOut();
		if (entityList.contains(hero)) entityList.remove(hero);
		entityList.add(hero);
		roomMusic.play();
	}
	void clearOut(){
		rectangleList.clear();
		entityList.clear();
	}
	void setStartPosition(float x, float y){
		startPosition.x = x;
		startPosition.y = y;
	}
	public void removeEntity(Entity en){
		getEntityList().remove(en);
		getRectangleList().remove(en.getImage().getBoundingRectangle());
	}
}
