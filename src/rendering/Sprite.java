package rendering;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import audio.SFX;
import game.Game;
import game.Player;

public class Sprite extends Strip{
	private BufferedImage img, nextAnim;
	private String fileName;
	private Camera c;
	protected double x;
	protected double y;
	protected double zpos = 0;
	double[] accel = {0,0,0};
	protected Game game;
	//actual game values now
	private double attack, ratio;
	protected int hp = 40;
	protected boolean alive = true;
	private boolean hasAnimated = true;
	private final int animDuration = 5;
	private int ticksSinceHit = animDuration;
	private double walk_speed = 0.06;
	private final double radius = 10;
	protected double heightmod = 1;
	private double lastAttack = 0, attackSpeed = 1000;
	private SFX[] damageSounds;
	
	public Sprite(double x, double y, String fileName, Game game){
		super(0, 0, 0, 0, 0, null);
		this.x = x;
		this.y = y;
		this.game = game;
		this.c = game.getCamera();
		this.fileName = fileName;
		this.damageSounds = new SFX[3];
		try {
			for(int i = 0; i < damageSounds.length; ++i){
				this.damageSounds[i] = new SFX("res/sfx/damage"+(i+1)+".wav");
			}
			img = ImageIO.read(new File(fileName));
			this.ratio = (double) (img.getWidth())/(img.getHeight());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getFileName(){
		return fileName;
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
		return RayPoint.validateAngleDiff(c.getAngle(), getRelativeAngle());
	}
	
	public boolean isVisible(){
		if(Math.abs(this.getCameraRelAngle()) > c.getFOV()/2)
			return false;
		else if(!isAlive())	return false;
		return true;
	}
	
	public boolean isAlive(){
		return alive && hp > 0;
	}
	
	public double getAngle(){
		return RayPoint.validateAngle(Math.PI + getRelativeAngle()); //pointing towards camera at all times
	}
	
	public void makeDecision(){
		if(isAlive() && isInWalkingRadius()){
			walk(walk_speed);
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
		 return (int) Math.floor(game.getScreen().getHeight()/2 + (this.getHeight()/heightmod)*(c.getHeight() - zpos - heightmod));
	}
	
	public double getVisualDistance(){
		//this one incorporates fisheye for the purpose of realistic, relative viewing to work with its surroundings
		double fisheye = Math.cos(Math.abs(getCameraRelAngle()));
		return RayPoint.distanceTo(x, y, c.getX(), c.getY())*fisheye;
	}
	
	public double getActualDistance(){
		//this one is plain actual distance for radius, attacks, etc. that aren't visual-based
		return RayPoint.distanceTo(x, y, c.getX(), c.getY());
	}
	
	@Override
	public Double getCast(){
		return (double) game.getScreen().getHeight()/this.getVisualDistance();
	}
	
	public double getZPos(){
		return zpos;
	}
	
	public int getHeight(){
		double height = game.getScreen().getHeight()*heightmod;
		return (int) Math.round((height/this.getVisualDistance()));
	}
	
	public int getWidth(){
		return (int) (this.getHeight()*ratio);
	}

	public double getPositionX() {
		return x;
	}

	public double getPositionY() {
		return y;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setZ(double z) {
		this.zpos = z;
	}

	public boolean isInMusicRadius() {
		return this.getActualDistance() < radius;
	}

	public boolean isInWalkingRadius() {
		return this.getActualDistance() < radius && this.getActualDistance() >= 1;
	}
	
	public boolean isInAttackingRadius(){
		if(Math.abs(this.getZPos() - game.getPlayer().getZ()) < 1)
			return this.getActualDistance() <= 1;
		else return false;
	}
	public double getBottom() {
		return zpos;
	}
	public double getTop() {
		return zpos + heightmod;
	}
	public void attack(Player player, int dmg) {
		if(lastAttack == 0 || System.currentTimeMillis() - lastAttack >= attackSpeed){
			int random = (int) Math.round((Math.random()*(damageSounds.length-1)));
			game.playSFX(damageSounds[random]);
			player.damage(dmg);
			lastAttack = System.currentTimeMillis();
		}
	}

}
