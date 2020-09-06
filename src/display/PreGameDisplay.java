package display;

import java.awt.*;

import game.GameController;
import misc.Graphics2;
import misc.Keyboard;
import misc.Mouse;

public class PreGameDisplay extends Display {
	@Override
	public void repaint() {
		this.fillScreen(new Color(210,180,140));
		Graphics g = this.getGraphics();
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial",Font.BOLD,36));
		Graphics2.outlineText(g, "GAME SELECT", Graphics2.centerX(g, "GAME SELECT"), 100, Color.WHITE);
		g.setColor(Color.GRAY);
		g.setFont(new Font("Arial",Font.ITALIC,18));
		Graphics2.outlineText(g, "Click a load file to play it", Graphics2.centerX(g, "Click a load file to play it"), 130, Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawString("Loading files not implemented yet... Just click to continue", 20, 180);
	}

	@Override
	public void individualInit() {
		
	}
	
	@Override
	public void unInit() {
		leave = false;
		count = 0;
	}
	
	private String loadFileName;
	
	private void leave() {
		GameController.setLoadFileName(loadFileName);
		leave = true;
	}
	
	private boolean leave = false;
	private int count = 0;
	@Override
	public void individualUpdate() {
		if (Keyboard.anyKeyDown() || Mouse.mouseDown())
			leave();
		
		if (leave) {
			DisplayController.showLoadingScreen();
			if (count > 1)
				DisplayController.setDisplay(DisplayController.GAME_DISPLAY);
			count++;
		}
	}
}
