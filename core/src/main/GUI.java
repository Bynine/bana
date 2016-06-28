package main;

import main.Bana;
import maps.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Hero;

public class GUI {
	private BitmapFont fontNormal;
	private BitmapFont fontSpecial;
	private final Sprite heart = new Sprite(new Texture(Gdx.files.internal("sprites/heart.PNG")));
	private final Sprite emptyHeart = new Sprite(new Texture(Gdx.files.internal("sprites/heartEmpty.PNG")));
	private final Sprite coin = new Sprite(new Texture(Gdx.files.internal("sprites/money.PNG")));
	private Texture noTexture = new Texture(Gdx.files.internal("sprites/invisibleTile.PNG"));
	private Sprite levelArtifactSprite = new Sprite(noTexture);
	private final int TILE = Bana.TILE;
	private final int SCREEN = Bana.SCREEN;
	private final int SCREENWIDTH = Bana.SCREENWIDTH;
	private final int SCREENHEIGHT = Bana.SCREENHEIGHT;
	private final int MAXWALLET = Bana.MAXWALLET;
	private float leftmost, upmost;
	private Batch batch;
	
	GUI(){
		fontNormal = new BitmapFont();
		fontNormal.setColor(Color.WHITE);
		fontSpecial = new BitmapFont();
		fontSpecial.setColor(Color.YELLOW);
	}

	void drawGUI(SpriteBatch batch, Hero hero, Camera cam, Level level) {
		this.batch = batch;
		leftmost = cam.position.x - SCREENWIDTH/(2*SCREEN);
		upmost = cam.position.y - TILE + SCREENHEIGHT/(2*SCREEN);
		batch.begin();
		drawArtifact(level);
		drawHealth(hero);
		drawMoney(hero.getWallet());
		batch.end();
	}
	
	private void drawHealth(Hero hero){
		heart.setPosition(leftmost, upmost);
		int i = 0;
		for (i = 0; i < hero.getHealth(); ++i){
			heart.setPosition(heart.getX()+(heart.getWidth()), heart.getY());
			heart.draw(batch);
		}
		emptyHeart.setX(heart.getX());
		for (int j = i; j < hero.getMaxHealth(); ++j){
			emptyHeart.setPosition(emptyHeart.getX()+(heart.getWidth()), heart.getY());
			emptyHeart.draw(batch);
		}
	}
	
	private void drawMoney(int money){
		coin.setPosition(leftmost+TILE/2, upmost-TILE/2);
		coin.draw(batch);
		float moneyPosX = leftmost+TILE*17f/16f;
		float moneyPosY = upmost-TILE/16;
		if (money < MAXWALLET) fontNormal.draw(batch, money + "", moneyPosX, moneyPosY);
		else fontSpecial.draw(batch, money + "", moneyPosX, moneyPosY);
	}
	
	@SuppressWarnings("static-access")
	private void drawArtifact(Level level){
		if (level.getArtifactObtained()) levelArtifactSprite.setTexture(level.getArtifact().getImage().getTexture());
		else levelArtifactSprite.setTexture(noTexture);
		levelArtifactSprite.setPosition(leftmost + SCREENWIDTH/SCREEN - TILE*1.5f, upmost-TILE/2);
		levelArtifactSprite.draw(batch);
	}
}
