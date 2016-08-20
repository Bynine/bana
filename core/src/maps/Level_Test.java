package maps;

import java.util.Arrays;

import entities.Artifact;

public class Level_Test extends Level{
	
	public Level_Test(){
		artifact = new Artifact(TILE*1, TILE*1, Artifact.Type.URN);
		rooms.addAll(Arrays.asList(new Room_Test(this)));
	}
}
