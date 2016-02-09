package game;

import rendering.RayPoint;

public class Player {
	double FOV = Math.PI/2, angle = 0, height = 0;
	RayPoint position;
	
	public Player(){
		position = new RayPoint();
	}
	
	public void setPosition(int x, int y){
		System.out.println(x);
		this.position.setLocation(x, y);
	}
	
	public void turn(double inc){
		angle+= inc;
	}
	
	public void up(double inc){
		height += inc;
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

	public void walk(double inc) {
		angle = RayPoint.validateAngle(angle);
        boolean posDirX = angle < Math.PI/2 || angle > (Math.PI *(3.0/2.0));
        boolean posDirY = angle > Math.PI;
        int xDir, yDir;
        if(posDirX)
            xDir = 1;
        else
            xDir = -1;
        if(posDirY)
            yDir = 1;
        else
            yDir = -1;
            
        this.position.increaseX(xDir*Math.abs(Math.cos(angle))*inc);
        this.position.increaseY(yDir*Math.abs(Math.sin(angle))*inc);
		
	}
}
