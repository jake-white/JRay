package game;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

import audio.Music;
import audio.MusicPlayer;
import audio.SFX;
import physics.InputManager;
import physics.Physics;
import rendering.Camera;
import rendering.Screen;
import rendering.Sprite;
import rendering.Tick;
import rendering.Weapon;
import rendering.World;

public class Game {
	Screen currentScreen;
	Timer gameLoop;
	World world;
	JFrame mainFrame; //hacking the mainframe with a visual basic GUI
	Camera playerCam;
	Player player;
	static Game newGame;
	private Weapon gun;
	private MusicPlayer musPlayer;
	private boolean musicPlaying = false;
	private boolean over = false;
	
	public static void main(String[] args){
		newGame = new Game();
	}
	
	public Game(){
		currentScreen = new Screen(this);
		player = new Player(this);
		playerCam = new Camera(player);
		//setting up the JFrame
		mainFrame = new JFrame();
		mainFrame.setSize(800, 600);
		mainFrame.getContentPane().add(currentScreen);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setTitle("JRay");
		world = new World(this, "res/world.png");
		gun = new Weapon("gun");
		musPlayer = new MusicPlayer();
		System.out.println("Started game!");
		currentScreen.rayCast();
		gameLoop = new Timer(1, new Tick(this, mainFrame));
		gameLoop.start();
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Camera getCamera(){
		return playerCam;
	}
	public Screen getScreen(){
		return currentScreen;
	}
	
	public World getWorld(){
		return world;
	}
	
	public void AITick(){
		for(int i = 0; i < getWorld().getSpriteList().size(); ++i){
			getWorld().getSpriteList().get(i).makeDecision();
		}
	}
	
	public void gunFX(){
		musPlayer.play(new SFX("res/laserfire02.wav"));
	}
	
	public void playBattleMusic(){
		if(!musicPlaying){
			musicPlaying = true;
			musPlayer.play(new Music("res/battleThemeA.wav"));
		}
	}

	public void stopBattleMusic() {
		musPlayer.stopMusic();
		musicPlaying = false;
	}
	
	public void render(long frames){
		currentScreen.rayCast();
		currentScreen.setFrameRate(frames);
		mainFrame.repaint();
	}

	public Weapon getGun() {
		return gun;
	}

	public void over() {
		over = true;
	}
	
	public boolean isOver(){
		return over;
	}
	

}
