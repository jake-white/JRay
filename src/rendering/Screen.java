package rendering;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game.Game;

public class Screen extends JPanel{
	Game currentGame;
	Raycaster render;
	double frameRate;
	int resX = 200, resY = 112;
	ArrayList<Point> lightSources;
	private Sprite middlePixelSprite;
	private BufferedImage skybox;
	private double ppRadian; //pixels per radian of the skybox
	
	public Screen(Game game){
		super();
		this.currentGame = game;
		this.render = new Raycaster(game);

		try {
			skybox = ImageIO.read(new File("res/skybox.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ppRadian = skybox.getWidth()/(2*Math.PI);
	}
	
	@Override
	public void paintComponent(Graphics g){
		//Overriding the JPanel painting
		Graphics2D g2d = (Graphics2D) g;
		
		/* skybox math is kinda dumb tbh
		 * so here goes nothing
		 */
		double angleStart = 2*Math.PI - currentGame.getCamera().getStartingAngle();
		double angleEnd = 2*Math.PI - currentGame.getCamera().getEndingAngle();
		int skyBoxStart = (int) (ppRadian*angleStart);
		int skyBoxEnd = (int) (ppRadian*angleEnd);
		int skyBoxLength = (int) (ppRadian*currentGame.getCamera().getFOV());
		if(angleStart > angleEnd){
			int firstLength = skybox.getWidth() - skyBoxStart;
			double firstRatio = (double)(firstLength)/(double)(skyBoxLength);
			int firstActualLength = (int) (firstRatio*this.getWidth());
			
			if(firstLength != 0){
				BufferedImage firstHalf = skybox.getSubimage(skyBoxStart, 0, firstLength, skybox.getHeight());
				g2d.drawImage(firstHalf, 0, 0, firstActualLength, this.getHeight(), null);
			}
			
			if(skyBoxEnd != 0){
				BufferedImage secondHalf = skybox.getSubimage(0, 0, skyBoxEnd, skybox.getHeight());
				g2d.drawImage(secondHalf, firstActualLength, 0, this.getWidth()-firstActualLength, this.getHeight(), null);
			}
		}
		else{
			BufferedImage sliver = skybox.getSubimage(skyBoxStart, 0, skyBoxEnd - skyBoxStart, skybox.getHeight());
			g2d.drawImage(sliver, 0, 0, this.getWidth(), this.getHeight(), null);
		}
		
		double cameraHeight = currentGame.getCamera().getHeight();
		double cameraAngle = currentGame.getCamera().getView();
		g2d.setColor(new Color(16,16,16));
		g2d.fillRect(0, this.getHeight()/2, this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);
		g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		int lastTopPoint = -1;
		int lastXValue = 0;
		Point lastMap = new Point(0,0);
		Tile lastTile = new Tile(TileType.EMPTY);
		ArrayList<Strip> stripList = new ArrayList<Strip>();
		try {
			lightSources = this.currentGame.getWorld().getLightSources();
			/* Painting the screen from raycasting data for this frame
			 * Given: two arraylists. One of heights, one of points
			 */
			double increment = (double) this.getWidth()/this.getResolutionX();
			for(int i = 0; i < this.getResolutionX(); i++){ //iterating through each column
				double dX = increment+1;
				ArrayList<Double> distanceList = render.getColumn(i);
				ArrayList<Point> columnPoints = render.getPointOfColumn(i);
				for(int j = 0; j < distanceList.size(); j++){
					//grabbing information about the current column/tile
					Point currentMap = columnPoints.get(j);
					Tile currentTile = currentGame.getWorld().getTileAt(currentMap);
					Color thisColor = currentTile.getColor();
					double thisHeight = currentTile.getHeight();
					double thisGap = currentTile.getGap();
					if(!thisColor.equals(Color.WHITE)){
						double columnHeight = 0;
						boolean weirdValueWarning = false;
						if(distanceList.get(j) == 0.0){ //weird value here
							columnHeight = Integer.MAX_VALUE;
							weirdValueWarning = true;
						}
						else
							columnHeight = this.getHeight()/distanceList.get(j);
						g2d.setColor(thisColor);
						double castedHeight = columnHeight;
							Color adjustedColor = transformColor(thisColor, currentMap, lightSources);
						double gapAdjustment = castedHeight*thisGap;
						//calculating first and last points of the columns
							double firstPoint = 0;
							double length = 0;
						g2d.setColor(adjustedColor);
						firstPoint = Math.floor(this.getHeight()/2 + castedHeight*(cameraHeight - thisGap - thisHeight))*cameraAngle;
						length = castedHeight*thisHeight;

						if(lastTile.equals(currentTile) || weirdValueWarning){ //checking it a top should be drawn for a block
							
							if(lastTopPoint < firstPoint){
								if(weirdValueWarning){
									firstPoint = this.getHeight();
									length = 0;
								}
								double columnLengthTemp = Math.round(firstPoint+length-lastTopPoint);
								stripList.add(new Strip(lastXValue,  (int) Math.round(lastTopPoint), (int) dX, (int) columnLengthTemp, castedHeight, adjustedColor));
							}
							else{
								if(weirdValueWarning){
									firstPoint = 0;
									length = 0;
								}
								double columnLengthTemp = Math.round(lastTopPoint - (firstPoint + length));
								stripList.add(new Strip(lastXValue, (int) Math.round(firstPoint+length), (int) dX, (int) columnLengthTemp, castedHeight, adjustedColor));
							}
						}
						else{
						//	length-=(10/(castedHeight*thisHeight)*(1/cameraAngle));
						}
						if(columnHeight > 0){
							stripList.add(new Strip(lastXValue,  (int) Math.round(firstPoint), (int) dX, (int) Math.round(length+1), castedHeight, adjustedColor));
						}
						if(currentTile.getType() != TileType.CEILING){
							stripList.add(new Strip(lastXValue,  (int) Math.round(firstPoint), (int) dX, 2, castedHeight, Color.BLACK));
							stripList.add(new Strip(lastXValue,  (int) Math.round(firstPoint + length), (int) dX, 1, castedHeight, Color.BLACK));
						}
						lastTopPoint = (int) Math.round(firstPoint);
						lastMap = currentMap;
						lastTile = currentTile;
					}
				}
				lastXValue+= dX;
			}

			stripList.addAll(this.currentGame.getWorld().getSpriteList());
			Boss b = currentGame.getWorld().getBoss();
			if(b.isActive()){
				stripList.add(b);
			}
			Collections.sort(stripList, new StripComparator());
			middlePixelSprite = null;
			for(int i = 0; i < stripList.size(); ++i){ //actually drawing stuff to the screen by distance
				if(stripList.get(i) instanceof Sprite){
					Sprite workingSprite = (Sprite) stripList.get(i);
					if(workingSprite.isVisible()){
						int x1 = workingSprite.getX(), y1 = workingSprite.getY();
						int x2 = x1 + (int)(workingSprite.getWidth());
						int y2 = y1 + (int)(workingSprite.getHeight());
						g2d.drawImage(workingSprite.getImage(), x1, y1, workingSprite.getWidth(), workingSprite.getHeight(), null);
						if(x1 <= this.getWidth()/2 && y1 <= this.getHeight()/2 && x2 >= this.getWidth()/2 && y2 >= this.getHeight()/2){
							//it is in the center of the crosshairs. this is dumb.
							middlePixelSprite = workingSprite;
						}
					}
				}
				else{
					g2d.setColor(stripList.get(i).getColor());
					g2d.fillRect(stripList.get(i).getX(), stripList.get(i).getY(), stripList.get(i).getWidth(), stripList.get(i).getLength());
					int x1 = stripList.get(i).getX(), y1 = stripList.get(i).getY();
					int x2 = stripList.get(i).getX() + stripList.get(i).getWidth();
					int y2 = stripList.get(i).getY() + stripList.get(i).getLength();
					if(x1 <= this.getWidth()/2 && y1 <= this.getHeight()/2 && x2 >= this.getWidth()/2 && y2 >= this.getHeight()/2){
						//it is in the center of the crosshairs. this is dumb.
						middlePixelSprite = null;
					}
				}
			}
			BufferedImage gunImage = currentGame.getGun().getImage();
			double gunRatio = (double) (gunImage.getWidth())/(gunImage.getHeight());
			double gunHeight = this.getHeight()/2;
			double gunWidth = gunHeight*gunRatio;
			g2d.drawImage(gunImage, (int) (this.getWidth()/2 - gunWidth/2), this.getHeight()/2, (int) gunWidth, (int) gunHeight, null);
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("Something went wrong trying to paint to the screen, but we caught it.");
		}
		g2d.setFont(new Font(null).deriveFont(20f));
		g2d.setColor(Color.RED);
		g2d.drawString("FPS: " + frameRate, 0, 20);
		g2d.drawString("Player: " + currentGame.getPlayer().toString(), 0, 40);

		g2d.setColor(Color.red);
		g2d.drawLine(this.getWidth()/2 + 20,  this.getHeight()/2, this.getWidth()/2 + 5, this.getHeight()/2);
		g2d.drawLine(this.getWidth()/2 - 20,  this.getHeight()/2, this.getWidth()/2 - 5, this.getHeight()/2);
		g2d.drawLine(this.getWidth()/2,  this.getHeight()/2 - 20, this.getWidth()/2, this.getHeight()/2 - 5);
		g2d.drawLine(this.getWidth()/2,  this.getHeight()/2 + 20, this.getWidth()/2, this.getHeight()/2 + 5);
		
		if(currentGame.isOver()){
			g2d.setColor(Color.RED);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d.setColor(Color.BLACK);
			g2d.drawString("GAME OVER", this.getWidth()/3, this.getHeight()/2);
		}
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public Sprite getMiddlePixelSprite(){
		return middlePixelSprite;
	}
	
	public Color transformColor(Color original, Point current, ArrayList<Point> lightSources){
		double intensity = 1;
		ArrayList<Double> distances = new ArrayList<Double>();
		for(int i = 0; i < lightSources.size(); ++i){
			distances.add(RayPoint.distanceTo(current.getX(), current.getY(), lightSources.get(i).getX(), lightSources.get(i).getY()));
		}
		Collections.sort(distances);
		intensity -= distances.get(0)/15;
		if(intensity > 1)
			intensity = 1;
		else if(intensity < 0.3)
			intensity = 0.3;
		
		int red = (int) (original.getRed()*intensity);
		int blue = (int) (original.getBlue()*intensity);
		int green = (int) (original.getGreen()*intensity);
		if(red > 255)
			red = 255;
		if(blue > 255)
			blue = 255;
		if(green > 255)
			green = 255;
		return new Color(red, green, blue);
	}
	
	public void setFrameRate(double frames){
		this.frameRate = frames;
	}
	
	public int getResolutionX(){
		return resX;
	}
	
	public int getResolutionY(){
		return resY;
	}
}
