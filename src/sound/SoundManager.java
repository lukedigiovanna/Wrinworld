package sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class SoundManager {
	public static final boolean LOOP = false;
	
	public static Clip playSound(String filePath) {
		filePath="library/assets/sounds/"+filePath+".wav";
		File audioFile = new File(filePath);
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.loop(0);
			return audioClip;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Clip playSound(String filePath, boolean loop) {
		filePath="library/assets/sounds/"+filePath+".wav";
		File audioFile = new File(filePath);
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			audioClip.open(audioStream);
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
			return audioClip;
		} catch (IOException ex) {
			return null;
		} catch (UnsupportedAudioFileException | LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//currently broken.. idk why
	public static void playSound(String filePath, float volume) {
		filePath="library/assets/sounds/"+filePath+".wav";
		File audioFile = new File(filePath);
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip audioClip = (Clip) AudioSystem.getLine(info);
			FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.VOLUME);
			float range = gainControl.getMaximum()-gainControl.getMinimum();
			float gain = volume*range+gainControl.getMinimum();
			gainControl.setValue(gain);
			audioClip.open(audioStream);
			audioClip.loop(0);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
