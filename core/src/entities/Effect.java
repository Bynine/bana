package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import entities.Entity.Timer;

public class Effect {
	private Sprite image;
	Timer timer;
	
	Effect(String path, float x, float y, Timer timer){
		image = new Sprite(new Texture(Gdx.files.internal(path)));
		image.setX(x);
		image.setY(y);
		this.timer = timer;
	}
	
	public Sprite getSprite(){
		return image;
	}
	
	public Timer getDrawTime(){
		return timer;
	}

	public void setTimer() {
		timer.set();
	}
	
	public boolean isDone(){
		return timer.stopped();
	}
}
