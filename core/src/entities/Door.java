package entities;

import main.Bana;
import maps.Room;

import com.badlogic.gdx.math.Vector2;

public class Door extends Entity{

	private Room room;
	private Vector2 destination = new Vector2();
	boolean TEMPflag_END = false;
	
	public Door(float x, float y, Room room, float desX, float desY){
		super(x, y);
		this.room = room;
		destination.x = desX;
		destination.y = desY;
		fallSpeed = 0;
		does_MOVE = false;
		setImage("sprites/door.PNG");
	}
	
	public Door(float x, float y, Room room){
		super(x, y);
		this.room = room;
		TEMPflag_END = true;
		destination.x = 0;
		destination.y = 0;
		fallSpeed = 0;
		does_MOVE = false;
		setImage("sprites/door.PNG");
	}
	
	public Room getRoom(){
		if (TEMPflag_END) Bana.TEMPendGame();
		return room;
	}
	public Vector2 getDestination(){
		return destination;
	}
}
