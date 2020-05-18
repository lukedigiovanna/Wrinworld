package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Program;
import misc.Graphics2;

public abstract class GUI {
	private int x, y, width, height;
	
	private BufferedImage image;
	
	public GUI(int x, int y, int width, int height) { 
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		image = new BufferedImage(this.width,this.height,BufferedImage.TYPE_INT_ARGB);
	}
	
	public GUI() {
		this(0,0,Program.DISPLAY_WIDTH,Program.DISPLAY_HEIGHT);
	}
	
	public abstract void update();
	public abstract void repaint();
	
	public String getID() {
		String s = "";
		String className = this.getClass().getSimpleName();
		int iOGUI = className.indexOf("GUI");
		if (iOGUI > -1)
			className = className.substring(0,iOGUI);
		for (int i = 0; i < className.length(); i++) {
			if (Graphics2.isCapital(className.charAt(i))) 
				//if index is > 0 then add underscore before
				if (i > 0) 
					s+="_";
			s+=(""+className.charAt(i)).toLowerCase();
		}
		return s;
	}

	public Graphics getGraphics() {
		return image.getGraphics();
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public void clear() {
		image = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_ARGB);
	}

	public BufferedImage getImage() {
		return image;
	}
}
