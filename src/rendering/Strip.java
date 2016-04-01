package rendering;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class Strip {
	int x, y, width, length;
	double castedHeight;
	Color color;
	
	public Strip(int x, int y, int width, int length, double castedHeight, Color color){
		this.x = x;
		this.y = y;
		this.width = width;
		this.length = length;
		this.castedHeight = castedHeight;
		this.color = color;
	}
	
	public Double getCast(){
		return castedHeight;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getLength(){
		return length;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public BufferedImage getImage(){
		return null;
	}
}

