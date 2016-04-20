package audio;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
	public MusicPlayer(){
		
	}
	
	public void play(SFX clip){
		Clip effect = null;
		try {
			AudioInputStream aIS = AudioSystem.getAudioInputStream(clip.getFile());
			effect = AudioSystem.getClip();
			effect.open(aIS);
		} catch (LineUnavailableException | IOException
				| UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		if(clip instanceof Music)
			effect.loop(Clip.LOOP_CONTINUOUSLY);
		else effect.start();
	}
}
