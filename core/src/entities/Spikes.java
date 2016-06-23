package entities;

public class Spikes extends Entity{
	public Spikes(float x, float y, Type type){
		super(x, y);
		if (type == Type.CACTUS) setImage("sprites/invisibleTile.PNG");
		else setImage("sprites/spikes.PNG");
		fallSpeed = 0;
		is_ENEMY = true;
		is_IMMUNE = true;
		knockback = 1.5f;
		does_MOVE = false;
		if (type == Type.CACTUS) damage = 1;
		else damage = 5;
	}
	
	public enum Type{
		SPIKES, CACTUS
	}
}
