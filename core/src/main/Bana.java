package main;

import java.util.ArrayList;
import java.util.List;

import maps.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import entities.Coin;
import entities.Door;
import entities.Entity;
import entities.Food;
import entities.Hero;

public final class Bana extends ApplicationAdapter implements InputProcessor{

	// CLASS PURPOSE: The game's engine. Starts the game, then updates objects over time.
	final static int SCREEN = 2;
	private static float volume = 0.1f;
	@Override
	public void create () {
		gui = new GUI();
		batch = new SpriteBatch();
		cam.setToOrtho(false, SCREENWIDTH/SCREEN, SCREENHEIGHT/SCREEN);
		state = State.GAME;
		activeLevel = new Level_Oasis();
		Room startingRoom = activeLevel.getRoom(0);
		hero = new Hero(startingRoom.getStartPosition().x, startingRoom.getStartPosition().y);
		changeRoom(startingRoom, startingRoom.getStartPosition(), true);
		restartLevel();
		Gdx.gl.glClearColor(222f/256f, 238f/256f, 214f/256f, 213f/256f); // 16 color palette's lightest color
		Gdx.input.setInputProcessor(this);
	}
	private final OrthographicCamera cam = new OrthographicCamera();
	public static final int SCREENWIDTH  = 480*SCREEN;
	public static final int SCREENHEIGHT = 320*SCREEN;
	public static final int TILE = 32;
	private final int camAdjustmentSpeed = 16;
	private int mapWidth;
	private int mapHeight;
	private int deltaTime;
	private SpriteBatch batch;
	private static State state;
	private Hero hero;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private Level activeLevel;
	private Room activeRoom;
	private GUI gui;
	private final List<Rectangle> rectangleList = new ArrayList<>();
	private final List<Door> activeDoors = new ArrayList<>();

	private int localMoney = 0;
	@SuppressWarnings("unused")
	private long totalMoney = 0; // TODO: implement adding local money to total
	private boolean flag_PAUSE;

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (state == State.GAME){
			if (!flag_PAUSE) updateEntities(activeRoom.getEntityList(), deltaTime);
			updateCamera();
			drawEntities(activeRoom.getEntityList());
			if (!flag_PAUSE) gui.drawGUI(batch, localMoney, hero, cam, activeLevel);
			if (!flag_PAUSE) deltaTime++;
		}
	}

	private void updateEntities(List<Entity> entityList, int deltaTime){
		checkForDoors();
		for (Entity en : entityList){
			if (en.getHealth() <= 0){
				if (en.getClass() == Hero.class) {
					restartLevel();
					break;
				}
				else en.die();
			}
			if (en.isOutOfBounds(mapWidth)){
				if (en.getClass() == Hero.class) {
					restartLevel();
					break;
				}
				else {
					activeRoom.removeEntity(en);
					break;
				}
			}
			if (en.isDestroyed()) {
				activeRoom.removeEntity(en); 
				break;
			}
			reactToHero(en);
			for (Entity subEn: entityList) if (en != subEn) en.reactToAll(subEn);
			en.updateVelocity(rectangleList, entityList, activeRoom.getGravity(), deltaTime);
			en.updatePosition();
			en.updateImage();
		}
		rectangleList.clear();
		rectangleList.addAll(activeRoom.getRectangleList());
	}
	private void reactToHero(Entity en) {
		if (en.getClass() == entities.Coin.class && en.isTouching(hero)) {
			Coin coin = (Coin)en;
			localMoney += coin.getValue();
		}
		if (en.getClass() == entities.Food.class && en.isTouching(hero)) {
			Food food = (Food)en;
			hero.addHealth(food.getValue());
		}
		if (en.getClass() == entities.Artifact.class && en.isTouching(hero)) {
			activeLevel.setArtifactObtained();
		}
		en.reactToHero(hero);
	}

	private void checkForDoors(){
		for (Door door: activeDoors) if (hero.isOverlapping(door) && hero.isInteracting()) {
			changeRoom(door.getRoom(), door.getDestination(), true);
			break;
		}
	}

	private void updateCamera(){
		moveCamTowardHero();
		cam.position.x = MathUtils.round(MathUtils.clamp(cam.position.x, SCREENWIDTH/(2*SCREEN),  mapWidth -(SCREENWIDTH/(2*SCREEN))));
		cam.position.y = MathUtils.round(MathUtils.clamp(cam.position.y, SCREENHEIGHT/(2*SCREEN), mapHeight-(SCREENHEIGHT/(2*SCREEN))));
		cam.update();
		renderer.setView(cam);
		int[] arr = new int[map.getLayers().getCount()-2]; // all background layers
		for (int i = 0; i < arr.length; ++i){
			arr[i] = i;
		}
		renderer.render(arr);
	}
	private void moveCamTowardHero(){
		cam.position.x = (cam.position.x*(camAdjustmentSpeed-1)+hero.getPosition().x)/camAdjustmentSpeed;
		cam.position.y = (cam.position.y*(camAdjustmentSpeed-1)+hero.getPosition().y)/camAdjustmentSpeed;
	}
	private void drawEntities(List<Entity> entityList){
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		for (Entity en : entityList){
			en.getImage().draw(batch);
		}
		hero.getImage().draw(batch); // makes sure hero is in front of all other objects
		batch.end();
		int[] arr = new int[]{map.getLayers().getCount()-2};
		renderer.render(arr); // foreground layer
		batch.begin();
		for (Entity en : entityList){
			if (en.isDead()) en.getImage().draw(batch); // draws dead enemies in the foreground
		}
		batch.end();
	}
	private void pauseGame(){
		if (flag_PAUSE) flag_PAUSE = false;
		else {
			hero.stop();
			flag_PAUSE = true;
		}
	}

	private void restartLevel(){
		hero.reset();
		localMoney = 0;
		activeLevel = new Level_Oasis();
		changeRoom(activeLevel.getStartRoom(), activeLevel.getStartRoom().getStartPosition(), true);
	}

	private void changeRoom (Room room, Vector2 vector, boolean resetCamera) {
		deltaTime = 0; // resets the timer for all movement based time things
		hero.stop();
		if (activeRoom != null && activeRoom.getMusic() != room.getMusic()) activeRoom.stopMusic();
		activeRoom = room;
		map = activeRoom.getMap();
		activeRoom.initEntities(hero);
		hero.moveToStart(vector);
		if (resetCamera){
			cam.position.x = hero.getPosition().x;
			cam.position.y = hero.getPosition().y;
			cam.update();
		}
		renderer = new OrthogonalTiledMapRenderer(map, 1);
		renderer.setView(cam);
		mapWidth  = map.getProperties().get("width",  Integer.class)*TILE;
		mapHeight = map.getProperties().get("height", Integer.class)*TILE;
		rectangleList.clear();
		rectangleList.addAll(activeRoom.getRectangleList());
		activeDoors.clear();
		for (Entity en: activeRoom.getEntityList()){
			if (en.getClass() == Door.class) activeDoors.add((Door)en);
		}
	}

	public static float getVolume(){
		return volume;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (!flag_PAUSE && state == State.GAME) hero.keyDown(keycode);
		switch(keycode){
		case Keys.SHIFT_LEFT: pauseGame(); break;
		default: break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (!flag_PAUSE) hero.keyUp(keycode);
		return false;
	}

	private enum State{
		GAME, MAP, MENU
	}

	public boolean keyTyped(char character) {return false;}
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;	}
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	public boolean scrolled(int amount) {return false;}
}