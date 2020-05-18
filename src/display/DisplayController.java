package display;

import java.awt.*;
import java.awt.image.BufferedImage;

import console.Console;
import main.Program;
import misc.Graphics2;
import misc.Keyboard;
import misc.Mouse;
import sound.MusicManager;

public class DisplayController {
	private static Overlay overlay = new Overlay();
	private static Display[] displays = {new MainDisplay(), new GameDisplay(), new PauseDisplay(), new UpdateNotesDisplay(), new DeadDisplay(), new PreGameDisplay(), new HowToPlayDisplay(), new CreditsDisplay()};
	public static final int 
			MAIN_DISPLAY = 0, 
			GAME_DISPLAY = 1, 
			PAUSE_DISPLAY = 2, 
			UPDATE_NOTES_DISPLAY = 3, 
			DEAD_DISPLAY = 4,
			PRE_GAME_DISPLAY = 5,
			HOW_TO_PLAY_DISPLAY = 6,
			CREDITS_DISPLAY = 7;
	//these numbers correspondto the index in the array
	private static int curDisp = -1;
	
	private static boolean showLoadingScreen = false;
	public static void showLoadingScreen() {
		showLoadingScreen = true;
	}
	
	public static void removeLoadingScreen() {
		showLoadingScreen = false;
	}
	
	public static boolean isShowingLoadingScreen() {
		return showLoadingScreen;
	}
	
	public static void setDisplay(int i) {
		if (curDisp == i || i < 0 || i > displays.length-1)
			return;
		if (curDisp > -1) {
			displays[curDisp].deactivateListeners();
			displays[curDisp].unInit();
		}
		Keyboard.reset();
		Mouse.reset();
		curDisp = i;
		showLoadingScreen();
		displays[curDisp].init();
		displays[curDisp].activateListeners();
	}
	
	public static Display getCurrentDisplay() {
		if (curDisp < 0)
			return null;
		return displays[curDisp];
	}
	
	public static Display getDisplay(int i) {
		if (i < 0 || i > displays.length-1)
			return null;
		return displays[i];
	}
	
	private static BufferedImage lastImage = new BufferedImage(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
	
	private static String loading = "LOADING...";
	private static int callCount = 0;
	public static BufferedImage getImage() {
		BufferedImage img = new BufferedImage(main.Program.DISPLAY_WIDTH,main.Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		//draws the current display image
		if (lastImage != null)
			g.drawImage(lastImage,0,0,img.getWidth(),img.getHeight(),null);
		
//		g.drawImage(getCurrentDisplay(), 0, 0, img.getWidth(), img.getHeight(), null);
		
		//draw the overlay
		g.drawImage(overlay, 0, 0, img.getWidth(), img.getHeight(), null);
		
		//loading display
		if (showLoadingScreen) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, img.getWidth(), img.getHeight());
			g.setFont(new Font("Arial",Font.BOLD,48));
			g.setColor(Color.WHITE);
			g.drawString(loading, Graphics2.centerX(g, loading), 200);
		}
		int numOfDots = callCount%27/(27/3);
		String dots = ".";
		for (int i = 0; i < numOfDots; i++)
			dots+=".";
		loading = "LOADING"+dots;
		
		callCount++;
		return img;
	}
	
	public static void updateCurrentDisplay() {
		Display cur = getCurrentDisplay();
		if (cur != null)
			cur.update();
	}
	
	public static void repaintCurrentDisplay() {
		overlay.update();
		Display cur = getCurrentDisplay();
		if (cur != null) 
			cur.repaint();
		
		if (curDisp >= 0 && curDisp < displays.length)
			lastImage.getGraphics().drawImage(displays[curDisp], 0, 0, lastImage.getWidth(), lastImage.getHeight(), null);
	}
}