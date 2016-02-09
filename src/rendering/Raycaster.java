package rendering;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import game.Game;

public class Raycaster {
	Game currentGame;
	ArrayList<ArrayList<Double>> columns;
	ArrayList<ArrayList<Double>> heights;
	ArrayList<ArrayList<Color>> colors;
	
	public Raycaster(Game game){
		this.currentGame = game;
		columns = new ArrayList<ArrayList<Double>>();
		heights = new ArrayList<ArrayList<Double>>();
		colors = new ArrayList<ArrayList<Color>>();
	}
	public void cast(){
		int screenWidth = currentGame.getScreen().getWidth();
		columns.clear();
		colors.clear();
		heights.clear();
		double FOV = currentGame.getCamera().getFOV();
		double playerAngle = currentGame.getCamera().getAngle();
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
    		double insideCheckX = 0, insideCheckY = 0;

    		ArrayList<Double> allHits = new ArrayList<Double>();
    		ArrayList<Double> allHeights = new ArrayList<Double>();
    		ArrayList<Color> allColors = new ArrayList<Color>();
    		double checkingDistance;
    		double checkingHeight;
    		Color checkingColor;
            
            while(!hit){
                checkX = currentX;
                if(posDirY){
                	insideCheckY = -1;
                    checkY = (int) currentY;
                }
                else{
                	insideCheckY = 1;
                    checkY = (int) currentY - 1;
                }
                if (checkX > maxX || checkY > maxY || checkX < 0 || checkY < 0) {
                    hit = true;
                }
                else {
                	if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
                		checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
                		checkingColor = currentGame.getWorld().getColorAt(checkX, checkY);
                		checkingHeight = currentGame.getWorld().getHeightAt(checkX, checkY);
                		int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allColors.add(index, checkingColor);
	                    allHeights.add(index, checkingHeight);
                	}
                	if(currentGame.getWorld().getColorAt(checkX, checkY + insideCheckY).getRGB() != Color.WHITE.getRGB()){
	                    checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
	                    checkingColor = currentGame.getWorld().getColorAt(checkX, checkY + insideCheckY);
	                    checkingHeight = currentGame.getWorld().getHeightAt(checkX, checkY + insideCheckY);
	                    int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allColors.add(index, checkingColor);
	                    allHeights.add(index, checkingHeight);
                		
                	}
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
            insideCheckX = 0;
            insideCheckY = 0;
            while(!hit){
                checkY = currentY;
                if(posDirX){
                	insideCheckX = -1;
                    checkX = (int) currentX;
                }
                else{
                	insideCheckX = 1;
                    checkX = (int) currentX - 1;
                }
                if(checkX > maxX || checkY > maxY || checkX < 0 || checkY < 0){
                    hit = true;
                }
                else{
	                if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
	                    checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
	                    checkingColor = currentGame.getWorld().getColorAt(checkX, checkY);
	                    checkingHeight = currentGame.getWorld().getHeightAt(checkX, checkY);
	                    int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allColors.add(index, checkingColor);
	                    allHeights.add(index, checkingHeight);
	                }
	            	if(currentGame.getWorld().getColorAt(checkX + insideCheckX, checkY).getRGB() != Color.WHITE.getRGB()){
	                    checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
	                    checkingColor = currentGame.getWorld().getColorAt(checkX + insideCheckX, checkY);
	                    checkingHeight = currentGame.getWorld().getHeightAt(checkX + insideCheckX, checkY);
	                    int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allColors.add(index, checkingColor);
	                    allHeights.add(index, checkingHeight);
	            		
	            	}
                }
                currentX += deltaX;
                currentY += deltaY;
            }
            
	        if(!allHits.isEmpty()) {
	        	columns.add(allHits);
	        	heights.add(allHeights);
	        	colors.add(allColors);
	        }
	        else {
	        	ArrayList<Double> NaNList = new ArrayList<Double>();
	        	NaNList.add(Double.NaN);
	        	ArrayList<Color> whiteList = new ArrayList<Color>();
	        	whiteList.add(Color.WHITE);
	        	columns.add(NaNList);
	        	heights.add(NaNList);
	        	colors.add(whiteList);
	        }
	        	
	        
		}
	}
	
	public ArrayList<Double> getColumn(int col){
		try{
			return columns.get(col);
		}
		catch(Exception e){
			System.out.println("Tried to paint too early... trying again.");
			return null;
		}
	}
	
	public int insertAllHits(double x, ArrayList<Double> allHits){
	    boolean endOfArray = false;
	    int i = 0;
	    if(allHits.size()==0) return 0;
	    while(!endOfArray){
	    	if(x < allHits.get(i)){
	    		++i;
	    	}
	    	else{
	    		return i;
	    	}
	    	if(allHits.size() == i)
	    		endOfArray = true;
	    }
	    return allHits.size();
	}
	

	
	public ArrayList<Color> getColor(int col){
		try{
			return colors.get(col);
		}
		catch(Exception e){
			System.out.println("Tried to paint too early... trying again.");
			return null;
		}
	}
	public ArrayList<Double> getHeights(int col){
		try{
			return heights.get(col);
		}
		catch(Exception e){
			System.out.println("Tried to paint too early... trying again.");
			return null;
		}
}
	
    public double distanceTo(double x1, double y1, double  x2, double y2){
        return Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2));
    }
}