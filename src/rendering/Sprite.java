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
	private int screenHeight;
	
	public Sprite(int x, int y, String fileName, Camera c, int screenHeight){
		super(x, y, 0, 0, 0, null);
		this.screenHeight = screenHeight;
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
	
	public boolean isVisible(){
		double dx = Math.abs(this.x - c.getX());
		double dy = Math.abs(this.y - c.getY());
		double angle = Math.abs(c.getAngle() - Math.atan2(dy, dx));
		if(angle > c.getFOV())
			return false;
		return true;
	}
	
	@Override
	public int getX(){
		double dx = Math.abs(this.x - c.getX());
		double dy = Math.abs(this.y - c.getY());
		double angle = Math.abs(c.getAngle() - Math.atan(dy/dx));
		System.out.println(angle/c.getFOV());
		return (int) Math.round((angle/c.getFOV())*this.screenHeight);
	}
	
	public double getDistance(){
		return RayPoint.distanceTo(x, y, c.getX(), c.getX());
	}
	
	public double getHeight(){
		return screenHeight/this.getDistance();
	}

}
