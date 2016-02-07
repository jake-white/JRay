package rendering;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

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
		for(int i = 0; i < currentGame.getScreen().getWidth(); ++i){
			ArrayList<Double> distanceList = render.getColumn(i);
			ArrayList<Color> colorList = render.getColor(i);
			for(int j = distanceList.size() - 1; j >= 0; --j){
				if(colorList.get(j).getRGB() != Color.WHITE.getRGB()){
					double columnHeight = this.getHeight()/distanceList.get(j);
					Color thisColor = colorList.get(j);
					columnHeight /= 2;
					g2d.setColor(thisColor);
					double firstPoint = Math.floor((this.getHeight()/2 - columnHeight/2) + 
							columnHeight*playerHeight);
					double secondPoint = Math.floor(firstPoint + columnHeight);
					g2d.drawLine(i,  (int) Math.round(firstPoint), i, (int) Math.round(secondPoint));
					g2d.setColor(Color.BLACK);
					g2d.drawLine(i,  (int) Math.round(firstPoint - 1), i, (int) Math.round(firstPoint));
				}
			}
		}
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public void display(){
		
	}
}
