package flight;

import game.Game;
import game.Player;

public class Physics {
	Game game;
	Player player;
	
	public Physics(Game game){
		this.game = game;
		this.player = game.getPlayer();
	}
	
	public void calculatePhys(){
		validateHeight();
		player.setHeight(player.getHeight() + player.getAccelY());
		player.setPosition(player.getPosition().getX() + player.getAccelX(), player.getPosition().getY() + player.getAccelY());
	}
	
	private void validateHeight(){
		if(player.getHeight() < 0){
			player.setAccelY(0);
			player.setHeight(0);
		}
	}
}
