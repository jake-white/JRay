package rendering;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;
import game.Player;

public class Sprite extends Strip{
	private BufferedImage img, nextAnim;
	private String fileName;
	private Camera c;
	private double x, y, zpos = 1;
	double[] accel = new double[3];
	private Game game;
	//actual game values now
	private double hp = 20, attack, ratio;
	private boolean alive = true , hasAnimated = true;
	private final int animDuration = 15;
	private int ticksSinceHit = animDuration;
	
	public Sprite(double x, double y, String fileName, Camera c, Game game){
		super(0, 0, 0, 0, 0, null);
		this.x = x;
		this.y = y;
		this.game = game;
		this.c = c;
		this.fileName = fileName;
		try {
			img = ImageIO.read(new File(fileName));
			this.ratio = (double) (img.getHeight())/(img.getWidth());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void hit(double attack){ //this sprite is getting shot at
		this.hp-=attack;
		ticksSinceHit = 0;
		//we have to manually clone this image so that they are separate references to different data
		nextAnim = new BufferedImage(img.getColorModel(), img.copyData(null), img.getColorModel().isAlphaPremultiplied(), null);
		for(int x = 0; x < img.getWidth(); ++x){
			for(int y = 0; y < img.getHeight(); ++y){
				if((new Color(img.getRGB(x, y), true).getAlpha() == 255)){
					int r = new Color(img.getRGB(x, y)).getRed();
					int g = (int) (new Color(img.getRGB(x, y)).getGreen()*0.5);
					int b = (int) (new Color(img.getRGB(x, y)).getBlue()*0.5);
					Color newColor = new Color(r,g,b);
					nextAnim.setRGB(x, y, newColor.getRGB());
				}
			}
		}
		if(hp <= 0){
			alive = false;
		}
	}
	
	public BufferedImage getImage(){
		if(ticksSinceHit < animDuration){
			++ticksSinceHit;
			return this.nextAnim;
		}
		else return this.img;
	}
	
	public double getRelativeAngle(){
		double dx = c.getX() - this.x;
		boolean xPos = c.getX() > this.x;
		double dy = c.getY() - this.y;
		boolean yPos = c.getY() > this.y;
		double angle = 0;
		if(xPos && yPos){
			angle = Math.atan(dx/dy) + Math.PI/2;
		}
		else if(xPos && !yPos){
			dx = Math.abs(dx);
			dy = Math.abs(dy);
			angle = Math.atan(dy/dx) + Math.PI;
		}
		else if(!xPos && !yPos){
			angle = Math.atan(dx/dy) + (3*Math.PI)/2;
		}
		else if(yPos && !xPos){
			dx = Math.abs(dx);
			dy = Math.abs(dy);
			angle = Math.atan(dy/dx);
		}
		return angle;
	}
	
	public double getCameraRelAngle(){
		return c.getAngle() - getRelativeAngle();
	}
	
	public boolean isVisible(){
		if(Math.abs(this.getX()) > game.getScreen().getWidth() || this.getX() < -this.getWidth())
			return false;
		else if(!isAlive())	return false;
		System.out.println("visible");
		return true;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public double getAngle(){
		System.out.println(x + ", " + y + " " + getRelativeAngle());
		return RayPoint.validateAngle(Math.PI + getRelativeAngle()); //pointing towards camera at all times
	}
	
	public void makeDecision(){
		if(isAlive()){
			walk(0.02);
		}
	}

	public void walk(double inc) {
		double angle = this.getAngle();
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
	
	@Override
	public int getX(){
		return (int) Math.round(this.game.getScreen().getWidth()/2 + (this.getCameraRelAngle()/c.getFOV())*this.game.getScreen().getWidth()) - this.getWidth()/2;
	}
	
	public int getY(){
		return (int) Math.round(this.game.getScreen().getHeight()/2 + (this.getHeight()/2)*(c.getHeight()-1) - this.getZPos()*this.getHeight()/2);
	}
	
	public double getDistance(){
		return RayPoint.distanceTo(x, y, c.getX(), c.getY());
	}
	
	@Override
	public Double getCast(){
		return (double) game.getScreen().getHeight()/this.getDistance();
	}
	
	public double getZPos(){
		return zpos - 0.5;
	}
	
	public int getHeight(){
		double height = img.getHeight()*10;
		return (int) Math.round((height/this.getDistance()));
	}
	
	public int getWidth(){
		return (int) (this.getHeight()*ratio);
	}

	public double getPositionX() {
		return x;
	}

	public double getPositionY() {
		return x;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setZ(double z) {
		this.zpos = z;
	}

}
