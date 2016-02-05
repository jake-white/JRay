package rendering;
import java.awt.Color;
import java.util.ArrayList;

import game.Game;

public class Raycaster {
	Game currentGame;
	ArrayList<Double> columns;
	ArrayList<Color> colors;
	
	public Raycaster(Game game){
		int screenWidth = 800;
		columns = new ArrayList<Double>();
		colors = new ArrayList<Color>();
		for(int i = 0; i < screenWidth; ++i){
			columns.add(Double.NaN);
			colors.add(new Color(255, 255, 255));
		}
		this.currentGame = game;
	}
	public void cast(){
		columns.clear();
		colors.clear();
		int screenWidth = currentGame.getScreen().getWidth();
		double FOV = currentGame.getPlayer().getFOV();
		double playerAngle = currentGame.getPlayer().getAngle();
		double endingAngle = validateAngle(playerAngle - FOV/2);
		double startingAngle = validateAngle(playerAngle + FOV/2);
		double increment = FOV/screenWidth;
		double currentAngle = startingAngle;
		double maxX = currentGame.getWorld().getWidth() - 1;
		double maxY = currentGame.getWorld().getHeight() - 1;
		RayPoint playerPoint = currentGame.getPlayer().getPosition();
		double playerY = playerPoint.getY();
		double playerX = playerPoint.getX();
		double dX = 0, dY = 0;
		
		for(int i = 0; i < screenWidth; ++i, currentAngle-=increment){
			double fisheye = Math.cos(Math.abs(currentAngle-playerAngle));
			currentAngle = validateAngle(currentAngle);
			boolean posDirX = currentAngle < Math.PI/2 || currentAngle > Math.PI *(3/2);
			boolean posDirY = currentAngle < Math.PI;
			double startX, startY, deltaX, deltaY, distFromPlayerX, distFromPlayerY;
			//checking horizontally
            if(posDirY){
                startY = (int) playerY + 1;
                deltaY = 1;
            }
            else{
                startY = (int) playerY;
                deltaY = -1;
            }
            distFromPlayerY = Math.abs(startY - playerY);
            distFromPlayerX = Math.abs((distFromPlayerY/Math.tan(currentAngle)));
            if(posDirX){
                startX = playerX + distFromPlayerX;
                deltaX = Math.abs(deltaY/Math.tan(currentAngle));
            }
            else{
                startX = playerX - distFromPlayerX;
                deltaX = -Math.abs(deltaY/Math.tan(currentAngle));
            }
            boolean hit = false;
            double currentX = startX;
            double currentY = startY;
            double distanceX = 0, distanceY;
            Color colorX = null, colorY;
            double checkX = currentX;
            double checkY = currentY;
            
            while(!hit){
                checkX = currentX;
                if(posDirY)
                    checkY = currentY;
                else
                    checkY = currentY - 1;
                if (checkX > maxX || checkY > maxY || checkX < 0 || checkY < 0) {
                    distanceX = -1;
                    hit = true;
                }
                else if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
                    hit = true;
                    distanceX = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
                    colorX = currentGame.getWorld().getColorAt(checkX, checkY);
                }

                currentX += deltaX;
                currentY += deltaY;
            }

            //checking vertically
            if(posDirX){
                startX = (int) playerX + 1;
                deltaX = 1;
            }
            else{
                startX = (int) playerX;
                deltaX = -1;
            }
            distFromPlayerX = Math.abs(startX - playerX);
            distFromPlayerY = Math.abs(distFromPlayerX*Math.tan(currentAngle));
            if(posDirY){
                startY = playerY + distFromPlayerY;
                deltaY = Math.abs(Math.tan(currentAngle)*(deltaX));
            }
            else{
                startY = playerY - distFromPlayerY;
                deltaY = -Math.abs(Math.tan(currentAngle)*(deltaX));
            }

            hit = false;
            currentX = startX;
            currentY = startY;
            
            while(!hit){
                checkY = currentY;
                if(posDirX)
                    checkX = currentX;
                else
                    checkX = currentX - 1;
                if(checkX > maxX || checkY > maxY || checkX < 0 || checkY < 0){
                    distanceY = -1;
                    hit = true;
                }
                else if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
                    hit = true;
                    distanceY = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
                    colorY = currentGame.getWorld().getColorAt(checkX, checkY);
                }
            }

                currentX += deltaX;
                currentY += deltaY;
                
            columns.add(distanceX);
            colors.add(colorX);
			
		}
	}
	
	public double getColumn(int col){
		return columns.get(col).doubleValue();
	}

	
	public Color getColor(int col){
		return colors.get(col);
	}

	public double validateAngle(double angle){
	    while(angle >= 2*Math.PI)
	        angle -= 2*Math.PI;
	    while( angle < 0)
	        angle += 2*Math.PI;
	    return angle;
	}
	
    public double distanceTo(double x1, double y1, double  x2, double y2){
        return Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2));
    }
}