package main;

import java.awt.*;
import javax.swing.*;

import display.DisplayController;
import misc.*;

public class Panel extends JPanel {
	private static final long serialVersionUID = 1L;

	public Panel() {
		this.setFocusable(true);
		//initiate threads belonging to the panel
		mainThread.start();
		paintThread.start();
	}
	
	private int refreshSpeed = 1000/Program.TICKS_PER_SECOND; //DEFAULT
	private Thread mainThread = new Thread(new Runnable() { public void run() { 
		while (true) { 
			long first = System.nanoTime();
			try { 
				Thread.sleep(refreshSpeed); 
			} catch (Exception e) {} 
			mainUpdate(); 
			tps = 1000000000.0/(System.nanoTime()-first);
		} 
	} });
	
	private Thread paintThread = new Thread(new Runnable() { public void run() {
		while (true) {
			long first = System.nanoTime();
			try {
				Thread.sleep(1000/20);
			} catch (Exception e) {}
			fps = 1000000000.0/(System.nanoTime()-first);
			repaint();
		}
	} });
	
	private double fps = 0, tps = 0;
	
	public double fps() {
		return MathUtils.round(fps, 1.0);
	}
	
	public double tps() {
		return MathUtils.round(tps, 1.0);
	}
	
	public void repaint() {
		DisplayController.repaintCurrentDisplay();
		super.repaint();
	}
	
	public void mainUpdate() {
		DisplayController.updateCurrentDisplay();
	}
	
	public void paintComponent(Graphics g) {
		if (DisplayController.getCurrentDisplay() != null)
			g.drawImage(DisplayController.getImage(), 0, 0, getWidth(), getHeight(), null);
	}
}
