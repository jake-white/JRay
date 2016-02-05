package rendering;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;

public class World {
	BufferedImage worldImg;
	Color[][] map;
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
		map = new Color[width][height];
		Color emptySpace = new Color(255, 255, 255);
		for(int x = 0; x < width; ++x){
			for(int y = 0; y < height; ++y){
				Color testingColor = new Color(worldImg.getRGB(x, y));
				if(testingColor.getRGB() == playerInsertion.getRGB()){
					//found the player square, inserting player!
					System.out.println("player found at " + y + ", " + x);
					currentGame.getPlayer().setPosition(y, x);
					map[y][x] = emptySpace;
				}
				else
					map[y][x] = testingColor;
			}
		}
	}
	
	public Color getColorAt(double x, double y){
		return map[(int)x][(int)y];
	}
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

}
