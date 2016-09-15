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

	private boolean flag_INTERACT, flag_LANDEDHIT;
	private Timer attackTimer = new Timer(20);
	private Timer slideTimer = new Timer(20);
	Sprite jumpImage = new Sprite(new Texture(Gdx.files.internal("sprites/herojump.PNG")));
	Sprite attackImage = new Sprite(new Texture(Gdx.files.internal("sprites/herokick.PNG")));
	Sprite hurtImage = new Sprite(new Texture(Gdx.files.internal("sprites/herohurt.PNG")));
	Sprite slideImage = new Sprite(new Texture(Gdx.files.internal("sprites/slide.PNG")));
	Animation walk = makeAnimation("sprites/herowalksheet.PNG", 4, 1, 8f, PlayMode.LOOP);
	Sprite standImage = new Sprite(walk.getKeyFrame(0));
	private Sound jump = Gdx.audio.newSound(Gdx.files.internal("sfx/jump.mp3"));
	private Sound doublejump = Gdx.audio.newSound(Gdx.files.internal("sfx/djump.mp3"));
	private Sound attack = Gdx.audio.newSound(Gdx.files.internal("sfx/attack5.mp3"));
	private Hitbox kneeHitbox;
	private Wallet wallet;
	private Bana bana;
	private int heldInput, currentInput;

	public Hero(float x, float y, Bana bana){
		super(x, y);
		this.bana = bana;
		wallet = new Wallet();
		stunTimer = new Timer(16);
		invincibleTimer = new Timer(24);
		facing = Facing.RIGHT;
		maxHealth = 8;
		acceleration = 1f;
		health = maxHealth;
		setImage(standImage);
		hurt = Gdx.audio.newSound(Gdx.files.internal("sfx/herohurt.mp3"));
		timerList.add(invincibleTimer);
		timerList.add(attackTimer);
		timerList.add(stunTimer);
		timerList.add(slideTimer);
		jumpStrength = 10f;
		kneeHitbox = new Hitbox(image.getWidth()-8, 8, 10, image.getHeight());
		hitboxList.add(kneeHitbox);
	}

	public void updateImage(){
		super.updateImage();
		if (!stunTimer.stopped()) changeImage(hurtImage);
		if (!slideTimer.stopped()) changeImage(slideImage);
		else if (!attackTimer.stopped()) changeImage(attackImage);
		else if (state == State.JUMP || state == State.DOUBLEJUMP || isFalling()) changeImage(jumpImage);
		else if (flag_GOLEFT || flag_GORIGHT) changeImage(walk.getKeyFrame(deltaTime));
		else changeImage(standImage);
	}

	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, Room room, int deltaTime){
		checkHeldInput();
		checkTouch(entityList);
		super.updateVelocity(mapRectangleList, entityList, room, deltaTime);
	}
	
	private void checkHeldInput(){
		if (heldInput == currentInput && canExecuteInput()){
			keyDown(heldInput);
			heldInput = 0;
		}
	}
	private boolean canExecuteInput(){
		return slideTimer.stopped();
	}
	
	@Override
	void updateSpeed(){
		if (!slideTimer.stopped()) {
			final double slideSpeed = 1;
			if (facing == Facing.RIGHT) velocity.x += slideSpeed;
			else			            velocity.x -= slideSpeed;
			return;
		}
		super.updateSpeed();
	}

	private void checkTouch(List<Entity> entityList) {
		for (Entity en: entityList){
			checkDoor(en);
			checkKnee(en);
			checkTrounce(en);
			checkHit(en);
		}
		if (invincibleTimer.stopped()) stopInvincible();
		if (attackTimer.stopped()) kneeHitbox.deactivate();
		flag_LANDEDHIT = false;
	}
	private void checkDoor(Entity en){
		if (en.getClass() == Door.class && this.isOverlapping(en) && flag_INTERACT){
			Door door = (Door) en;
			bana.changeRoom(door.getRoom(), door.getDestination(), true);
		}
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
	
	@Override
	void takeKnockback(Entity en, float x, float y, boolean done){
		super.takeKnockback(en, x, y, done);
		slideTimer.reset();
	}

	public boolean keyUp(int keycode) {
		if (currentInput == keycode) currentInput = 0;
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
		currentInput = keycode;
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
		if (!slideTimer.stopped()) {
			holdInput(Keys.A); return;
		}
		flag_GOLEFT  = true;
		flag_GORIGHT = false;
		if (facing == Facing.RIGHT) {
			kneeHitbox.setDisplacementX(-kneeHitbox.getRectangle().width);
			image.flip(true, false);
		}
		facing = Facing.LEFT;
	}
	private void pressRight(){
		if (!slideTimer.stopped()) {
			holdInput(Keys.D); return;
		}
		flag_GORIGHT = true; 
		flag_GOLEFT = false;
		if (facing == Facing.LEFT) {
			kneeHitbox.setDisplacementX(image.getWidth());
			image.flip(true, false);
		}
		facing = Facing.RIGHT;
	}
	private void pressAction(){
		if ((state == State.JUMP || (isFalling() && state != State.DOUBLEJUMP))) { // goes first to avoid being called simultaneously w/ jump
			doubleJump();
			return; // prevents jump from being called afterwards
		}
		if (state == State.GROUND) {
			jump.play(Bana.getVolume());
			jump(); 
		}
	}
	private void holdInput(int keycode){
		heldInput = keycode;
	}
	private void doubleJump(){
		doublejump.play(Bana.getVolume());
		state = State.DOUBLEJUMP;
		velocity.y = 3f*jumpStrength/4f;
	}
	private void pressAttack(){
		if (attackTimer.stopped()) {
			attackTimer.set();
			if (state == State.GROUND) groundAttack();
			else airAttack();
		}
		flag_INTERACT = true;
	}
	private void groundAttack(){
		slideTimer.set();
		int slideSpeed = 10;
		if (facing == Facing.RIGHT) velocity.x += slideSpeed;
		else velocity.x -= slideSpeed;
		kneeHitbox.activate();
	}
	private void airAttack(){
		int kneeSpeed = 10;
		if (facing == Facing.RIGHT) velocity.x += kneeSpeed;
		else velocity.x -= kneeSpeed;
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
	
	public Wallet getWallet(){ return wallet; }
	
	public boolean isInteracting(){ return flag_INTERACT; }
}
