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
	
	private Thread mainThread = new Thread(new Runnable() { public void run() { 
		while (true) { 
			long first = System.currentTimeMillis();
			mainUpdate(); 
			long wait = 1000/Program.TICKS_PER_SECOND;
			while (System.currentTimeMillis() - first < wait) continue; // sleeps
			tps = 1000.0/(System.currentTimeMillis() - first);
		} 
	} });
	
	private Thread paintThread = new Thread(new Runnable() { public void run() {
		while (true) {
			long first = System.currentTimeMillis();
			repaint();
			long wait = 1000/Program.TARGET_FPS;
			while (System.currentTimeMillis() - first < wait) continue; // sleeps
			fps = 1000.0/(System.currentTimeMillis() - first);
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
