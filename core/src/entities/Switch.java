package entities;

public class Switch extends Entity{
	
	Trapdoor trapdoor;

	public Switch(float x, float y, Trapdoor trapdoor) {
		super(x, y);
		this.trapdoor = trapdoor;
		setImage("sprites/switch.PNG");
		density = 3f;
		does_MOVE = false;
		is_ENEMY = true;
		damage = 0;
	}
	
	@Override
	public void reactToAll(Entity en) {
		if (flag_DEAD) trapdoor.activate();
	}

}
