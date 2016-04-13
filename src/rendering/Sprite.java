package rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Player;

public class Sprite extends Strip{
	private BufferedImage img;
	private String fileName;
	private Camera c;
	private int screenWidth, screenHeight;
	private double x, y;
	
	public Sprite(double x, double y, String fileName, Camera c, int screenWidth, int screenHeight){
		super(0, 0, 0, 0, 0, null);
		this.x = x;
		this.y = y;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.c = c;
		this.fileName = fileName;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage(){
		return this.img;
	}
	
	public double getRelativeAngle(){
		double dx = c.getX() - this.x;
		boolean xPos = c.getX() > this.x;
		double dy = c.getY() - this.y;
		boolean yPos = c.getY() > this.y;
		double angle = 0, relAngle = 0;
		System.out.println("Sprite: " +x+", "+y+". Player: "+c.getX()+", "+c.getY());
		System.out.println("dx = " + dx + ", dy = " + dy);
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
		relAngle = c.getAngle() - angle;
		return relAngle;
	}
	
	public boolean isVisible(){
		System.out.println(this.getRelativeAngle());
		if(Math.abs(this.getRelativeAngle()) > c.getFOV()/2)
			return false;
		return true;
	}
	
	@Override
	public int getX(){
		return (int) Math.round(this.screenWidth/2 + (this.getRelativeAngle()/c.getFOV())*this.screenWidth) - this.getWidth()/2;
	}
	
	public int getY(){
		return (int) Math.round(this.screenHeight/2 + this.getHeight()*(c.getHeight()-1));
	}
	
	public double getDistance(){
		return RayPoint.distanceTo(x, y, c.getX(), c.getY());
	}
	
	@Override
	public Double getCast(){
		return (double) this.getHeight();
	}
	
	public int getHeight(){
		return (int) Math.round(screenHeight/this.getDistance());
	}
	
	public int getWidth(){
		return this.getHeight();
	}

}
