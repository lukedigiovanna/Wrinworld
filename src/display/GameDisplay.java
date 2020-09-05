package display;

import java.awt.*;
import java.awt.event.*;
import java.util.EventListener;

import game.GameController;
import main.Panel;
import main.Program;
import sound.MusicManager;

public class GameDisplay extends Display {
	public GameDisplay(Panel panel) {
		super(panel);
	}
	
	public GameDisplay() {
		super();
	}
	
	private boolean addedListeners = false;
	
	public void setAddedListeners(boolean value) {
		addedListeners = value;
	}
	
	public void individualInit() {
		GameController.init();
		
		MusicManager.changeSong("prelude");
		
		if (!addedListeners) {
			for (EventListener l : GameController.game().getListeners())
				this.addListener(l);
			addedListeners = true;
		}
	}
	
	public void unInit() {
	}
	
	public void individualUpdate() {
		GameController.updateCurrentGame();
	}

	public void repaint() {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		this.fillScreen(GameController.game().getView());
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial",Font.ITALIC,14));
		g.drawString("FPS: "+Program.fps(), getWidth()-g.getFontMetrics().stringWidth("FPS: "+Program.fps())-5, getHeight()-10);
		g.drawString("TPS: "+Program.tps(), getWidth()-g.getFontMetrics().stringWidth("TPS: "+Program.tps())-5, getHeight()-26);
	}
}
