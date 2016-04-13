package rendering;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import game.Game;

public class World {
	BufferedImage worldImg;
	Tile[][] tileSet;
	double[][] configMap;
	Color playerInsertion;
	Point light;
	Game currentGame;
	int width, height;
	boolean playerFound = false, lightFound = false;
	
	public World(Game game, String fileName){
		playerInsertion = new Color(255, 0, 0);
		this.currentGame = game;
		try {
			File worldFile = new File(fileName);
			worldImg = ImageIO.read(worldFile);
			parseConfig();
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
		Color emptySpace = new Color(255, 255, 255, 0); //png transparent whitespace
		for(int x = 0; x < width; ++x){
			for(int y = 0; y < height; ++y){
				double tileHeight, tileGap;
				Color colorAtCoord = new Color(worldImg.getRGB(x, y), true);
				if(colorAtCoord.equals(playerInsertion)){ //found the player square, inserting player!
					System.out.println("player found at " + y + ", " + x);
					playerFound = true;
					currentGame.getPlayer().setPosition(x, y);
					tileSet[x][y] = new Tile(TileType.PLAYER);
				}
				else if(colorAtCoord.equals(emptySpace)){ //if it's an empty tile
					
					tileSet[x][y] = new Tile(TileType.EMPTY);
				}
				else if(colorAtCoord.equals(Color.YELLOW)){
					lightFound = true;
					tileSet[x][y] = new Tile(colorAtCoord, 1, 2);
					light = new Point(x, y);
				}
				else{
					tileSet[x][y] = new Tile(colorAtCoord, configMap[colorAtCoord.getAlpha()][0], configMap[colorAtCoord.getAlpha()][1]);
				}
			}
		}
		//tileSet[50][50] = new Tile(Color.RED, 1, 0);
		if(!playerFound){
			playerFound = true;
			currentGame.getPlayer().setPosition(10, 10);
		}
		if(!lightFound){
			lightFound = true;
			light = new Point(10, 10);			
		}
			
	}
	
	private void parseConfig(){
		configMap = new double[256][2];
		try {
			Scanner config = new Scanner(new File("alphaConfig.txt"));
			while(config.hasNextLine()){
				String line = config.nextLine();
				if(!line.contains("#")){
					String[] delimit = line.split("\\s+"); //splitting by whitespace
					configMap[Integer.parseInt(delimit[0])][0] = Double.parseDouble(delimit[1]);
					configMap[Integer.parseInt(delimit[0])][1] = Double.parseDouble(delimit[2]);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(Arrays.toString(configMap[255]));
		System.out.println(Arrays.toString(configMap[254]));
	}
	public Tile getTileAt(double x, double y){
		return tileSet[(int)x][(int)y];
	}
	
	public Point getLightSource(){
		return light;
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
