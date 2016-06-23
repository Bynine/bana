package entities;

public class Box extends Entity {

	public Box(float x, float y) {
		super(x, y);
		collision = Collision.SOLID;
		setImage("sprites/box.PNG");
		center();
	}

}
