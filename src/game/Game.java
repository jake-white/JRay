package game;
import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

import physics.InputManager;
import rendering.Camera;
import rendering.Screen;
import rendering.Tick;
import rendering.World;

public class Game {
	Screen currentScreen;
	InputManager input;
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
		this.input = new InputManager();
		currentScreen = new Screen(this);
		player = new Player();
		playerCam = new Camera(player);
		//setting up the JFrame
		mainFrame = new JFrame();
		mainFrame.setSize(800, 600);
		mainFrame.getContentPane().add(currentScreen);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.addKeyListener(input);
		mainFrame.setTitle("JRay");
		world = new World(this, "world.bmp");
		System.out.println("Started game!");
		currentScreen.rayCast();
		gameLoop = new Timer(1, new Tick(this));
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
		parseInput(player);
		currentScreen.rayCast();
		currentScreen.setFrameRate(frames);
		mainFrame.repaint();
	}
	
	public void parseInput(Player p){
		double walk_speed = 0.05;
		double turn_speed = 0.07;
		double fly_speed = 0.04;
		if(input.input(KeyEvent.VK_W)){
			p.walk(walk_speed);
		}
		else if(input.input(KeyEvent.VK_S)){
			p.walk(-walk_speed);
		}
		if(input.input(KeyEvent.VK_Q)){
			p.turn(turn_speed);
		}
		else if(input.input(KeyEvent.VK_E)){
			p.turn(-turn_speed);
		}
		if(input.input(KeyEvent.VK_Z)){
			p.up(fly_speed);
		}
		else if(input.input(KeyEvent.VK_X)){
			p.up(-fly_speed);
		}
	}
}
