package audio;

import java.io.File;
import java.io.InputStream;

public class SFX {
	private File sfx;
	
	public SFX(String fileName){
		sfx = new File(fileName);
		
	}

	public File getFile() {
		return sfx;
	}
}
