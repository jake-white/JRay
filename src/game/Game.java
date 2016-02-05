package game;
import javax.swing.JFrame;
import javax.swing.Timer;

import rendering.Screen;
import rendering.Tick;
import rendering.World;

public class Game {
	Screen currentScreen;
	Timer gameLoop;
	World world;
	JFrame mainFrame; //hacking the mainframe with a visual basic GUI
	Player player;
	
	public Game(){
		currentScreen = new Screen(this);
		player = new Player();
		mainFrame = new JFrame();
		mainFrame.setSize(800, 600);
		mainFrame.getContentPane().add(currentScreen);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		world = new World(this, "world.png");
		System.out.println("hi");
		currentScreen.rayCast();
		gameLoop = new Timer(20, new Tick(this));
		gameLoop.start();
	}
	
	public Player getPlayer(){
		return player;
	}
	public Screen getScreen(){
		return currentScreen;
	}
	
	public World getWorld(){
		return world;
	}
	
	public void render(){
		currentScreen.rayCast();
		currentScreen.display();
		mainFrame.repaint();
	}
}
