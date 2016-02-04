package rendering;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import game.Game;

public class Screen extends JPanel{
	Game currentGame;
	Raycaster render;
	
	public Screen(Game game){
		this.currentGame = game;
		this.render = new Raycaster(game);
	}
	
	@Override
	public void paintComponent(Graphics g){ //Overriding the JPanel painting
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
	}
	
	public void rayCast(){
		
	}
	
	public void display(){
		
	}
}
