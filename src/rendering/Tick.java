package rendering;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import game.Game;
import game.Player;
import physics.InputManager;

public class Tick implements ActionListener{
	Game currentGame;
	long frameTime = 0;
	public Tick(Game game){
		this.currentGame = game;
		frameTime = System.currentTimeMillis();
	}

	public void actionPerformed(ActionEvent arg0) {
		//code executed every tick
		currentGame.render();
		long difference = System.currentTimeMillis() - frameTime;
		if(difference == 0)
			difference = 1;
		System.out.println("Rendering at " + 1000/difference + " FPS at " + 
		currentGame.getScreen().getWidth() + "x" + currentGame.getScreen().getHeight());
		frameTime = System.currentTimeMillis();
	}


}
