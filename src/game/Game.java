package game;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

import physics.InputManager;
import physics.Physics;
import rendering.Camera;
import rendering.Screen;
import rendering.Tick;
import rendering.World;

public class Game {
	Screen currentScreen;
	Timer gameLoop;
	World world;
	JFrame mainFrame; //hacking the mainframe with a visual basic GUI
	Camera playerCam;
	Player player;
	static Game newGame;
	
	public static void main(String[] args){
		newGame = new Game();
	}
	
	public Game(){
		currentScreen = new Screen(this);
		player = new Player();
		playerCam = new Camera(player);
		//setting up the JFrame
		mainFrame = new JFrame();
		mainFrame.setSize(800, 600);
		mainFrame.getContentPane().add(currentScreen);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.setTitle("JRay");
		world = new World(this, "res/world.png");
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
	
	public void render(long frames){
		currentScreen.rayCast();
		currentScreen.setFrameRate(frames);
		mainFrame.repaint();
	}
	

}
