package maps;

import java.util.ArrayList;
import java.util.Arrays;

import entities.Artifact;
import entities.Coin;
import entities.Collectible;
import entities.Food;

public class Level_Oasis extends Level{
	
	public Level_Oasis(){
		artifact = new Artifact(TILE*7, TILE*5, Artifact.Type.CHEST);
		rooms.addAll(Arrays.asList(new Room_OasisStart(this), new Room_OasisMiddle(this), new Room_OasisEnd(this)));
		roomContents.addAll(Arrays.asList(oasisStartList, oasisMiddleList, oasisEndList));
	}
	
	public ArrayList<Collectible> oasisStartList = new ArrayList<Collectible>(Arrays.asList(
			new Coin(TILE*15, TILE*6, Coin.Type.COIN),
			new Coin(TILE*16, TILE*6, Coin.Type.COIN),
			new Coin(TILE*17, TILE*6, Coin.Type.COIN),
			new Coin(TILE*18, TILE*6, Coin.Type.COIN),
			new Coin(TILE*28, TILE*5, Coin.Type.COIN),
			new Coin(TILE*29, TILE*5, Coin.Type.COIN),
			new Coin(TILE*30, TILE*5, Coin.Type.COIN),
			new Coin(TILE*28, TILE*6, Coin.Type.COIN),
			new Coin(TILE*29, TILE*6, Coin.Type.COIN),
			new Coin(TILE*30, TILE*6, Coin.Type.COIN),
			new Coin(TILE*77, TILE*10, Coin.Type.COIN),
			new Coin(TILE*78, TILE*10, Coin.Type.COIN),
			new Coin(TILE*79, TILE*10, Coin.Type.COIN),
			new Coin(TILE*80, TILE*10, Coin.Type.COIN),
			new Coin(TILE*48, TILE*19, Coin.Type.GEM),
			new Coin(TILE*93, TILE*10, Coin.Type.GEM),
			new Food(TILE*82, TILE*10, Food.Type.LIGHT)
			));
	public ArrayList<Collectible> oasisMiddleList = new ArrayList<Collectible>(/*Arrays.asList(
			new Coin(TILE*7, TILE*9, Coin.Type.GEM)
			)*/);
	public ArrayList<Collectible> oasisEndList = new ArrayList<Collectible>(Arrays.asList(
			new Coin(TILE*17, TILE*17, Coin.Type.COIN),
			new Coin(TILE*18, TILE*17, Coin.Type.COIN),
			new Coin(TILE*19, TILE*17, Coin.Type.COIN),
			new Coin(TILE*10, TILE*17, Coin.Type.COIN),
			new Coin(TILE*11, TILE*17, Coin.Type.COIN),
			new Coin(TILE*12, TILE*17, Coin.Type.COIN),
			new Coin(TILE*73, TILE*4, Coin.Type.COIN),
			new Coin(TILE*74, TILE*4, Coin.Type.COIN),
			new Coin(TILE*75, TILE*4, Coin.Type.COIN),
			new Coin(TILE*76, TILE*4, Coin.Type.COIN),
			new Coin(TILE*77, TILE*4, Coin.Type.COIN),
			new Coin(TILE*7, TILE*16, Coin.Type.GEM),
			new Coin(TILE*27, TILE*7, Coin.Type.GEM),
			new Coin(TILE*75, TILE*19, Coin.Type.GEM),
			new Food(TILE*48, TILE*7, Food.Type.HEAVY)
			));
}
