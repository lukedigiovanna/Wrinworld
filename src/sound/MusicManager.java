package sound;

import javax.sound.sampled.Clip;

public class MusicManager {
	private static Clip currentSong;
	private static String currentSongName = "";
	private static boolean muted = true;
	
	public static void changeSong(String fp) {
		if (muted) return;
		
		if (currentSong != null)
			currentSong.close();
		if (currentSongName.equals(fp)) {
			paused = false;
			return;
		}
		currentSong = SoundManager.playSound("music/"+fp,SoundManager.LOOP);
		currentSongName = fp;
		paused = false;
	}
	
	private static boolean paused = false;
	public static void pause() {
		if (currentSong != null) {
			paused = true;
			currentSong.stop();
		}
	}
	
	public static void resume() {
		if (paused && currentSong != null) {
			currentSong.start();
			paused = false;
		}
	}
}
