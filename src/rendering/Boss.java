package rendering;

import game.Game;
import game.Player;

public class Boss extends Sprite{
	
	private boolean active = false;
	private double lastAttack = 0, attackSpeed = 3000;
	
	public Boss(double x, double y, String fileName, Game game) {
		super(x, y, fileName, game);
		this.hp = 200;
		this.active = false;
		this.heightmod = 5;
	}

	public boolean isActive() {
		return active;
	}
	
	@Override
	public void makeDecision(){
		if(isAlive()){
			if(this.getTop() > game.getPlayer().getTop()){ //if its on higher ground than the player
				if(System.currentTimeMillis() - lastAttack >= attackSpeed){
					this.attack(game.getPlayer(), 10);
					lastAttack = System.currentTimeMillis();
				}
			}
		}
	}
	
	@Override
	public void attack(Player p, int dmg){
		double force = (RayPoint.distanceTo(this.x, this.y, game.getPlayer().getPosition().getX(), game.getPlayer().getPosition().getY()));
		double forceMultiplier = (this.getTop() - game.getPlayer().getTop())/100; //difference in heights
		game.getWorld().getProjectiles().add(new Projectile(x, y, this.getTop()/2, "res/sprites/rock.png", game, dmg, force+ force*forceMultiplier));
	}
	
	public int getHeight(){
		return super.getHeight();
	}
	
	public void activate(){
		lastAttack = System.currentTimeMillis();
		active = true;
	}

}
