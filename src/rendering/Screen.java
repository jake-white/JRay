package rendering;
import java.awt.Color;
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
			for(int i = 0; i < currentGame.getScreen().getWidth(); ++i){
				ArrayList<Double> distanceList = render.getColumn(i);
				ArrayList<Color> colorList = render.getColor(i);
				ArrayList<Double> columnLengths = render.getHeights(i);
				ArrayList<Point> columnPoints = render.getPointOfColumn(i);
				for(int j = 0; j < distanceList.size(); j++){
					if(colorList.get(j).getRGB() != Color.WHITE.getRGB()){
						int actualHeight = this.getHeight();
						double columnHeight = this.getHeight()/distanceList.get(j);
					//	if(colorList.size() != columnPoints.size())
					//		System.out.println(colorList.size() + " vs " + columnPoints.size() + ", yet we maybe had trouble at element " + j);
						Color thisColor = colorList.get(j);
						Point currentMap = columnPoints.get(j);
						g2d.setColor(thisColor);
						double heightAdjustment = columnHeight*(columnLengths.get(j)-1);
						double firstPoint = Math.floor((this.getHeight()/2 - columnHeight/2) + 
								(columnHeight)*playerHeight - heightAdjustment);
						double secondPoint = Math.floor(firstPoint + columnHeight + heightAdjustment);
						if(lastMap.equals(currentMap)){
						//	System.out.println(lastMap + " vs " + currentMap);
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
						//System.out.println(beforeLastMap + " " + lastMap + " " + currentMap);
					}
				}
			}
		}
		catch(Exception e){
		//	e.printStackTrace();
		//	System.out.println("Something went wrong trying to paint to the screen.");
		}
	//	g2d.drawImage(yanmega, this.getWidth()/2, this.getHeight()/2, null);
		g2d.setColor(Color.RED);
		g2d.drawString("FPS: " + frameRate, 0, 10);
		g2d.drawString("Player: " + currentGame.getPlayer().toString(), 0, 20);
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public void setFrameRate(double frames){
		this.frameRate = frames;
	}
}
