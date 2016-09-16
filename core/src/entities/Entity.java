package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.Bana;
import maps.Room;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	final Vector2 position = new Vector2();
	final Vector2 velocity = new Vector2();
	float acceleration = 1.2f;
	float airacceleration = .7f;
	float maxSpeed = 15f; // TODO: fix for various enemies who have it lowered to modify their speed
	float friction = 0.8f;
	float fallSpeed = 0.6f;
	float density = 1;
	float jumpStrength = 10f;
	float terminalVelocity = -10f;
	float knockback = 1;
	float bounce = 1;
	int maxHealth = 1;
	int health = 1;
	int damage = 1;
	int deltaTime = 0;
	Collision collision;
	boolean flag_GOLEFT, flag_GORIGHT, flag_TODESTROY, flag_DEAD, flag_HITGROUND, flag_GROUNDED;
	boolean is_ENEMY, is_IMMUNE, does_MOVE, is_DESTRUCTABLE, is_ROCKHEAD, is_HURTHEAD;
	Facing facing;
	State state;
	Sound hurt, bump;
	Sprite image, corpse;
	Timer invincibleTimer = new Timer(20);
	Timer stunTimer = new Timer(30);
	final List<Timer> timerList = new ArrayList<>();
	final List<Hitbox> hitboxList = new ArrayList<>();
	private final List<Effect> effects = new ArrayList<>();

	protected final int collisionCheck = 4;
	protected final float softening = .8f;
	public static final int TILE = Bana.TILE;
	final List<Rectangle> tempRectangleList = new ArrayList<Rectangle>();

	Entity(float x, float y){
		collision = Collision.CREATURE;
		hurt = Gdx.audio.newSound(Gdx.files.internal("sfx/hurt.mp3"));
		bump = Gdx.audio.newSound(Gdx.files.internal("sfx/bump2.mp3")); 
		does_MOVE = true;
		position.x = x;
		position.y = y;
		timerList.addAll(Arrays.asList(invincibleTimer, stunTimer));
	}

	public void updateImage(){
		image.setX(position.x);
		image.setY(position.y);
	}

	public void updatePosition(){
		if (health <= 0 && this.getClass() != Hero.class && !flag_DEAD) die();
		if (state == State.STAND || state == State.CROUCH) flag_GROUNDED = true;
		else flag_GROUNDED = false;
		if (does_MOVE){
			position.x += MathUtils.clamp(velocity.x, -maxSpeed, maxSpeed);
			position.y += velocity.y;
		}
		for (Hitbox hitbox: hitboxList){
			hitbox.update();
		}
		image.setX(position.x);
		image.setY(position.y);
	}

	public void updateVelocity(List<Rectangle> mapRectangleList, List<Entity> entityList, Room room, int deltaTime){
		this.deltaTime = deltaTime;
		if (stunTimer.stopped()) updateSpeed();
		limitingForces(mapRectangleList, entityList, room);
	}
	void updateSpeed(){
		if (flag_GROUNDED){
			if (state == State.STAND){
				if (flag_GOLEFT)  velocity.x-=acceleration;
				if (flag_GORIGHT) velocity.x+=acceleration;
			}
			else{
				if (flag_GOLEFT)  velocity.x-=(acceleration/4);
				if (flag_GORIGHT) velocity.x+=(acceleration/4);
			}
		}
		else{
			if (flag_GOLEFT)  velocity.x-=airacceleration;
			if (flag_GORIGHT) velocity.x+=airacceleration;
		}
	}
	void limitingForces(List<Rectangle> mapRectangleList, List<Entity> entityList, Room room){
		actWind(room.getWind());
		actGravity(room.getGravity());
		setupRectangles(mapRectangleList, entityList);
		checkWalls(tempRectangleList);
		checkGrounded(tempRectangleList);
		checkDiagonal(tempRectangleList);
		friction();
	}
	void friction(){
		if (Math.abs(velocity.x) < 0.1) velocity.x = 0;
		if (Math.abs(velocity.y) < 0.1) velocity.y = 0;
		if (flag_GROUNDED) velocity.x*=friction;
		else velocity.x*=(1-Math.pow((1-friction), 1.2));
	}
	void setupRectangles(List<Rectangle> mapRectangleList, List<Entity> entityList){
		tempRectangleList.clear();
		tempRectangleList.addAll(mapRectangleList);
		for (Entity en: entityList){
			if (en.getCollisionType() == Collision.SOLID) {
				tempRectangleList.add(en.getImage().getBoundingRectangle());
			}
		}
	}
	void checkWalls(List<Rectangle> mapRectangleList){
		for (int i = 0; i < collisionCheck; ++i)
			if (collisionDetection(position.x + velocity.x, position.y, mapRectangleList)) velocity.x *= softening;
		if (collisionDetection(position.x + velocity.x, position.y, mapRectangleList)) velocity.x = 0;
	}
	void checkGrounded(List<Rectangle> mapRectangleList){
		for (int i = 0; i < collisionCheck; ++i)
			if (collisionDetection(position.x, position.y + velocity.y, mapRectangleList)) {
				if (velocity.y < 0) makeGrounded();
				velocity.y *= softening;
			}
		if (collisionDetection(position.x, position.y + velocity.y, mapRectangleList)) velocity.y = 0;
	}
	void makeGrounded(){
		if ((!flag_GROUNDED || isFalling()) && does_MOVE){
			flag_HITGROUND = true;
			state = State.STAND;
		}
	}
	void checkDiagonal(List<Rectangle> mapRectangleList){
		if (collisionDetection(position.x + velocity.x, position.y + velocity.y, mapRectangleList)) velocity.y = 0;
	}
	boolean collisionDetection(float x, float y, List<Rectangle> mapRectangleList){
		if (collision == Collision.GHOST) return false;
		boolean ignoreRectangle;
		for (Rectangle r : mapRectangleList){
			ignoreRectangle = false;
			Rectangle thisR = image.getBoundingRectangle();
			thisR.setX(x);
			thisR.setY(y);
			if (r.getHeight() <= 1 && r.getY() - this.getPosition().y > 0) ignoreRectangle = true;
			if (!ignoreRectangle && Intersector.overlaps(thisR, r) && thisR != r) return true;
		}
		return false;
	}
	void actGravity(float gravity){
		velocity.y-=(fallSpeed*gravity);
		if (velocity.y < terminalVelocity*gravity) velocity.y = terminalVelocity*gravity;
	}
	private void actWind(float wind) {
		if (flag_GROUNDED) velocity.x+=wind/(2*density);
		else velocity.x+=2*wind;
	}

	void jump(){
		state = State.JUMP;
		velocity.y += jumpStrength;
	}

	void takeDamage(Entity en){
		if (invincibleTimer.stopped()) {
			health -= en.damage;
			takeKnockback(en, 12f+(en.damage*6f), 6, false);
			if (en.damage > 0) {
				hurt.play(Bana.getVolume());
				invincibleTimer.set();
				stunTimer.set();
			}
			else{
				bump.play(Bana.getVolume());
			}
		}
	}
	void takeKnockback(Entity en, float x, float y, boolean done){
		if (!done && !en.isImmune() && (this.isEnemy() || this.getClass() == Hero.class)) en.takeKnockback(this, x/2, y/2, true);
		if (en.knockback == 0) return;
		float knockbackX = en.knockback*x/density;
		float knockbackY = en.knockback*y/density;
		knockbackX *= Math.random()+.5;
		knockbackY *= Math.random()+.2;
		if ((position.y - en.getPosition().y) > en.getImage().getHeight()/2) velocity.y = 7*en.bounce;
		else {
			velocity.y = knockbackY;
			if ((en.getPosition().x - position.x) < 0) velocity.x = knockbackX;
			else velocity.x = -knockbackX;
		}
	}

	public void stop(){
		velocity.x = 0;
		velocity.y = 0;
	}
	public void flip(){
		if (facing == Facing.LEFT) facing = Facing.RIGHT;
		else facing = Facing.LEFT;
		image.flip(true, false);
	}
	public void die(){
		flag_GOLEFT = flag_GORIGHT = false;
		flag_DEAD = true;
		does_MOVE = true;
		velocity.y += 2;
		image = corpse;
		is_ENEMY = false;
		acceleration = 0;
		fallSpeed = 0.5f;
		friction = 1;
		collision = Collision.GHOST;
	}
	void tremor(Entity en){
		if (flag_HITGROUND && this.isThisCloseTo(en, 320) && flag_GROUNDED) {
			flag_HITGROUND = false;
			if (en.flag_GROUNDED) {
				en.velocity.y += 3*density; 
				en.state = State.JUMP;
			}
		}
	}

	void setImage(String path){
		image = new Sprite(new Texture(Gdx.files.internal(path)));
		setImageHelper();
	}
	void setImage(TextureRegion tr){
		try {if (!tr.getTexture().equals(image.getTexture())) image = new Sprite(tr);}
		catch (NullPointerException e){image = new Sprite(tr);}
		setImageHelper();
	}
	private void setImageHelper(){
		corpse = image;
		center();
		updatePosition();
	}
	void changeImage(TextureRegion tr){
		float alpha = image.getColor().a;
		image.setRegion(tr);
		image.setColor(image.getColor().r, image.getColor().g, image.getColor().b, alpha);
		image.setX(position.x);
		image.setY(position.y);
		if (!image.isFlipX() && facing == Facing.LEFT) image.flip(true, false);
		if (image.isFlipX() && facing == Facing.RIGHT) image.flip(true, false);
	}
	void setCorpse(String path){
		corpse = new Sprite(new Texture(Gdx.files.internal(path)));
	}

	public void addHealth(int amount){
		health = MathUtils.clamp(health + amount, 0, maxHealth);
	}

	public void adjust(){
		position.y += fallSpeed;
	}
	void center(){
		if (image.getWidth() < TILE) position.x += ((TILE-image.getWidth())/2); // centers objects on their tile
	}

	public boolean isOverlapping(Entity en){
		return Intersector.overlaps(image.getBoundingRectangle(), en.getImage().getBoundingRectangle());
	}
	public boolean isTouching(Entity en){
		Rectangle tr = en.getImage().getBoundingRectangle();
		tr.setWidth(tr.getWidth()+2);
		tr.setHeight(tr.getHeight()+2);
		tr.setX(tr.getX()-1);
		tr.setY(tr.getY()-1);
		return Intersector.overlaps(image.getBoundingRectangle(), tr);
	}
	public boolean isAtop(Entity en){
		Rectangle tr = en.getImage().getBoundingRectangle();
		tr.setHeight(tr.getHeight()+1);
		return Intersector.overlaps(image.getBoundingRectangle(), tr);
	}

	public boolean isThisCloseTo(Entity en, float distance)
	{ return Math.abs(en.position.x - position.x) < distance && Math.abs(en.position.y - position.y) < distance;}

	public boolean isEnemy(){return is_ENEMY;}

	public boolean isImmune(){return is_IMMUNE;}

	public boolean isDestroyed(){return flag_TODESTROY;}

	public boolean isDestructable(){ return is_DESTRUCTABLE; }

	public boolean isRockhead(){ return is_ROCKHEAD; }

	public boolean isHurthead() { return is_HURTHEAD; }

	public boolean isDead(){ return flag_DEAD; }

	public boolean isHeavy() { return density > 1.5; }

	public boolean canBeAttacked(){ return (isEnemy() || isDestructable()) && !isImmune() && !isDead(); }

	public boolean canBeTrounced(){ return isEnemy() && !isHurthead() && !isImmune() && !isDead(); }

	public boolean isOutOfBounds(float width){ return position.y+image.getHeight() < 0 || position.x-image.getWidth() > width || position.x+image.getWidth() < 0; }

	boolean isFalling(){ return Math.abs(velocity.y) > 1; }

	public Vector2 getPosition(){ return position; }

	public Vector2 getVelocity(){ return velocity; }

	public Sprite getImage(){ return image; }

	public int getHealth(){ return health; }

	public int getMaxHealth(){ return maxHealth; }

	public Collision getCollisionType(){ return collision; }

	public List<Effect> getEffects(){ return effects; }

	public void reactToHero(Hero hero) { }

	public void reactToAll(Entity en) {
		if (isHeavy()) tremor(en);
	}

	int facing(){
		if (facing == Facing.RIGHT) return 1;
		else return -1;
	}

	Animation makeAnimation(String address, int cols, int rows, float speed, PlayMode playMode){
		Texture sheet = new Texture(Gdx.files.internal(address));
		TextureRegion[][] tmp = TextureRegion.split(sheet, sheet.getWidth()/cols, sheet.getHeight()/rows);
		TextureRegion[] frames = new TextureRegion[cols * rows];
		int index = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		Animation animation = new Animation(speed, frames);
		animation.setPlayMode(playMode);
		return animation;
	}

	void resetTimers(){
		for(Timer timer: timerList)timer.reset();
	}

	class Timer{
		int clock = 0;
		private final int stop;
		Timer(int stop){ this.stop = stop; }
		void set(){ clock = deltaTime + stop; }
		void set(int time){ clock = deltaTime + time; }
		void reset(){ clock = 0; }
		boolean stopped(){ return clock <= deltaTime; }
		boolean timed(){ return Math.abs(clock - deltaTime) == 1; }
	}

	class Hitbox{
		Rectangle rectangle;
		private boolean active = false;
		float dispX, dispY;
		Hitbox(float dispX, float dispY, float sizeX, float sizeY){ 
			this.dispX = dispX;
			this.dispY = dispY;
			this.rectangle = new Rectangle(dispX+position.x, dispY+position.y, sizeX, sizeY);
		}
		void activate(){ active = true; }
		void deactivate(){ active = false; }
		void setDisplacementX(float x){ dispX = x; set(); }
		void setDisplacementY(float y){ dispY = y; set(); }
		void set(){
			this.rectangle.x = dispX + position.x;
			this.rectangle.y = dispY + position.y;
		}
		void update(){
			rectangle.x += velocity.x;
			rectangle.y += velocity.y;
		}
		boolean isActive(){ return active; }
		Rectangle getRectangle(){ return rectangle; }
	}

	enum Collision{
		SOLID, CREATURE, GHOST
	}

	enum Facing{
		LEFT, RIGHT
	}

	enum State{
		JUMP, DOUBLEJUMP, DAMAGE, STAND, CROUCH, WALLJUMP
	}
}
