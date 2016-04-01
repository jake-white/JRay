package rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite extends Strip{
	private BufferedImage img;
	private String fileName;
	
	public Sprite(int x, int y, String fileName, double castedHeight){
		super(x, y, 0, 0, castedHeight, null);
		this.fileName = fileName;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage(){
		return this.img;
	}

}
