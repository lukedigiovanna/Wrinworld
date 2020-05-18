package display;

import java.awt.*;

import main.Program;
import misc.Graphics2;

public class Overlay extends Display {
	public Overlay() {
		super();
	}
	
	public void individualUpdate() {
		repaint();
	}
	
	int clear = (new Color(0,0,0,0)).getRGB();
	@Override
	public void repaint() {
		Graphics g = this.getGraphics();
//		for (int x = getWidth()/2; x < getWidth(); x++) for (int y = 0; y < getHeight(); y++)
//			setRGB(x,y,clear);
		g.setColor(Color.BLACK);
		int height = Graphics2.getFontSize(0.035);
		g.setFont(new Font("Arial",Font.BOLD,height-1));
		String[] notes = {
			Program.GAME_NAME+" "+Program.VERSION,
			Program.RELEASE_INFO,
			Program.BUSINESS
		};
		for (int i = 0; i < notes.length; i++) {
			int width = g.getFontMetrics().stringWidth(notes[i]);
			g.drawString(notes[i], getWidth()-width-5, /*getHeight()-notes.length*height+*/i*height+height+5);
		}
	}
	
	public void individualInit() {} 
}
