package rendering;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import game.Game;

public class Raycaster {
	Game currentGame;
	ArrayList<ArrayList<Double>> columns;
	ArrayList<ArrayList<Color>> colors;
	
	public Raycaster(Game game){
		this.currentGame = game;
		columns = new ArrayList<ArrayList<Double>>();
		colors = new ArrayList<ArrayList<Color>>();
	}
	public void cast(){
		int screenWidth = currentGame.getScreen().getWidth();
		columns.clear();
		colors.clear();
		double FOV = currentGame.getPlayer().getFOV();
		double playerAngle = currentGame.getPlayer().getAngle();
		double startingAngle = playerAngle + FOV/2;
		double increment = FOV/screenWidth;
		double currentAngle = startingAngle;
		double maxX = currentGame.getWorld().getWidth() - 1;
		double maxY = currentGame.getWorld().getHeight() - 1;
		RayPoint playerPoint = currentGame.getPlayer().getPosition();
		double playerY = playerPoint.getY();
		double playerX = playerPoint.getX();
		
		for(int i = 0; i < screenWidth; ++i, currentAngle-=increment){
			double fisheye = Math.cos(Math.abs(currentAngle-playerAngle));
			currentAngle = RayPoint.validateAngle(currentAngle);
			boolean posDirX = (currentAngle < Math.PI/2) || (currentAngle > (Math.PI *(3.0/2.0)));
			boolean posDirY = currentAngle > Math.PI;
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
            double checkX = currentX;
            double checkY = currentY;
            ArrayList<Double> allHits = new ArrayList<Double>();
            ArrayList<Color> allColors = new ArrayList<Color>();
            
            while(!hit){
                checkX = currentX;
                if(posDirY)
                    checkY = (int) currentY;
                else
                    checkY = (int) currentY - 1;
                if (checkX > maxX || checkY > maxY || checkX < 0 || checkY < 0) {
                    hit = true;
                }
                else if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
                    allHits.add(distanceTo(playerX, playerY, currentX, currentY)*fisheye);
                    allColors.add(currentGame.getWorld().getColorAt(checkX, checkY));
                    allHits.add(distanceTo(playerX, playerY, checkX, checkY)*fisheye);
                    allColors.add(currentGame.getWorld().getColorAt(checkX, checkY));
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
                    checkX = (int) currentX;
                else
                    checkX = (int) currentX - 1;
                if(checkX > maxX || checkY > maxY || checkX < 0 || checkY < 0){
                    hit = true;
                }
                else if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
                    allHits.add(distanceTo(playerX, playerY, currentX, currentY)*fisheye);
                    allColors.add(currentGame.getWorld().getColorAt(checkX, checkY));
                    allHits.add(distanceTo(playerX, playerY, checkX, checkY)*fisheye);
                    allColors.add(currentGame.getWorld().getColorAt(checkX, checkY));
                    
                }
                currentX += deltaX;
                currentY += deltaY;
            }
            
	        if(!allHits.isEmpty()) {
	        	columns.add(allHits);
	        	colors.add(allColors);
	        }
	        else {
	        	ArrayList<Double> NaNList = new ArrayList<Double>();
	        	NaNList.add(Double.NaN);
	        	ArrayList<Color> whiteList = new ArrayList<Color>();
	        	whiteList.add(Color.WHITE);
	        	columns.add(NaNList);
	        	colors.add(whiteList);
	        }
	        	
	        
		}
	}
	
	public ArrayList<Double> getColumn(int col){
			return columns.get(col);
	}

	
	public ArrayList<Color> getColor(int col){
			return colors.get(col);
	}
	
    public double distanceTo(double x1, double y1, double  x2, double y2){
        return Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2));
    }
}