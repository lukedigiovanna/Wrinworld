package display;

import main.Program;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import misc.Graphics2;

public class HowToPlayDisplay extends Display {
	
	public void individualInit() {
		this.addListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
			}
		});
	}
	
	public void individualUpdate() {
		
	}
	
	@Override
	public void repaint() {
		fillScreen(Color.BLACK);
		Graphics g = getGraphics();
		g.setColor(Color.MAGENTA);
		g.setFont(new Font("Times New Roman",Font.BOLD,35));
		Graphics2.outlineText(g, "HOW TO PLAY", Graphics2.centerX(g, "HOW TO PLAY"), 60, Color.BLUE);
		g.setColor(Color.GRAY);
		g.setFont(new Font("Times New Roman",Font.ITALIC,19));
		Graphics2.outlineText(g, "Version "+Program.VERSION, Graphics2.centerX(g,"Version "+Program.VERSION), 90, Color.LIGHT_GRAY);
		String[] notes = {
			"Move: WASD",
			"Sprint: shift",
			"Using item: point and click",
			"Open inventory: e",
			"Talk to trader: space",
			"Pause: escape",
			"Traverse hotbar: number keys or by scrolling",
			"",
			"Your goal is to destroy all the portals in the world"
		};
		int y = 120, height = 15;
		g.setFont(new Font("Arial",Font.PLAIN,13));
		for (int i = 0; i < notes.length; i++) {
			String toDraw = notes[i];
			int xAdd = 0;
			while (toDraw.length() > 0 && toDraw.charAt(0) == ' ') {
				toDraw = toDraw.substring(1);
				xAdd+=g.getFontMetrics().stringWidth(" ");
			}
			g.drawString("• "+toDraw, 35+xAdd, y+i*height);
		}
	}
}

