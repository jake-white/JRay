import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
	BufferedImage worldImg;
	Color[][] map;
	Game currentGame;
	int width, height;
	
	public World(Game game, String fileName){
		this.currentGame = game;
		try {
			File worldFile = new File(fileName);
			worldImg = ImageIO.read(worldFile);
			this.parseMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void parseMap() { //reads through the image for each pixel
		// TODO Auto-generated method stub
		this.width = worldImg.getWidth();
		this.height = worldImg.getHeight();
		map = new Color[width][height];
		for(int x = 0; x < width; ++x){
			for(int y = 0; y < height; ++y){
				map[x][y] = new Color(worldImg.getRGB(x, y));
			}
		}
	}

}
