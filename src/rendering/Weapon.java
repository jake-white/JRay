package rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import audio.MusicPlayer;
import audio.SFX;

public class Weapon {
	private BufferedImage[] img = new BufferedImage[4];
	private String fileName;
	private int state = 0;
	private final int animLength = 5;
	private boolean isShooting = false;
	
	public Weapon(String filename){
		this.fileName = filename;
		try {
			for(int i = 0; i < img.length; ++i){
				img[i] = ImageIO.read(new File("res/"+fileName+"_"+i+".png"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage(){
		if(isShooting){
			++state;
		}
		if(state >= img.length*animLength){
			System.out.println("done");
			state = 0;
			isShooting = false;
		}
		return img[(int) state/animLength];
	}

	public void shoot() {
		state = 0;
		isShooting = true;
	}

}
