package rendering;

import game.Game;
import game.Player;

public class Projectile extends Sprite{
	private int dmg;
	private double angle;
	private double force, ticksInAir;

	public Projectile(double x, double y, double z, String fileName, Game game, int dmg, double force) {
		super(x, y, fileName, game);
		this.zpos = z;
		this.dmg = dmg;
		this.hp = 10;
		this.heightmod = .5;
		this.angle = this.getAngle();
		this.force = force;
		this.accel[2] = -0.001;
		force *= 1.1;
	}
	
	@Override
	public void makeDecision(){
		if(this.getAccelZ() >= 0 || zpos <= 0){
			this.alive = false;
		}
		force/=1.1;
		if(this.isAlive()){
			this.walk(force/10);
		}
	}
	
	@Override
	public void walk(double inc){
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
	
	@Override
	public void attack(Player p, int dmg){
		super.attack(p, dmg);
	}

}
