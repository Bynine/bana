package entities;

import com.badlogic.gdx.math.Intersector;

public class Switch extends Entity{
	
	Trapdoor trapdoor;

	public Switch(float x, float y, Trapdoor trapdoor) {
		super(x, y);
		this.trapdoor = trapdoor;
		setImage("sprites/switch.PNG");
		density = 3f;
		does_MOVE = false;
		is_DESTRUCTABLE = true;
	}
	
	@Override
	public void reactToAll(Entity en) {
		for(Hitbox hitbox: en.hitboxList){
			if (Intersector.overlaps(hitbox.getRectangle(), image.getBoundingRectangle()) && hitbox.isActive()){
				trapdoor.activate();
			}
		}
	}

}
