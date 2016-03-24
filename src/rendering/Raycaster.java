package rendering;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

import game.Game;

public class Raycaster {
	Game currentGame;
	ArrayList<ArrayList<Double>> columns;
	ArrayList<ArrayList<Point>> mapPoints;
	
	public Raycaster(Game game){
		this.currentGame = game;
		columns = new ArrayList<ArrayList<Double>>();
		mapPoints = new ArrayList<ArrayList<Point>>();
	}
	public void cast(){
		int screenWidth = currentGame.getScreen().getWidth();
		columns.clear();
		mapPoints.clear();
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
    		ArrayList<Point> allPoints = new ArrayList<Point>();
    		double checkingDistance;
            
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
                	if(currentGame.getWorld().getColorAt(checkX, checkY + insideCheckY).getRGB() != Color.WHITE.getRGB()){
	                    checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
	                    int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allPoints.add(index, new Point((int) checkX, (int) (checkY + insideCheckY)));
                		
                	}
                	if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
                		checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
                		int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allPoints.add(index, new Point((int) checkX, (int) checkY));
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
	            	if(currentGame.getWorld().getColorAt(checkX + insideCheckX, checkY).getRGB() != Color.WHITE.getRGB()){
	                    checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
	                    int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allPoints.add(index, new Point((int) (checkX + insideCheckX), (int) checkY));
	            		
	            	}
	                if(currentGame.getWorld().getColorAt(checkX, checkY).getRGB() != Color.WHITE.getRGB()){
	                    checkingDistance = distanceTo(playerX, playerY, currentX, currentY)*fisheye;
	                    int index = insertAllHits(checkingDistance, allHits);
	                    allHits.add(index, checkingDistance);
	                    allPoints.add(index, new Point((int) checkX, (int) checkY));
	                }
                }
                currentX += deltaX;
                currentY += deltaY;
            }
            
	        if(!allHits.isEmpty()) {
	        	columns.add(allHits);
	        	mapPoints.add(allPoints);
	        	//System.out.println(i + " = " + allPoints);
	        	//System.out.println(allPoints);
	        }
	        else { //adding dummy values to the lists if empty
	        	ArrayList<Double> NaNList = new ArrayList<Double>();
	        	NaNList.add(Double.NaN);
	        	ArrayList<Color> whiteList = new ArrayList<Color>();
	        	whiteList.add(Color.WHITE);
	        	ArrayList<Point> pointLess = new ArrayList<Point>();
	        	pointLess.add(new Point(0,0));
	        	columns.add(NaNList);
	        	mapPoints.add(pointLess);
	        }
	        	
	        
		}
	}
	
	public ArrayList<Double> getColumn(int col){
		try{
			return columns.get(col);
		}
		catch(Exception e){
			System.out.println("Tried to paint too early... no getColumn available.");
			return null;
		}
	}
	
	public ArrayList<Point> getPointOfColumn(int col){ //returns what map point a column was casted from

		try{
			return mapPoints.get(col);
		}
		catch(Exception e){
			System.out.println("Tried to paint too early... no getPointOfColumn available.");
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
	
    public double distanceTo(double x1, double y1, double  x2, double y2){
        return Math.sqrt(Math.pow(y1 - y2, 2) + Math.pow(x1 - x2, 2));
    }
}