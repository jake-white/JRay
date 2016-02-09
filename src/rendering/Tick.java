package rendering;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import flight.Physics;
import game.Game;

public class Tick implements ActionListener{
	Game currentGame;
	long frameTime = 0, frameRate = 0;
	Physics physics;
	
	public Tick(Game game){
		this.currentGame = game;
		physics = new Physics(game);
		frameTime = System.currentTimeMillis();
	}

	public void actionPerformed(ActionEvent arg0) {
		//code executed every tick
		currentGame.render(frameRate);
		physics.calculatePhys();
		long difference = System.currentTimeMillis() - frameTime;
		if(difference == 0)
			difference = 1;
		frameRate = 1000/difference;
		frameTime = System.currentTimeMillis();
	}


}
