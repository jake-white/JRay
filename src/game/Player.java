package game;

import rendering.RayPoint;

public class Player {
	double FOV = Math.PI/2, angle = Math.PI, height = 0;
	RayPoint position;
	
	public Player(){
		position = new RayPoint();
	}
	
	public void setPosition(int x, int y){
		System.out.println(x);
		this.position.setLocation(x, y);
	}
	
	public void turn(){
		angle+= 0.001;
	}
	
	public void up(){
		height += 0.0007;
	}
	
	public RayPoint getPosition(){
		return position;
	}
	
	public double getFOV(){
		return FOV;
	}

	public double getAngle(){
		return angle;
	}

	public double getHeight(){
		return height;
	}
}
