package game;

import rendering.RayPoint;

public class Player {
	double height = 0, angle = 0;
	double accelX = 0, accelY = 0;
	RayPoint position;
	
	public Player(){
		position = new RayPoint();
	}
	
	public void setPosition(double d, double e){
		this.position.setLocation(d, e);
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

	public double getAccelX(){
		return accelX;
	}

	public void setAccelX(double accel){
		this.accelX = accel;
	}

	public double getAccelY(){
		return accelY;
	}

	public void setAccelY(double accel){
		this.accelY = accel;
	}
	
	public double getHeight(){
		return height;
	}

	public void setHeight(double height){
		this.height = height;
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

	public double getAngle() {
		return angle;
	}
	
	public String toString(){
		return position.toString() + ": " + this.height;
	}
}
