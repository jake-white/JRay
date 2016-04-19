package rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.Game;
import game.Player;

public class Sprite extends Strip{
	private BufferedImage img;
	private String fileName;
	private Camera c;
	private double x, y;
	private Game game;
	//actual game values now
	private double hp = 20, attack;
	private boolean alive = true;
	
	public Sprite(double x, double y, String fileName, Camera c, Game game){
		super(0, 0, 0, 0, 0, null);
		this.x = x;
		this.y = y;
		this.game = game;
		this.c = c;
		this.fileName = fileName;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void hit(double attack){ //this sprite is getting shot at
		this.hp-=attack;
		if(hp <= 0)
			alive = false;
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
		if(Math.abs(this.getX()) > game.getScreen().getWidth() || this.getX() < -this.getWidth())
			return false;
		else if(!isAlive())	return false;		
		return true;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	@Override
	public int getX(){
		return (int) Math.round(this.game.getScreen().getWidth()/2 + (this.getRelativeAngle()/c.getFOV())*this.game.getScreen().getWidth()) - this.getWidth()/2;
	}
	
	public int getY(){
		return (int) Math.round(this.game.getScreen().getHeight()/2 + this.getHeight()*(c.getHeight()-1));
	}
	
	public double getDistance(){
		return RayPoint.distanceTo(x, y, c.getX(), c.getY());
	}
	
	@Override
	public Double getCast(){
		return (double) this.getHeight();
	}
	
	public int getHeight(){
		return (int) Math.round(this.game.getScreen().getHeight()/this.getDistance());
	}
	
	public int getWidth(){
		return this.getHeight();
	}

}
