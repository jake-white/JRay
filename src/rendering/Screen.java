package rendering;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
		for(int i = 0; i < currentGame.getScreen().getWidth(); ++i){
			ArrayList<Double> distanceList = render.getColumn(i);
			ArrayList<Color> colorList = render.getColor(i);
			ArrayList<Integer> columnLengths = render.getHeights(i);
			for(int j = 0; j < distanceList.size(); j++){
				if(colorList.get(j).getRGB() != Color.WHITE.getRGB()){
					double columnHeight = this.getHeight()/distanceList.get(j);
					Color thisColor = colorList.get(j);
					g2d.setColor(thisColor);
					double heightAdjustment = columnHeight*(columnLengths.get(j)-1);
					double firstPoint = Math.floor((this.getHeight()/2 - columnHeight/2) + 
							columnHeight*playerHeight - heightAdjustment);
					double secondPoint = Math.floor(firstPoint + columnHeight + heightAdjustment);
					g2d.drawLine(i,  (int) Math.round(firstPoint), i, (int) Math.round(secondPoint));
					g2d.setColor(Color.BLACK);
					g2d.drawLine(i,  (int) Math.round(firstPoint - 1), i, (int) Math.round(firstPoint));
					g2d.drawLine(i,  (int) Math.round(secondPoint - 1), i, (int) Math.round(secondPoint));
				}
			}
		}
		g2d.drawImage(yanmega, this.getWidth()/2, this.getHeight()/2, null);
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public void display(){
		
	}
}
