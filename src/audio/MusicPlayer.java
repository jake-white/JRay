package audio;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
	Clip currentlyPlaying = null;
	boolean playingMusic = false;
	
	
	public MusicPlayer(){//empty constructor
	}
	
	public void play(SFX clip, float decibels){ //play a file with a specific loudness in decibels
		Clip effect = null;
		if(!(clip instanceof Music)){ //if it's an effect, just play it
			try {
				AudioInputStream aIS = AudioSystem.getAudioInputStream(clip.getFile());
				effect = AudioSystem.getClip();
				effect.open(aIS);
			} catch (LineUnavailableException | IOException
					| UnsupportedAudioFileException e) {
				e.printStackTrace();
			}
			effect.start();
		}
		else if(!playingMusic){ //if it's music, we need to check whether other
			try {
				AudioInputStream aIS = AudioSystem.getAudioInputStream(clip.getFile());
				effect = AudioSystem.getClip();
				effect.open(aIS);
			} catch (LineUnavailableException | IOException
				| UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
			playingMusic = true;
			currentlyPlaying = effect;
			FloatControl gainControl = (FloatControl) currentlyPlaying.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(decibels); // Reduce volume by 10 decibels.
			currentlyPlaying.loop(Clip.LOOP_CONTINUOUSLY); //play in a loop
		}
		
	}
	
	public void stopMusic(){ //stop any Music
		if(playingMusic){
			playingMusic = false;
			currentlyPlaying.stop();
			currentlyPlaying.close();
		}
	}
}
