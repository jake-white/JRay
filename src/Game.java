import javax.swing.Timer;

public class Game {
	Screen currentScreen;
	Timer gameLoop;
	World world;
	public Game(){
		world = new World(this, "world.png");
		currentScreen = new Screen(this);
		gameLoop = new Timer(20, new Tick(this));
		gameLoop.start();
	}
	
	public void render(){
		currentScreen.rayCast();
		currentScreen.display();
	}
}
