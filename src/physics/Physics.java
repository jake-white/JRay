package physics;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import game.Game;
import game.Player;

public class Physics {
	Game game;
	Player player;
	InputManager input;
	
	public Physics(Game game, JFrame mainFrame){
		this.input = new InputManager();
		mainFrame.addKeyListener(input);
		this.game = game;
		this.player = game.getPlayer();
	}
	
	public void tick(){
		this.calculatePhys();
		this.parseInput(this.player);
	}
	
	public void calculatePhys(){
		validateHeight();
		player.setHeight(player.getHeight() + player.getAccelY());
		player.setPosition(player.getPosition().getX() + player.getAccelX(), player.getPosition().getY() + player.getAccelY());
	}
	
	public void parseInput(Player p){
		double walk_speed = 0.05;
		double turn_speed = 0.02;
		double fly_speed = 0.04;
		if(input.input(KeyEvent.VK_W)){
			p.walk(walk_speed);
		}
		else if(input.input(KeyEvent.VK_S)){
			p.walk(-walk_speed);
		}
		if(input.input(KeyEvent.VK_A)){
			p.strafe(walk_speed);
		}
		else if(input.input(KeyEvent.VK_D)){
			p.strafe(-walk_speed);
		}
		if(input.input(KeyEvent.VK_Q)){
			p.turn(turn_speed);
		}
		else if(input.input(KeyEvent.VK_E)){
			p.turn(-turn_speed);
		}
		if(input.input(KeyEvent.VK_Z)){
			p.up(fly_speed);
		}
		else if(input.input(KeyEvent.VK_X)){
			p.up(-fly_speed);
		}
	}
	
	private void validateHeight(){
		if(player.getHeight() < 0){
			player.setAccelY(0);
			player.setHeight(0);
		}
	}
}
