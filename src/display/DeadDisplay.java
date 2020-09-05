package display;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import game.GameController;
import misc.Graphics2;
import sound.MusicManager;

public class DeadDisplay extends Display {
	public void individualInit() {
		MusicManager.changeSong("gameover");
		
		this.addListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
					GameController.endGame();
					break;
				}
			}
		});
		
		this.addListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				GameController.game().resetPlayer();
				DisplayController.setDisplay(DisplayController.GAME_DISPLAY);
			}
		});
	}
	
	public void individualUpdate() {
		
	}
	
	@Override
	public void repaint() {
		Graphics g = this.getGraphics();
		this.fillScreen(DisplayController.getDisplay(DisplayController.GAME_DISPLAY));
		this.fillScreen(new Color(125,125,125,125));
		g.setFont(new Font("Arial",Font.BOLD,42));
		g.setColor(Color.RED);
		Graphics2.outlineText(g, "YOU DIED!", Graphics2.centerX(g, "YOU DIED!"), 200, Color.BLACK, 2);
		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial",Font.ITALIC,30));
		Graphics2.outlineText(g, "* Click to respawn *", Graphics2.centerX(g, "★ Click to respawn ★"), 300, Color.BLACK);
		Graphics2.outlineText(g, "* Press escape to return to home *", Graphics2.centerX(g, "★ Press escape to return to home ★"), 350, Color.BLACK);
	}
}
