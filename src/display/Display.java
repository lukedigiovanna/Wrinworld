package display;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import main.Panel;
import main.Program;

/*
 * The Display class is an overarching class that extends the BufferedImage class
 * It has uses the default resolution dimensions given in the main package to create an imgae that 
 * can be displayed. It also controls aspects like listeners of the panel that it is linked to. Often 
 * this panel will be the same for all displays but not always. 
 */

public abstract class Display extends BufferedImage {
	private Panel panel = Program.panel;
	private List<Button> buttons = new ArrayList<Button>();
	
	public Display(Panel panel) {
		super(Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT,BufferedImage.TYPE_INT_ARGB);
		this.panel = panel;
	}
	
	public Display() {
		this(Program.panel);
	}
	
	public void fillScreen(Color c) {
		Graphics g = this.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public void fillScreen(Image img) {
		Graphics g = this.getGraphics();
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}
	
	public void fillScreen(String s) {
		fillScreen(Program.getImage(s));
	}
	
	public void drawButtons(Graphics g) {
		for (Button b : buttons)
			b.draw(g);
	}
	
	public void add(Button b) {
		buttons.add(b);
	}
	
	//redraws the display.. this should be linked to a panel update method that calls it once every program tick
	public abstract void repaint(); 
	public abstract void individualInit();
	public abstract void individualUpdate();
	
	public void update() {
		for (Button b : buttons)
			b.update();
		individualUpdate();
	}
	
	public void init() {
		individualInit();
		DisplayController.removeLoadingScreen();
	}
	
	public void unInit() {
		
	}
	
	private ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public void addListener(EventListener listener) {
		if (listener != null)
			listeners.add(listener);
	}
	
	public void removeListener(EventListener listener) {
		listeners.remove(listener);
	}
	
	public void removeListeners() {
		this.deactivateListeners();
		listeners.clear();
	}
	
	//SUPPORTED LISTENERS: key, mouse, mousemotion, mousewheel
	public void activateListeners() {
		panel = Program.panel;
		for (EventListener l : listeners) {
			if (panel == null) {
				System.err.println("Unable to add listener: panel is not defined");
				break;
			}
			if (l instanceof KeyListener) 
				panel.addKeyListener((KeyListener) l);
			else if (l instanceof MouseListener)
				panel.addMouseListener((MouseListener) l);
			else if (l instanceof MouseMotionListener) 
				panel.addMouseMotionListener((MouseMotionListener) l);
			else if (l instanceof MouseWheelListener)
				panel.addMouseWheelListener((MouseWheelListener) l);
			else
				System.err.println("Unable to add unsupported listener");
		}
	}
	
	public void deactivateListeners() {
		for (EventListener l : listeners) {
			if (panel == null) {
				System.err.println("Unable to remove listener: panel is not defined");
				break;
			}
			if (l instanceof KeyListener)
				panel.removeKeyListener((KeyListener) l);
			else if (l instanceof MouseListener)
				panel.removeMouseListener((MouseListener) l);
			else if (l instanceof MouseMotionListener) 
				panel.removeMouseMotionListener((MouseMotionListener) l);
			else if (l instanceof MouseWheelListener)
				panel.removeMouseWheelListener((MouseWheelListener) l);
			else
				System.err.println("Unable to remove unsupported listener");
		}
	}
}
