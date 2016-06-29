package entities;

import java.util.List;

import main.Bana;
import maps.Room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Hero extends Entity{

	// CLASS PURPOSE: The hero. Only one should exist at a time.

	private boolean flag_INTERACT, flag_LANDEDHIT;
	private Timer attackTimer = new Timer(25);
	private Sprite jumpImage = new Sprite(new Texture(Gdx.files.internal("sprites/herojump.PNG")));
	private Sprite attackImage = new Sprite(new Texture(Gdx.files.internal("sprites/herokick.PNG")));
	private Sprite hurtImage = new Sprite(new Texture(Gdx.files.internal("sprites/herohurt.PNG")));
	private Animation walk = makeAnimation("sprites/herowalksheet.PNG", 4, 1, 8f, PlayMode.LOOP);
	private Sprite standImage = new Sprite(walk.getKeyFrame(0));
	private Sound jump = Gdx.audio.newSound(Gdx.files.internal("sfx/jump.mp3"));
	private Sound doublejump = Gdx.audio.newSound(Gdx.files.internal("sfx/djump.mp3"));
	private Sound attack = Gdx.audio.newSound(Gdx.files.internal("sfx/attack5.mp3"));
	private Hitbox kneeHitbox;
	private float kickHitboxWidth = 10;
	private int wallet;

	public Hero(float x, float y){
		super(x, y);
		stunTimer = new Timer(20);
		invincibleTimer = new Timer(25);
		facing = Facing.RIGHT;
		maxHealth = 8;
		acceleration = 1f;
		health = maxHealth;
		setImage(standImage);
		hurt = Gdx.audio.newSound(Gdx.files.internal("sfx/herohurt.mp3"));
		timerList.add(invincibleTimer);
		timerList.add(attackTimer);
		timerList.add(stunTimer);
		jumpStrength = 9f;
		kneeHitbox = new Hitbox(image.getWidth()-8, 8, kickHitboxWidth, image.getHeight());
		hitboxList.add(kneeHitbox);
	}

	public void updateImage(){
		super.updateImage();
		if (!stunTimer.stopped()) changeImage(hurtImage);
		else if (!attackTimer.stopped()) changeImage(attackImage);
		else if (state == State.JUMP || state == State.DOUBLEJUMP || isFalling()) changeImage(jumpImage);
		else if (flag_GOLEFT || flag_GORIGHT) changeImage(walk.getKeyFrame(deltaTime));
		else changeImage(standImage);
	}

	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, Room room, int deltaTime){
		checkTouch(entityList);
		super.updateVelocity(mapRectangleList, entityList, room, deltaTime);
	}

	private void checkTouch(List<Entity> entityList) {
		for (Entity en: entityList){
			checkKnee(en);
			checkTrounce(en);
			checkHit(en);
		}
		if (invincibleTimer.stopped()) stopInvincible();
		if (attackTimer.stopped()) kneeHitbox.deactivate();
		flag_LANDEDHIT = false;
	}
	private boolean canTrounceEnemy(Entity en){
		if ((position.y - en.getPosition().y) > en.getImage().getHeight()*.75f) return true;
		return false;
	}
	private Rectangle getNextRectangle(){
		Rectangle thisR = image.getBoundingRectangle();
		thisR.setX(thisR.getX() + velocity.x);
		thisR.setY(thisR.getY() + velocity.y);
		return thisR;
	}
	private void checkKnee(Entity en){
		final boolean kneeIntersects = (kneeHitbox.isActive() && Intersector.overlaps(en.getImage().getBoundingRectangle(), kneeHitbox.getRectangle()));
		if (kneeIntersects && en.canBeAttacked()){
			flag_LANDEDHIT = true;
			en.takeDamage(this);
		}
	}
	private void checkTrounce(Entity en){
		if (!flag_LANDEDHIT && Intersector.overlaps(en.getImage().getBoundingRectangle(), getNextRectangle())){
			if (canTrounceEnemy(en) && en.canBeTrounced()) {
				flag_LANDEDHIT = true;
				if (en.isHurthead()) this.takeDamage(en);
				else if (!en.isRockhead()) en.takeDamage(this);
				state = State.JUMP;
			}
		}
	}
	private void checkHit(Entity en){
		if (!flag_LANDEDHIT && Intersector.overlaps(en.getImage().getBoundingRectangle(), image.getBoundingRectangle())){
			if (en.isEnemy() && !flag_LANDEDHIT && en.invincibleTimer.stopped()) takeDamage(en);
		}
	}

	void takeDamage(Entity en){
		super.takeDamage(en);
		if (en.damage > 0 && invincibleTimer.stopped()) {
			invincibleTimer.set();
			image.setAlpha(0.6f);
		}
		else if (state == State.DOUBLEJUMP) state = State.JUMP;
	}
	private void stopInvincible(){
		image.setAlpha(1f);
	}

	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.A:  flag_GOLEFT  = false; break;
		case Keys.D:  flag_GORIGHT = false; break;
		case Keys.SHIFT_RIGHT:  break;
		case Keys.ENTER: flag_INTERACT = false; break;
		default: break;
		}
		return false;
	}
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.A: pressLeft();  break;
		case Keys.D: pressRight(); break;
		case Keys.SHIFT_RIGHT: if (stunTimer.stopped()) pressAction(); break;
		case Keys.ENTER: if (stunTimer.stopped()) pressAttack(); break;
		default: break;
		}
		return false;
	}
	private void pressLeft(){
		flag_GOLEFT  = true;
		flag_GORIGHT = false;
		if (facing == Facing.RIGHT) {
			kneeHitbox.setDisplacementX(-kneeHitbox.getRectangle().width);
			image.flip(true, false);
		}
		facing = Facing.LEFT;
	}
	private void pressRight(){
		flag_GORIGHT = true; 
		flag_GOLEFT = false;
		if (facing == Facing.LEFT) {
			kneeHitbox.setDisplacementX(image.getWidth());
			image.flip(true, false);
		}
		facing = Facing.RIGHT;
	}
	private void pressAction(){
		if ((state == State.JUMP || isFalling()) && state != State.DOUBLEJUMP) { // goes first to avoid being called simultaneously w/ jump
			doubleJump(); 
			return; // prevents jump from being called afterwards
		}
		if (state != State.JUMP && state != State.DOUBLEJUMP && !isFalling()) {
			jump.play(Bana.getVolume());
			jump(); 
		}
	}
	private void doubleJump(){
		doublejump.play(Bana.getVolume());
		state = State.DOUBLEJUMP;
		velocity.y = 3f*jumpStrength/4f;
	}
	private void pressAttack(){
		if (attackTimer.stopped()) {
			attack();
			attackTimer.set();
		}
		flag_INTERACT = true;
	}
	private void attack(){
		attack.play(Bana.getVolume()); // TODO: Don't play when going through doors
		int kickSpeed = 8;
		if (state == State.GROUND) velocity.y += 4;
		if (facing == Facing.RIGHT) velocity.x += kickSpeed;
		if (facing == Facing.LEFT) velocity.x += -kickSpeed;
		if (state != State.DOUBLEJUMP) state = State.JUMP;
		kneeHitbox.activate();
	}

	public void moveToStart(Vector2 vector){
		stopInvincible();
		resetTimers();
		attack.stop();
		position.x = vector.x;
		position.y = vector.y;
		center();
		updateImage();
		kneeHitbox.set();
	}

	void setTiny(){
		image.setSize(8, 12);
		jumpStrength = 7f;
		fallSpeed = 0.3f;
		// make attack hitbox smaller
		// speed up sfx
	}
	void setHuge(){
		image.setSize(40, 90);
		damage = 4;
		jumpStrength = 12f;
		fallSpeed = 0.7f;
		// make attack hitbox bigger
		// slow down sfx
	}

	public void stop(){
		super.stop();
		flag_GOLEFT = false;
		flag_GORIGHT = false;
		resetTimers();
		state = State.GROUND;
		flag_INTERACT = false;
	}
	public void reset(){
		stop();
		if (facing == Facing.LEFT){
			facing = Facing.RIGHT;
			image.flip(true, false);
			kneeHitbox.setDisplacementX(image.getWidth());
		}
		health = maxHealth;
	}
	public boolean isInteracting(){ return flag_INTERACT; }
	public int getWallet() { return wallet; }
	public void addMoney(int money){ wallet += money; }
	public void emptyWallet(){ wallet = 0; }

	enum Forms{
		NORMAL, // standard state
		FLY, // can fly (no double jump limiter)
		HONEY, // covered in honey. Slow movement, distracts bees & bears.
		GIANT, // very big. can smash through hard blocks
		TINY, // very small. Lower gravity and can fit into tiny areas.
	}
}
