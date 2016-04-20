package audio;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
	Clip currentlyPlaying = null;
	boolean playingMusic = false;
	
	
	public MusicPlayer(){
		
	}
	
	public void play(SFX clip){
		Clip effect = null;
		if(!(clip instanceof Music)){ //if it's an effect
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
		else if(!playingMusic){
			try {
				System.out.println("tryna play");
				AudioInputStream aIS = AudioSystem.getAudioInputStream(clip.getFile());
				effect = AudioSystem.getClip();
				effect.open(aIS);
			} catch (LineUnavailableException | IOException
				| UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
			playingMusic = true;
			currentlyPlaying = effect;
			currentlyPlaying.loop(Clip.LOOP_CONTINUOUSLY);
		}
		
	}
	
	public void stopMusic(){
		if(playingMusic){
			playingMusic = false;
			currentlyPlaying.stop();
			currentlyPlaying.close();
		}
	}
}
