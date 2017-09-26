package goldminer;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	
	private AudioInputStream audioInputStream;
	private Clip clip;

	public Music() {
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream("/resources/sounds/SouthTexasCowboyBlues.wav")));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void play() {
		clip.setFramePosition(0);
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}
}
