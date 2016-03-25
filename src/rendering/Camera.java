package rendering;

import game.Player;

public class Camera {
	double FOV = Math.PI/2;
	Player player;
	
	public Camera(Player p){
		this.player = p;
	}

	public double getAngle(){
		return player.getAngle();
	}
	
	public double getFOV(){
		return FOV;
	}
	public double getHeight(){
		return player.getHeight() + 0.8;
	}

}
