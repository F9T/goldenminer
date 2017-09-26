package goldminer;

import java.io.BufferedInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundEffect {

	String filename;
	AudioInputStream audioInputStream;
	Clip clip;

	public void playGold() {
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream("/resources/sounds/2706.wav")));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.setFramePosition(0);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void playBomb() {
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(this.getClass().getResourceAsStream("/resources/sounds/1460.wav")));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.setFramePosition(0);
			clip.start();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
