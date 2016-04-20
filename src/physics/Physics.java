package physics;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import audio.MusicPlayer;
import audio.SFX;
import game.Game;
import game.Player;
import rendering.Camera;
import rendering.Sprite;
import rendering.Tile;
import rendering.TileType;

public class Physics {
	Game game;
	Player player;
	InputManager input;
	JFrame mainFrame;
	public double frictionConstant = 0.5, stairThreshold = 0.21;
	
	public Physics(Game game, JFrame mainFrame){
		this.mainFrame = mainFrame;
		this.input = new InputManager();
		mainFrame.addKeyListener(input);
		mainFrame.addMouseListener(input);
		this.game = game;
		this.player = game.getPlayer();
	}
	
	public void tick(){
		this.parseInput(this.player);
		this.calculatePhys();
	}
	
	public void calculatePhys(){
		validateHeight();
		double[] intendedPosition = new double[3];
		//friction
		player.setAccelX(player.getAccelX()*frictionConstant);
		player.setAccelY(player.getAccelY()*frictionConstant);
		intendedPosition[0] = player.getPosition().getX() + player.getAccelX();
		intendedPosition[1] = player.getPosition().getY() + player.getAccelY();
		intendedPosition[2] = player.getZ() + player.getAccelZ();
		if(!collisionCheck(intendedPosition, false)){
			player.setPosition(intendedPosition[0], intendedPosition[1]);
			player.setZ(intendedPosition[2]);
		}
		else
			player.setAccelZ(0);
			
	}
	
	public boolean collisionCheck(double[] intended, boolean gravity){
		Tile[][] tiles = game.getWorld().getTiles();
		Player intendedPlayer = new Player();
		intendedPlayer.setPosition(intended[0], intended[1]);
		intendedPlayer.setZ(intended[2]);
		boolean hasCollidedYet = false;
		for(int x = 0; x < tiles.length; ++x){
			for(int y = 0; y < tiles[x].length; ++y){
				if(this.isColliding(intendedPlayer, tiles[x][y], x, y, intended, gravity)){
					hasCollidedYet = true;
				}
			}
		}
		return hasCollidedYet;
	}
	
	public boolean isColliding(Player player, Tile tile, int x, int y, double[] intended, boolean gravity){
		if((int) Math.floor(player.getPosition().getX()) == x && (int) Math.floor(player.getPosition().getY()) == y
				&& !(tile.getType().equals(TileType.EMPTY) || tile.getType().equals(TileType.PLAYER))){
			//if within x and y is isn't an empty tile
			if(player.getBottom() < tile.getTop() && player.getTop() > tile.getTop()){
				//if it's intersecting it z-position wise
				if(Math.abs(tile.getTop() - player.getBottom()) <= stairThreshold && !gravity){
					System.out.println(player.getBottom() + " VS " + tile.getTop());
					System.out.println(Math.abs(player.getBottom() - tile.getTop()));
					this.player.setPosition(intended[0], intended[1]);
					this.player.setZ(tile.getTop());
					this.player.setAccelZ(0);
					return true;
				}
				return true;
			}
			else if(player.getBottom() < tile.getBottom() && player.getTop() > tile.getBottom() && !gravity){
				player.setZ(tile.getBottom());
				return true;
			}
			else if(player.getBottom() >= tile.getBottom() && player.getTop() <= tile.getTop() && !gravity){
				//if the player is completely within the tile
				return true;
			}
		}
		return false;
	}
	
	public void mouseInput(){
		Sprite enemy = game.getScreen().getMiddlePixelSprite();
		if(input.getMouseClicked()){
			game.getGun().shoot();
			game.gunFX();
			if(enemy != null){
				enemy.hit(10);
			}
		}
		double turn_speed = 0.01;
		if(input.isLocked()){
			Cursor cursor = Cursor.getDefaultCursor();
			Robot bot;
			int sourceX = mainFrame.getX()+mainFrame.getWidth()/2;
			int sourceY = mainFrame.getY()+mainFrame.getHeight()/2;
			int dx = sourceX - (int) MouseInfo.getPointerInfo().getLocation().getX();
			int dy = (int) MouseInfo.getPointerInfo().getLocation().getY() - sourceY;
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

			// Create a new blank cursor.
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			    cursorImg, new Point(0, 0), "blank cursor");

			// Set the blank cursor to the JFrame.
			mainFrame.getContentPane().setCursor(blankCursor);
			
			try {
				bot = new Robot();
				bot.mouseMove(sourceX, sourceY);
				player.turn(turn_speed*dx);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void parseInput(Player p){
		this.mouseInput();
		double walk_speed = 0.05;
		double fly_speed = 0.04;
		Camera camera = game.getCamera();
		if(input.input(KeyEvent.VK_W)){
			p.walk(walk_speed);
		}
		else if(input.input(KeyEvent.VK_S)){
			p.walk(-walk_speed);
		}
		if(input.input(KeyEvent.VK_A)){
			p.strafe(walk_speed);
		}
		else if(input.input(KeyEvent.VK_D)){
			p.strafe(-walk_speed);
		}
		if(input.input(KeyEvent.VK_R)){
			camera.changeView(walk_speed);
		}
		else if(input.input(KeyEvent.VK_F)){
			camera.changeView(-walk_speed);
		}
		if(input.input(KeyEvent.VK_SPACE)){
			if(player.getAccelZ() == 0)
			p.up(fly_speed);
		}
	}
	
	private void validateHeight(){
		if(player.getZ() < 0){
			player.setAccelZ(0);
			player.setZ(0);
		}
		else{
			double[] gravity = new double[3];
			gravity[0] = player.getPosition().getX();
			gravity[1] = player.getPosition().getY();
			gravity[2] = player.getZ() + (player.getAccelZ()-.001);
			
			if(!collisionCheck(gravity, true)){
				player.setAccelZ(player.getAccelZ()-.001);
			}
				
		}
	}
}
