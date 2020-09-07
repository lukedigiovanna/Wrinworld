package display;

import java.awt.*;
import java.awt.event.*;

import main.Panel;
import main.Program;
import misc.Graphics2;
import misc.Keyboard;
import sound.MusicManager;

public class MainDisplay extends Display {

	public MainDisplay(Panel panel) {
		super(panel);
		this.add(new Button("start",Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2-40,40,new Runnable() { 
			public void run() { 
				DisplayController.setDisplay(DisplayController.PRE_GAME_DISPLAY); 
			} 
		}));
		this.add(new Button("update notes",Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2-40+50,40,new Runnable() {
			public void run() {
				DisplayController.setDisplay(DisplayController.UPDATE_NOTES_DISPLAY);
			}
		}));
		this.add(new Button("how to play",Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2-40+100,40,new Runnable() {
			public void run() {
				DisplayController.setDisplay(DisplayController.HOW_TO_PLAY_DISPLAY);
			}
		}));
		this.add(new Button("credits",Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2-40+150,40,new Runnable() {
			public void run() {
				DisplayController.setDisplay(DisplayController.CREDITS_DISPLAY);
			}
		}));
		this.add(new Button("quit",Program.DISPLAY_WIDTH/2,Program.DISPLAY_HEIGHT/2-40+200,40,new Runnable() {
			public void run() {
				System.exit(0);
			}
		}));
	}
	
	public MainDisplay() {
		this(Program.panel);
	}
	
	public void individualInit() {
		MusicManager.changeSong("opening");	
	}
	
	public void unInit() {
		MusicManager.changeSong("");
	}
	
	public void individualUpdate() {
		if (Keyboard.keyDown(KeyEvent.VK_U))
			DisplayController.setDisplay(DisplayController.UPDATE_NOTES_DISPLAY);
		else if (Keyboard.keyDown(KeyEvent.VK_ESCAPE))
			System.exit(0);
	}

	int num = 0;
	@Override
	public void repaint() {
		Graphics g = this.getGraphics();
		
		this.fillScreen("mainscreen.jpg");
		
		g.setFont(new Font("Vivaldi",Font.BOLD | Font.ITALIC,130));
		
		int width = g.getFontMetrics().stringWidth(Program.GAME_NAME);
		g.setColor(Color.BLACK);
		Graphics2.outlineText(g, Program.GAME_NAME, getWidth()/2-width/2, getHeight()/3,Color.MAGENTA);
	
		drawButtons(g);
	}
}
