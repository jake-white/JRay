import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Screen extends JPanel{
	Game currentGame;
	public Screen(Game game){
		this.currentGame = game;
	}
	
	@Override
	public void paintComponent(Graphics g){ //Overriding the JPanel painting
		Graphics2D g2d = (Graphics2D) g;
	}
	
	public void rayCast(){
		
	}
	
	public void display(){
		
	}
}
