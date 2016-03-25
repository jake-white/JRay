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

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import game.Game;

public class Screen extends JPanel{
	Game currentGame;
	Raycaster render;
	double frameRate;
	int resX = 480, resY = 270;
	
	public Screen(Game game){
		super();
		this.currentGame = game;
		this.render = new Raycaster(game);
	}
	
	@Override
	public void paintComponent(Graphics g){
		//Overriding the JPanel painting
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
		          RenderingHints.VALUE_RENDER_QUALITY);
		
		double cameraHeight = currentGame.getCamera().getHeight();
		g2d.setColor(Color.BLACK.brighter());
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight()/2);
		g2d.setColor(Color.GREEN.darker());
		g2d.fillRect(0, this.getHeight()/2, this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);
		g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		BufferedImage yanmega = null;
		try {
			File file = new File("res/yanmega.png");
			yanmega = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int lastTopPoint = -1;
		int lastXValue = 0;
		Point lastMap = new Point(0,0);
		Tile lastTile = new Tile(TileType.EMPTY);
		
		try {
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
						double columnHeight = -1;
						if(distanceList.get(j) > 0){
							columnHeight = this.getHeight()/distanceList.get(j);
						}
						g2d.setColor(thisColor);
						double castedHeight = columnHeight;
						columnHeight*=thisHeight;
						Color adjustedColor = transformColor(thisColor, castedHeight);
						double gapAdjustment = castedHeight*thisGap;
						//calculating first and last points of the columns
							double firstPoint = 0;
							double length = 0;
						if(cameraHeight > thisGap){
							firstPoint = this.getHeight();
							length = 0;
						}
						g2d.setColor(adjustedColor);
						if(columnHeight > 0){
							firstPoint = Math.floor(this.getHeight()/2 - columnHeight - gapAdjustment + castedHeight*cameraHeight);
							length = columnHeight;
						}
						if(length > 1000)
							System.out.println(castedHeight);
						if(lastMap.equals(currentMap)){ //checking it a top should be drawn for a block
							if(lastTopPoint < firstPoint+length){
								g2d.fillRect(lastXValue,  (int) Math.round(lastTopPoint), (int) dX, (int) Math.round(firstPoint+length-lastTopPoint));
							}
							else{
								g2d.fillRect(lastXValue,  (int) Math.round(firstPoint+length), (int) dX, (int) Math.round(lastTopPoint - (firstPoint + length)));
							}
						}
						if(columnHeight > 0){
							g2d.fillRect(lastXValue,  (int) Math.round(firstPoint), (int) dX, (int) Math.round(length+1));
						}
						if(currentTile.getType() != TileType.CEILING){
							g2d.setColor(Color.BLACK);
							g2d.fillRect(lastXValue,  (int) Math.round(firstPoint), (int) dX, 2);
							g2d.fillRect(lastXValue,  (int) Math.round(firstPoint + length), (int) dX, 1);
						}
						lastTopPoint = (int) Math.round(firstPoint);
						lastMap = currentMap;
						lastTile = currentTile;
					}
				}
				lastXValue+= dX;
			}
		}
		catch(Exception e){
			//e.printStackTrace();
			System.out.println("Something went wrong trying to paint to the screen, but we caught it.");
		}
	//	g2d.drawImage(yanmega, this.getWidth()/2, this.getHeight()/2, null);
		g2d.setFont(new Font(null).deriveFont(20f));
		g2d.setColor(Color.RED);
		g2d.drawString("FPS: " + frameRate, 0, 20);
		g2d.drawString("Player: " + currentGame.getPlayer().toString(), 0, 40);
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public Color transformColor(Color original, double distance){
		if(distance > 0){
		double intensity = 1 - 20/distance;
		//System.out.println(intensity);
		if(intensity > 1)
			intensity = 1;
		else if(intensity < 0)
			intensity = 0;
		
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
		else
			return original;
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
