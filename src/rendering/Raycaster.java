package rendering;

import game.Game;

public class Raycaster {
	Game currentGame;
	public Raycaster(Game game){
		this.currentGame = game;
	}
	public void cast(){
		int screenWidth = currentGame.getScreen().getWidth();
		double FOV = currentGame.getPlayer().getFOV();
		double startingAngle = validateAngle(currentGame.getPlayer().getAngle() - FOV/2);
		double endingAngle = validateAngle(currentGame.getPlayer().getAngle() + FOV/2);
		double increment = FOV/screenWidth;
		for(double currentAngle = startingAngle; currentAngle < endingAngle; currentAngle+=increment){
			currentAngle = validateAngle(currentAngle);
			boolean posDirX = currentAngle < Math.PI/2 | currentAngle > (3/2)*Math.PI;
		    boolean posDirY = currentAngle > Math.PI;
			//setting up some necessary values for this particular angle
		}
	}


	public double validateAngle(double angle){
	    while(angle >= 2*Math.PI)
	        angle -= 2*Math.PI;
	    while( angle < 0)
	        angle += 2*Math.PI;
	    return angle;
	}
}