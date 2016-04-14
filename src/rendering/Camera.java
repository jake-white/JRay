package rendering;

import game.Player;

public class Camera {
	double FOV = Math.PI/2, x, y;
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
		return player.getZ() + 0.8;
	}
	
	public double getX(){
		return player.getPosition().getX();
	}
	
	public double getY(){
		return player.getPosition().getY();
	}

}
