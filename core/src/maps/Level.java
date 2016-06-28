package maps;

import java.util.ArrayList;
import java.util.List;

import main.Bana;
import entities.Artifact;
import entities.Collectible;

public abstract class Level {
	boolean artifactObtained = false;
	static Artifact artifact;
	static final int TILE = Bana.TILE;
	static int record; // TODO: implement record. gives high score of coins
	final List<Room> rooms = new ArrayList<>();
	final List<List<Collectible>> roomContents = new ArrayList<>();
	public static Artifact getArtifact(){
		if (artifact != null) artifact.adjust();
		return artifact;
	}
	public boolean getArtifactObtained(){
		return artifact.isObtained();
	}
	public Room getRoom(int i){
		return rooms.get(i);
	}
	public List<Collectible> getRoomCollectibles(int i){
		return roomContents.get(i);
	}
}
