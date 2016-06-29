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

import entities.Collectible;
import entities.Entity;
import entities.Hero;

public abstract class Room {
	TiledMap map;
	MapObjects mapObjects;
	final TmxMapLoader tmxMapLoader = new TmxMapLoader();
	final List<Rectangle> rectangleList = new ArrayList<>();
	final List<Entity> entityList = new ArrayList<>();
	final Vector2 startPosition = new Vector2();
	float gravity = 1f;
	float wind = 0f;
	Music roomMusic;
	final Level superLevel;
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
	public float getWind(){
		return wind;
	}
	public void stopMusic(){
		roomMusic.stop();
	}
	public Music getMusic(){
		return roomMusic;
	}

	public void initEntities(Hero hero){
		clearOut();
		if (entityList.contains(hero)) entityList.remove(hero);
		entityList.add(hero);
		roomMusic.setLooping(true);
		roomMusic.play();
	}
	void clearOut(){
		rectangleList.clear();
		entityList.clear();
	}
	void addObjects(List<Collectible> collectibleList){
		for (Collectible c: collectibleList) if (!c.isDead() && !c.isDestroyed()) entityList.add(c);
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
