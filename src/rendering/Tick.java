package rendering;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Game;

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
		currentGame.getPlayer().turn();
		currentGame.getPlayer().up();
		long difference = System.currentTimeMillis() - frameTime;
		if(difference == 0)
			difference = 1;
		System.out.println("Rendering at " + 1000/difference + " FPS at " + 
		currentGame.getScreen().getWidth() + "x" + currentGame.getScreen().getHeight());
		frameTime = System.currentTimeMillis();
	}


}
