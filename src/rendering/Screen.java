package rendering;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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
	
	public Screen(Game game){
		super();
		this.currentGame = game;
		this.render = new Raycaster(game);
	}
	
	@Override
	public void paintComponent(Graphics g){
		//Overriding the JPanel painting
		Graphics2D g2d = (Graphics2D) g;
		double playerHeight = currentGame.getPlayer().getHeight();
		g2d.setColor(Color.CYAN);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight()/2);
		g2d.setColor(Color.GREEN);
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
		int beforeLastTopPoint = -1;
		int lastHeight = -1;
		int beforeLastHeight = -1;
		Point lastMap = new Point(0,0);
		Point beforeLastMap = new Point(0,0);
		
		try {
			/* Painting the screen from raycasting data for this frame
			 * Given: two arraylists. One of heights, one of points
			 */
			for(int i = 0; i < currentGame.getScreen().getWidth(); ++i){ //iterating through each column
				ArrayList<Double> distanceList = render.getColumn(i);
				ArrayList<Point> columnPoints = render.getPointOfColumn(i);
				for(int j = 0; j < distanceList.size(); j++){
					Point currentMap = columnPoints.get(j);
					Color thisColor = currentGame.getWorld().getColorAt(currentMap.getX(), currentMap.getY());
					double thisHeight = currentGame.getWorld().getHeightAt(currentMap.getX(), currentMap.getY());
					double thisGap = currentGame.getWorld().getGapAt(currentMap.getX(), currentMap.getY());
					if(thisColor.getRGB() != Color.WHITE.getRGB()){
						double columnHeight = this.getHeight()/distanceList.get(j);
						g2d.setColor(thisColor);
						double heightAdjustment = columnHeight*thisHeight;
						double gapAdjustment = columnHeight*thisGap;
						double startingHeight = heightAdjustment + gapAdjustment;
						double firstPoint = Math.floor((this.getHeight()/2 - columnHeight/2) + 
								(columnHeight)*playerHeight - startingHeight);
						double secondPoint = Math.floor(firstPoint + columnHeight + heightAdjustment);
						if(lastMap.equals(currentMap)){
							g2d.drawLine(i,  (int) Math.round(lastTopPoint), i, (int) Math.round(secondPoint));
						}
						g2d.setColor(Color.BLACK);
						g2d.drawLine(i,  (int) Math.round(firstPoint - 1), i, (int) Math.round(firstPoint));
						g2d.setColor(thisColor);
						g2d.drawLine(i,  (int) Math.round(firstPoint), i, (int) Math.round(secondPoint));
						g2d.setColor(Color.BLACK);
						g2d.drawLine(i,  (int) Math.round(secondPoint - 1), i, (int) Math.round(secondPoint));
						beforeLastTopPoint = lastTopPoint;
						lastTopPoint = (int) Math.round(firstPoint);
						lastMap = currentMap;
					}
				}
			}
		}
		catch(Exception e){
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
	
	public void setFrameRate(double frames){
		this.frameRate = frames;
	}
}
