package rendering;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

import game.Game;
import rendering.Tile;

public class World {
	private BufferedImage worldImg;
	private Tile[][] tileSet;
	private ArrayList<Sprite> spriteSet;
	private ArrayList<Projectile> projectiles;
	double[][] configMap;
	private Color playerInsertion;
	private ArrayList<Point> lights;
	private Game currentGame;
	int width, height;
	boolean playerFound = false;
	private Boss boss;
	private int bossAlpha = 241, activationAlpha = 240, risingAlpha = 239;
	private ArrayList<Tile> activationTiles;
	private ArrayList<Tile> risingTiles;
	private Tile bossTile;
	
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
		spriteSet = new ArrayList<Sprite>();
		lights = new ArrayList<Point>();
		activationTiles = new ArrayList<Tile>();
		risingTiles = new ArrayList<Tile>();
		tileSet = new Tile[width][height];
		projectiles = new ArrayList<Projectile>();
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
				else if(new Color(colorAtCoord.getRGB(), false).equals(Color.YELLOW)){
					tileSet[x][y] = new Tile(colorAtCoord, configMap[colorAtCoord.getAlpha()][0], configMap[colorAtCoord.getAlpha()][1], x, y);
					lights.add(new Point(x, y));
				}
				else{
					tileSet[x][y] = new Tile(colorAtCoord, configMap[colorAtCoord.getAlpha()][0], configMap[colorAtCoord.getAlpha()][1], x, y);
				}
				if(configMap[colorAtCoord.getAlpha()][2]==1){
					spriteSet.add(new Sprite(x, y, "res/sprites/zombie.png", this.currentGame));
				}
				if(colorAtCoord.getAlpha() == bossAlpha){
					boss = new Boss(x+0.5, y+0.5, "res/sprites/andy.png", currentGame);
					bossTile = tileSet[x][y];
				}
				if(colorAtCoord.getAlpha() == activationAlpha){
					activationTiles.add(tileSet[x][y]);
				}
				if(colorAtCoord.getAlpha() == risingAlpha){
					risingTiles.add(tileSet[x][y]);
				}
			}
		}
		//tileSet[50][50] = new Tile(Color.RED, 1, 0);
		if(!playerFound){
			playerFound = true;
			currentGame.getPlayer().setPosition(10, 10);
		}
		if(lights.isEmpty()){
			lights.add(new Point(10, 10));			
		}
			
	}
	
	private void parseConfig(){
		configMap = new double[256][3];
		try {
			Scanner config = new Scanner(new File("alphaConfig.txt"));
			while(config.hasNextLine()){
				String line = config.nextLine();
				if(!line.contains("#")){
					String[] delimit = line.split("\\s+"); //splitting by whitespace
					configMap[Integer.parseInt(delimit[0])][0] = Double.parseDouble(delimit[1]);
					configMap[Integer.parseInt(delimit[0])][1] = Double.parseDouble(delimit[2]);
					configMap[Integer.parseInt(delimit[0])][2] = Integer.parseInt(delimit[3]);
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
	
	public ArrayList<Point> getLightSources(){
		return lights;
	}
	
	public ArrayList<Sprite> getSpriteList(){
		return spriteSet;
	}
	
	public ArrayList<Tile> getActivationTiles(){
		return activationTiles;
	}
	
	public ArrayList<Tile> getRisingTiles(){
		return risingTiles;
	}
	
	public Boss getBoss(){
		return boss;
	}

	public Tile getBossTile(){
		return bossTile;
	}
	
	public ArrayList<Projectile> getProjectiles(){
		return projectiles;
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
	public Tile[][] getTiles() {
		return tileSet;
	}

}
