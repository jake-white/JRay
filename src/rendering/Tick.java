package rendering;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import game.Game;
import physics.Physics;

public class Tick implements ActionListener{
	Game currentGame;
	long frameTime = 0, frameRate = 0;
	Physics physics;
	JFrame frame;
	
	public Tick(Game game, JFrame frame){
		this.currentGame = game;
		this.frame = frame;
		frameTime = System.currentTimeMillis();
		physics = new Physics(game, frame);
	}

	public void actionPerformed(ActionEvent arg0) {
		//code executed every tick
		currentGame.render(frameRate);
		physics.tick();
		long difference = System.currentTimeMillis() - frameTime;
		if(difference == 0)
			difference = 1;
		frameRate = 1000/difference;
		frameTime = System.currentTimeMillis();
	}


}
