package rendering;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
	public void paintComponent(Graphics g){ //Overriding the JPanel painting
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
		for(int i = 0; i < currentGame.getScreen().getWidth(); ++i){
			if(render.getColor(i) != Color.WHITE){
				double columnHeight = this.getHeight()/render.getColumn(i);
				g2d.setColor(render.getColor(i));
				double firstPoint = Math.floor((this.getHeight()/2 - columnHeight/2));
				double secondPoint = Math.floor((this.getHeight()/2 + columnHeight/2));
				g2d.drawLine(i,  (int) firstPoint, i, (int) secondPoint);
			}
		}
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public void display(){
		
	}
}
