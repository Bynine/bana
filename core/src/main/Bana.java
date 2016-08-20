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

import entities.Entity;
import entities.Hero;

public final class Bana extends ApplicationAdapter implements InputProcessor{

	final static int SCREEN = 2;
	private static float volume = 0.0f;
	private void startGame(){
		state = State.GAME;
		activeLevel = new Level_Test();
		Room startingRoom = activeLevel.getRoom(0);
		hero = new Hero(startingRoom.getStartPosition().x, startingRoom.getStartPosition().y, this);
		changeRoom(startingRoom, startingRoom.getStartPosition(), true);
		Gdx.gl.glClearColor(222f/256f, 238f/256f, 214f/256f, 213f/256f); // 16 color palette's lightest color
	}
	private final OrthographicCamera cam = new OrthographicCamera();
	public static final int SCREENWIDTH  = 480*SCREEN;
	public static final int SCREENHEIGHT = 320*SCREEN;
	public static final int TILE = 32;
	public static final int MAXWALLET = 100;
	private final int camAdjustmentSpeed = 16;
	private int mapWidth;
	private int mapHeight;
	private int deltaTime;
	private SpriteBatch batch;
	private State state;
	private Hero hero;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private Level activeLevel;
	private Room activeRoom;
	private GUI gui;
	private final List<Rectangle> rectangleList = new ArrayList<>();
	private boolean flag_PAUSE;
	
	@Override
	public void create () {
		gui = new GUI();
		batch = new SpriteBatch();
		cam.setToOrtho(false, SCREENWIDTH/SCREEN, SCREENHEIGHT/SCREEN);
		Gdx.input.setInputProcessor(this);
		startGame();
	}
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch(state){
		case GAME:
			if (!flag_PAUSE) updateEntities(activeRoom.getEntityList(), deltaTime);
			updateCamera();
			drawGraphics(activeRoom.getEntityList());
			if (!flag_PAUSE) gui.drawGUI(batch, hero, cam, activeLevel);
			if (!flag_PAUSE) deltaTime++;
			break;
		case MENU: break;
		default: break;
		}
	}

	private void updateEntities(List<Entity> entityList, int deltaTime){
		for (Entity en : entityList){
			if (en.isOutOfBounds(mapWidth) || en.isDestroyed()){
				if (en.getClass() == Hero.class) { restartLevel(); break; }
				else { activeRoom.removeEntity(en); break; }
			}
			if (hero.getHealth() <= 0) {
				restartLevel();
				break;
			}
			en.reactToHero(hero);
			for (Entity subEn: entityList) if (en != subEn) en.reactToAll(subEn);
			en.updateVelocity(rectangleList, entityList, activeRoom, deltaTime);
			en.updatePosition();
			en.updateImage();
		}
		rectangleList.clear();
		rectangleList.addAll(activeRoom.getRectangleList());
	}

	private void updateCamera(){
		moveCamTowardHero();
		cam.position.x = MathUtils.round(MathUtils.clamp(cam.position.x, SCREENWIDTH/(2*SCREEN),  mapWidth -(SCREENWIDTH/(2*SCREEN))));
		cam.position.y = MathUtils.round(MathUtils.clamp(cam.position.y, SCREENHEIGHT/(2*SCREEN), mapHeight-(SCREENHEIGHT/(2*SCREEN))));
		cam.update();
		renderer.setView(cam);
	}
	private void moveCamTowardHero(){
		cam.position.x = (cam.position.x*(camAdjustmentSpeed-1)+hero.getPosition().x)/camAdjustmentSpeed;
		cam.position.y = (cam.position.y*(camAdjustmentSpeed-1)+hero.getPosition().y)/camAdjustmentSpeed;
	}
	private void drawGraphics(List<Entity> entityList){
		batch.setProjectionMatrix(cam.combined);
		int[] arr = new int[map.getLayers().getCount()-2]; // all background layers
		for (int i = 0; i < arr.length; ++i){
			arr[i] = i;
		}
		renderer.render(arr);
		batch.begin();
		for (Entity en : entityList){
			en.getImage().draw(batch);
		}
		hero.getImage().draw(batch); // makes sure hero is in front of all other objects
		batch.end();
		arr = new int[]{map.getLayers().getCount()-2};
		renderer.render(arr); // foreground layer
		batch.begin();
		for (Entity en : entityList){
			if (en.isDead()) en.getImage().draw(batch); // draws dead enemies in the foremostground
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
		hero.emptyWallet();
		try { activeLevel = activeLevel.getClass().newInstance(); }
		catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }
		changeRoom(activeLevel.getRoom(0), activeLevel.getRoom(0).getStartPosition(), true);
	}

	public void changeRoom (Room room, Vector2 vector, boolean resetCamera) {
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
	}

	public static float getVolume(){
		return volume;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (!flag_PAUSE && state == State.GAME) hero.keyDown(keycode);
		if (state == State.MENU && keycode == Keys.ENTER) startGame();
		switch(keycode){
		case Keys.SHIFT_LEFT: if (state == State.GAME) pauseGame(); break;
		default: break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (!flag_PAUSE && state == State.GAME) hero.keyUp(keycode);
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