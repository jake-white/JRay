package rendering;
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
			if(render.getColumn(i) != Double.NaN){
				double columnHeight = this.getHeight()/render.getColumn(i);
				g2d.setColor(render.getColor(i));
				double secondPoint = Math.floor((this.getHeight()/2 + columnHeight));
				g2d.drawLine(i,  this.getHeight()/2 - (int) columnHeight, i, (int) secondPoint);
			}
		}
	}
	
	public void rayCast(){
		render.cast();
	}
	
	public void display(){
		
	}
}
