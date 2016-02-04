package game;
import java.awt.Point;

public class Player {
	double FOV = 0, angle = 0;
	Point position;
	
	public Player(){
		
	}
	
	public void setPosition(int x, int y){
		this.position.setLocation(x, y);
	}
	
	public Point getPosition(){
		return position;
	}
	
	public double getFOV(){
		return FOV;
	}

	public double getAngle(){
		return angle;
	}
}
