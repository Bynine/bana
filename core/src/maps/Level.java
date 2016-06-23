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
	final List<Room> rooms = new ArrayList<>();
	final List<List<Collectible>> roomContents = new ArrayList<>();
	public static Artifact getArtifact(){
		if (artifact != null) artifact.adjust();
		return artifact;
	}
	public void setArtifactObtained(){
		artifactObtained = true;
	}
	public boolean getArtifactObtained(){
		return artifactObtained;
	}
	public Room getStartRoom(){
		return rooms.get(0); // TODO: change room list to just starting room?
	}
	public Room getRoom(int i){
		return rooms.get(i);
	}
	public List<Collectible> getRoomContents(int i){
		return roomContents.get(i);
	}
}
