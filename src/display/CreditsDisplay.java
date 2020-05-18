package display;

import main.Program;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import misc.Graphics2;

public class CreditsDisplay extends Display {
	
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
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Times New Roman",Font.BOLD,35));
		Graphics2.outlineText(g, "CREDITS", Graphics2.centerX(g, "CREDITS"), 60, Color.BLUE);
		g.setColor(Color.GRAY);
		g.setFont(new Font("Times New Roman",Font.ITALIC,19));
		Graphics2.outlineText(g, "Version "+Program.VERSION, Graphics2.centerX(g,"Version "+Program.VERSION), 90, Color.LIGHT_GRAY);
		String[] notes = {
			"All code written in pure Java, with no external libraries used, by Luke DiGiovanna",
			"  *Though stackoverflow was visited for some questions",
			"All pixel art/animations created by Luke DiGiovanna using an online program: piskelapp.com",
			"  *Except for the main screen art",
			"All sound effects created by Luke DiGiovanna using Audacity",
			"Music downloaded from opengameart.org by t4ngr4m",
			"",
			"Project began in March with 106 classes made and 9,254 lines of code written"
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

