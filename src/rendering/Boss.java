package rendering;

import game.Game;

public class Boss extends Sprite{
	
	private boolean active = false;

	public Boss(double x, double y, String fileName, Camera c, Game game) {
		super(x, y, fileName, c, game);
		this.hp = 200;
		this.active = false;
		this.heightmod = 5;
	}

	public boolean isActive() {
		return active;
	}
	
	public int getHeight(){
		return super.getHeight();
	}
	
	public void activate(){
		active = true;
	}

}
