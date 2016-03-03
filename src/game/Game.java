package game;
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
		mainFrame = new JFrame();
		mainFrame.setSize(800, 600);
		mainFrame.getContentPane().add(currentScreen);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		mainFrame.addKeyListener(input);
		world = new World(this, "world.png");
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
		if(input.input(KeyEvent.VK_W)){
			p.walk(0.03);
		}
		else if(input.input(KeyEvent.VK_S)){
			p.walk(-0.03);
		}
		if(input.input(KeyEvent.VK_Q)){
			p.turn(0.05);
		}
		else if(input.input(KeyEvent.VK_E)){
			p.turn(-0.05);
		}
		if(input.input(KeyEvent.VK_Z)){
			p.up(0.02);
		}
		else if(input.input(KeyEvent.VK_X)){
			p.up(-0.02);
		}
	}
}
