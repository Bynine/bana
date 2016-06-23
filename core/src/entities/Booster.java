package entities;

public class Booster extends Entity{

	private Direction direction;
	
	public Booster(float x, float y, Direction direction) {
		super(x, y);
		this.direction = direction;
		fallSpeed = 0;
		does_MOVE = false;
		setImage("sprites/booster.PNG");
		if (direction == Direction.DOWN) image.flip(false, true);
	}

	public void reactToAll(Entity entity) {
		if (isOverlapping(entity)) {
			entity.state = State.JUMP;
			if (direction == Direction.UP) entity.velocity.y += 10;
			if (direction == Direction.DOWN) entity.velocity.y -= 10;
		}
	}
	
	public enum Direction{
		UP, DOWN
	}
}
