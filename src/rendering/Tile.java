package rendering;

import java.awt.Color;

public class Tile {
	private TileType type;
	private Color color;
	private double height, gap;
	
	public Tile(TileType type){
		this.type = type;
		if(this.type == TileType.EMPTY || this.type == TileType.PLAYER){
			this.color = new Color(255, 255, 255);
			this.height = 0;
			this.gap = 0;
		}
	}
	
	public Tile(Color colorAtCoord, double tileHeight, double tileGap) {
		this.color = colorAtCoord;
		this.height = tileHeight;
		this.gap = tileGap;
		if(this.gap == 0)
			this.type = TileType.BLOCK;
		else
			this.type = TileType.CEILING;
	}
	
	public TileType getType(){
		return this.type;
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public double getHeight(){
		return this.height;
	}
	
	public double getBottom(){
		return this.gap;
	}
	
	public double getTop(){
		return this.gap+this.height;
	}
	
	public double getGap(){
		return this.gap;
	}
	
}
