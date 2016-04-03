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
	
	public Sprite(int x, int y, String fileName, Camera c, int screenWidth, int screenHeight){
		super(x, y, 0, 0, 0, null);
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
		double dx = this.x - c.getX();
		double dy = this.y - c.getY();
		double angle = RayPoint.validateAngle(c.getAngle() - RayPoint.validateAngle(Math.atan2(dy, dx)));
		return angle;		
	}
	
	public boolean isVisible(){
		System.out.println(this.getRelativeAngle());
		if(this.getRelativeAngle() > c.getFOV())
			return false;
		return true;
	}
	
	@Override
	public int getX(){
		return (int) Math.round((this.getRelativeAngle()/c.getFOV())*this.screenWidth);
	}
	
	public double getDistance(){
		return RayPoint.distanceTo(x, y, c.getX(), c.getY());
	}
	
	public int getHeight(){
		System.out.println(this.getDistance());
		return (int) Math.round(screenHeight/this.getDistance());
	}
	
	public int getWidth(){
		return this.getHeight();
	}

}
