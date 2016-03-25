package rendering;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;

public class World {
	BufferedImage worldImg;
	Tile[][] tileSet;
	Color playerInsertion;
	Game currentGame;
	int width, height;
	
	public World(Game game, String fileName){
		playerInsertion = new Color(255, 0, 0);
		this.currentGame = game;
		try {
			File worldFile = new File(fileName);
			worldImg = ImageIO.read(worldFile);
			parseMap();
		} catch (IOException e) {
			//image file didn't exist, bla bla bla
			e.printStackTrace();
		}
	}
	private void parseMap() { //reads through the image for each pixel
		this.width = worldImg.getWidth();
		this.height = worldImg.getHeight();
		tileSet = new Tile[width][height];
		Color emptySpace = new Color(255, 255, 255);
		for(int x = 0; x < width; ++x){
			for(int y = 0; y < height; ++y){
				double tileHeight, tileGap;
				Color colorAtCoord = new Color(worldImg.getRGB(x, y));
				if(colorAtCoord.equals(playerInsertion)){ //found the player square, inserting player!
					System.out.println("player found at " + y + ", " + x);
					currentGame.getPlayer().setPosition(x, y);
					tileSet[x][y] = new Tile(TileType.PLAYER);
				}
				else if(colorAtCoord.equals(emptySpace)){ //if it's an empty tile
					
					tileSet[x][y] = new Tile(TileType.EMPTY);
				}
				else{
					if(colorAtCoord.equals(Color.GRAY)){
						tileHeight = 0.001;
						tileGap = 1;
					}
					else if(colorAtCoord.equals(Color.BLUE)){
						tileHeight = 3;
						tileGap = 0;
					}
					else if(colorAtCoord.equals(Color.MAGENTA)){
						tileHeight = 2;
						tileGap = 0;
					}
					else {
						tileHeight = 2;
						tileGap = 0;
					}
					tileSet[x][y] = new Tile(colorAtCoord, tileHeight, tileGap);
				}
			}
		}
	}
	public Tile getTileAt(double x, double y){
		return tileSet[(int)x][(int)y];
	}
	
	public Tile getTileAt(Point p){
		return tileSet[(int) p.getX()][(int) p.getY()];
	}
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

}
