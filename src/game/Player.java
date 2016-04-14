package game;

import rendering.RayPoint;

public class Player {
	double z = 0, angle = 0;
	double height  = 1;
	double[] accel = new double[3];
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
	
	public RayPoint getPosition(){
		return position;
	}

	public double getAccelX(){
		return accel[0];
	}

	public void setAccelX(double accel){
		this.accel[0] = accel;
	}

	public double getAccelY(){
		return accel[1];
	}

	public void setAccelY(double accel){
		this.accel[1] = accel;
	}	

	public double getAccelZ(){
		return accel[2];
	}

	public void setAccelZ(double accel){
		this.accel[2] = accel;
	}	
	
	public void setZ(double height){
		z=height;
	}
	
	public double getZ(){
		return z;
	}
	
	public double getHeight(){
		return height;
	}
	
	public double getBottom(){
		return z;
	}
	
	public double getTop(){
		return z+height;
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
            
        this.setAccelX(this.getAccelX() + xDir*Math.abs(Math.cos(angle))*inc);
        this.setAccelY(this.getAccelY() + yDir*Math.abs(Math.sin(angle))*inc);
		
	}
	
	public void strafe(double inc){
		double currentAngle = RayPoint.validateAngle(angle);
		if(inc > 0)
			currentAngle += Math.PI/2;
		else
			currentAngle -= Math.PI/2;
		currentAngle = RayPoint.validateAngle(currentAngle);
		boolean posDirX = currentAngle < Math.PI/2 || currentAngle > (3*Math.PI)/2;
        boolean posDirY = currentAngle > Math.PI;
        int xDir, yDir;
        if(posDirX)
            xDir = 1;
        else
            xDir = -1;
        if(posDirY)
            yDir = 1;
        else
            yDir = -1;
        this.setAccelX(this.getAccelX() + xDir* Math.abs(Math.cos(currentAngle)*inc));
        this.setAccelY(this.getAccelY() + yDir* Math.abs(Math.sin(currentAngle)*inc));
	}
	
	public void up(double inc){
		this.setAccelZ(this.getAccelZ() + inc);
	}

	public double getAngle() {
		return angle;
	}
	
	public String toString(){
		return position.toString() + ": " + this.z;
	}
}
