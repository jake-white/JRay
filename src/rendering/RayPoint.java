package rendering;

import java.awt.geom.Point2D;

public class RayPoint extends Point2D.Double{
	public RayPoint() {
		// TODO Auto-generated constructor stub
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	
	public void increaseX(double xInc){
		this.x += xInc;
	}
	
	public void increaseY(double yInc){
		this.y += yInc;
	}
	
	/*kind of hacky, but this static method can be accessed from anywhere in the program
	 *it's used in a lot of the raycasting and movement algorithms
	 *so I put it in this class and it can be called anywhere via
	 *RayPoint.validateAngle(angle);
	 *that way I don't have to define it a bazillion times
	 */
	public static double validateAngle(double angle){
	    while(angle >= 2*Math.PI)
	        angle -= 2*Math.PI;
	    while( angle < 0)
	        angle += 2*Math.PI;
	    return angle;
	}
	
	public static double validateAngleDiff(double angle, double angle2){ //needs to be within -pi and pi
		angle = RayPoint.validateAngle(angle);
		angle2 = RayPoint.validateAngle(angle2);
		double baseDiff = angle - angle2;
	    while(baseDiff >= Math.PI)
	    	baseDiff -= Math.PI*2;
	    while(baseDiff <= -Math.PI)
	    	baseDiff += Math.PI*2;
	    return baseDiff;
	}
	

    public static double distanceTo(double x1, double y1, double  x2, double y2){
        return Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2));
    }
	
	@Override
	public String toString(){
		return "("+this.x+", "+this.y+")";
	}
}

