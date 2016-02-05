package rendering;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.Game;

public class Tick implements ActionListener{
	Game currentGame;
	public Tick(Game game){
		this.currentGame = game;
	}

	public void actionPerformed(ActionEvent arg0) {
		//code executed every tick
		currentGame.render();
		currentGame.getPlayer().turn();
	}


}
