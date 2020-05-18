package display;

import main.Program;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import misc.Graphics2;

public class UpdateNotesDisplay extends Display {
	
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
		g.setColor(Color.GREEN);
		g.setFont(new Font("Times New Roman",Font.BOLD,35));
		Graphics2.outlineText(g, "UPDATE NOTES", Graphics2.centerX(g, "UPDATE NOTES"), 60, Color.BLUE);
		g.setColor(Color.GRAY);
		g.setFont(new Font("Times New Roman",Font.ITALIC,19));
		Graphics2.outlineText(g, "Version "+Program.VERSION+" (Since May 2, 2019)", Graphics2.centerX(g,"Version "+Program.VERSION+" (Since May 2, 2019)"), 90, Color.LIGHT_GRAY);
		String[] notes = {
			"New enemies: minion, giant, slime, fire devil, mage, wizard",
			"New items: javelin, shurikens, fire wand",
			"New world",
			"Fixed collision with walls",
			"Now rotates entities based on velocity angle",
			"   Contains modified collision for rotated entities",
			"Better button mechanics",
			"More tiles: planks",
			"Added traders which you can buy items from",
			"Fixed the way you use items with a cooldown period and a load/charge",
			"Implemented a far better inventory GUI",
			"Added an objective to the game:",
			"   Destroy all portals, which spawn enemies",
			"Many game balances, bug fixes, and tweaks"
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

