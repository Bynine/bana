package main;

import main.Bana;
import maps.Level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Hero;

public class GUI {
	// GUI Sprites
	private static BitmapFont font;
	private final Sprite heart = new Sprite(new Texture(Gdx.files.internal("sprites/heart.PNG")));
	private final Sprite emptyHeart = new Sprite(new Texture(Gdx.files.internal("sprites/heartEmpty.PNG")));
	private Texture noTexture = new Texture(Gdx.files.internal("sprites/invisibleTile.PNG"));
	private Sprite levelArtifactSprite = new Sprite(noTexture);
	
	GUI(){
		font = new BitmapFont();
		font.setColor(Color.BLACK);
	}

	@SuppressWarnings("static-access")
	void drawGUI(SpriteBatch batch, int money, Hero hero, Camera cam, Level level) {
		if (level.getArtifactObtained()) levelArtifactSprite.setTexture(level.getArtifact().getImage().getTexture());
		else levelArtifactSprite.setTexture(noTexture);
		float leftmost = cam.position.x + Bana.TILE/2 - heart.getWidth() - Bana.SCREENWIDTH/(2*Bana.SCREEN);
		float upmost = cam.position.y - Bana.TILE/2 - heart.getHeight() + Bana.SCREENHEIGHT/(2*Bana.SCREEN);
		heart.setPosition(leftmost, upmost);
		batch.begin();
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
		font.draw(batch, money + "", leftmost+Bana.TILE/2, upmost-Bana.TILE/2);
		levelArtifactSprite.setPosition(cam.position.x + Bana.SCREENWIDTH/(2*Bana.SCREEN) - Bana.TILE*1.5f, upmost-Bana.TILE/2);
		levelArtifactSprite.draw(batch);
		batch.end();
	}
}
