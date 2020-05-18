package game;

import java.awt.image.BufferedImage;
import java.util.*;

public class GUIManager {
	private int current;
	private List<GUI> guis = new ArrayList<GUI>();
	
	public GUIManager(GUI ... guis) {
		for (GUI g : guis)
			this.guis.add(g);
		current = -1;
	}
	
	public void addGUI(GUI gui) {
		this.guis.add(gui);
	}
	
	public void update() {
		GUI cur = getCurrent();
		if (cur != null) {
			cur.update();
			cur.repaint();
		}
	}
	
	public GUI getCurrent() {
		try {
			return guis.get(current);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getCurrentID() {
		if (getCurrent() == null)
			return "none";
		else
			return getCurrent().getID();
	}
	
	public BufferedImage getCurrentImage() {
		if (getCurrent() == null)
			return new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		else
			return getCurrent().getImage();
	}
	
	public void setGUI(String id) {
		if (id.equals("none"))
			current = -1;
		for (int i = 0; i < guis.size(); i++) {
			GUI gui = guis.get(i);
			if (gui.getID().equals(id)) {
				current = i;
				return;
			}
		}
	}
}
