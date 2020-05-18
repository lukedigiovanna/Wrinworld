package display;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.GameController;

import java.awt.*;

import main.Panel;
import sound.MusicManager;

public class PauseDisplay extends Display {
	public PauseDisplay(Panel panel) {
		super(panel);
	}
	
	public PauseDisplay() {
		super();
	}
	
	public void individualInit() {
		MusicManager.pause();
		this.addListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					DisplayController.setDisplay(DisplayController.GAME_DISPLAY);
				if (e.getKeyCode() == KeyEvent.VK_Q) {
					GameController.saveCurrentGame("game");
					DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
				}
			}
		});
	}
	
	public void unInit() {
		MusicManager.resume();
	}
	
	public void individualUpdate() {
		
	}
	
	@Override
	public void repaint() {
		this.fillScreen(ImageProcessor.grayScale(DisplayController.getDisplay(DisplayController.GAME_DISPLAY)));
		Graphics g = this.getGraphics();
		g.setColor(Color.RED);
		g.setFont(new Font("Arial",Font.BOLD,48));
		g.drawString("PAUSE", getWidth()/2-g.getFontMetrics().stringWidth("PAUSE")/2, getHeight()/2-getHeight()/6);
		g.setFont(new Font("Arial",Font.ITALIC,30));
		g.drawString("Press 'q' to quit", getWidth()/2-g.getFontMetrics().stringWidth("Press 'q' to quit")/2, getHeight()/2+getHeight()/6);
	}	
}
