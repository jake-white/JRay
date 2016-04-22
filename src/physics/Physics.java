package physics;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

import audio.MusicPlayer;
import audio.SFX;
import game.Game;
import game.Player;
import rendering.Boss;
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
		game.AITick();
		this.parseInput(this.player);
		this.calculatePhys();
		this.cutsceneCheck();
	}
	
	private void cutsceneCheck() {
		boolean timeToActivate = false;
		ArrayList<Tile> activation = game.getWorld().getActivationTiles();
		for(int i = 0; i < activation.size(); ++i){
			Tile thisTile = activation.get(i);
			if(Math.floor(player.getPosition().getX()) == thisTile.getX() && Math.floor(player.getPosition().getY()) == thisTile.getY()){
				timeToActivate = true;
			}
		}
		if(timeToActivate){
			player.cutscene();
		}
	}

	public void calculatePhys(){
		boolean music = false, bossMusic = false;
		ArrayList<Sprite> allEnemies = new ArrayList<Sprite>(game.getWorld().getSpriteList());
		allEnemies.add(game.getWorld().getBoss());
		for(int i = 0; i < allEnemies.size(); ++i){
			Sprite enemy = allEnemies.get(i);
			if(enemy.isAlive()){
				if(!(enemy instanceof Boss)){
					double[] intendedPosition = new double[3];
					//friction
					enemy.setAccelX(enemy.getAccelX()*frictionConstant);
					enemy.setAccelY(enemy.getAccelY()*frictionConstant);
					intendedPosition[0] = enemy.getPositionX() + enemy.getAccelX();
					intendedPosition[1] = enemy.getPositionY() + enemy.getAccelY();
					intendedPosition[2] = enemy.getZPos();
					if(!collisionCheck(enemy, intendedPosition, false)){
						enemy.setPosition(intendedPosition[0], intendedPosition[1]);
						enemy.setZ(intendedPosition[2]);
					}
					else
						enemy.setAccelZ(0);
					if(enemy.isInAttackingRadius() && enemy.isAlive())
						enemy.attack(player, 5);					
				}
				if(enemy instanceof Boss && ((Boss) enemy).isActive()){
					bossMusic = true;
				}
				else if(!(enemy instanceof Boss) && enemy.isInMusicRadius()){
					music = true;
				}
			}
		}
		if(bossMusic){
			game.playBossMusic();
		}
		else if(music){
			game.playBattleMusic();
		}
		else
			game.stopBattleMusic();
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
	
	//checking the player
	public boolean collisionCheck(double[] intended, boolean gravity){
		Tile[][] tiles = game.getWorld().getTiles();
		Player intendedPlayer = new Player(game);
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
	
	//checking enemies
	public boolean collisionCheck(Sprite enemy, double[] intended, boolean gravity){
		Tile[][] tiles = game.getWorld().getTiles();
		Sprite intendedEnemy = new Sprite(intended[0], intended[1], enemy.getFileName(), game.getCamera(), game);
		intendedEnemy.setZ(intended[2]);
		boolean hasCollidedYet = false;
		for(int x = 0; x < tiles.length; ++x){
			for(int y = 0; y < tiles[x].length; ++y){
				if(this.isColliding(enemy, intendedEnemy, tiles[x][y], x, y, intended, gravity)){
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
	
	//is the enemy colliding
	public boolean isColliding(Sprite enemy, Sprite intendedEnemy, Tile tile, int x, int y, double[] intended, boolean gravity){
		if((int) Math.floor(intendedEnemy.getPositionX()) == x && (int) Math.floor(intendedEnemy.getPositionY()) == y
				&& !(tile.getType().equals(TileType.EMPTY) || tile.getType().equals(TileType.PLAYER))){
			//if within x and y is isn't an empty tile
			if(intendedEnemy.getBottom() < tile.getTop() && intendedEnemy.getTop() > tile.getTop()){
				//if it's intersecting it z-position wise
				if(Math.abs(tile.getTop() - player.getBottom()) <= stairThreshold && !gravity){
					enemy.setPosition(intended[0], intended[1]);
					enemy.setZ(tile.getTop());
					enemy.setAccelZ(0);
					return true;
				}
				return true;
			}
			else if(intendedEnemy.getBottom() < tile.getBottom() && intendedEnemy.getTop() > tile.getBottom() && !gravity){
				enemy.setZ(tile.getBottom());
				return true;
			}
			else if(intendedEnemy.getBottom() >= tile.getBottom() && intendedEnemy.getTop() <= tile.getTop() && !gravity){
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
		double turn_speed = 0.005;
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
				if(player.canMove())
					player.turn(turn_speed*dx);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void parseInput(Player p){
		this.mouseInput();

		if(player.canMove()){
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
