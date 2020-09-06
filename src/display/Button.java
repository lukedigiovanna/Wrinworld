package display;

import java.awt.*;
import java.awt.image.BufferedImage;

import main.Program;
import misc.Mouse;

public class Button {
	public static final BufferedImage BUTTON_SIDE = Program.getImage("buttons/button_side.png"), BUTTON_MIDDLE = Program.getImage("buttons/button_middle.png");
	
	private int x, y, width, height;
	private BufferedImage image;
	private BufferedImage toDraw;
	private Runnable clicked;
	
	public Button(String text, int x, int y, int width, int height, Runnable clicked) {
		//image = Program.getImage("buttons/"+imageName+".png");
		int pixelWidth = 6+text.length()*6; //letter is 5 pixels plus 1 pixel for character spacing
		image = new BufferedImage(pixelWidth,17,BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(BUTTON_SIDE, 0, 0, 5, 17, null);
		g.drawImage(ImageProcessor.flipY(BUTTON_SIDE), image.getWidth()-5, 0, 5, 17, null);
		for (int px = 5; px < image.getWidth()-5; px++)
			g.drawImage(BUTTON_MIDDLE, px, 0, 1, 17, null);
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < text.length(); i++) {
			int index = alphabet.indexOf(text.charAt(i));
			if (index < 0)
				continue;
			BufferedImage letter = ImageProcessor.scaleToColor(Program.getImage("letters/"+index+".png"),new Color(0,200,200));
			g.drawImage(letter, 3+i*6, 5, 5, 7, null);
		}
		
		toDraw = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.clicked = clicked;
	}
	
	public Button(String text, int centerX, int y, int height, Runnable clicked) {
		int pixelWidth = 6+text.length()*6; //letter is 5 pixels plus 1 pixel for character spacing
		image = new BufferedImage(pixelWidth,17,BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.drawImage(BUTTON_SIDE, 0, 0, 5, 17, null);
		g.drawImage(ImageProcessor.flipY(BUTTON_SIDE), image.getWidth()-5, 0, 5, 17, null);
		for (int px = 5; px < image.getWidth()-5; px++)
			g.drawImage(BUTTON_MIDDLE, px, 0, 1, 17, null);
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < text.length(); i++) {
			int index = alphabet.indexOf(text.charAt(i));
			if (index < 0)
				continue;
			BufferedImage letter = ImageProcessor.scaleToColor(Program.getImage("letters/letter_"+index+".png"),new Color(0,200,200));
			g.drawImage(letter, 3+i*6, 5, 5, 7, null);
		}
		
		toDraw = image;
		this.width = (int)((double)image.getWidth()/image.getHeight()*height);
		//iw/ih = w/h
		this.x = centerX-this.width/2;
		this.y = y;
		this.height = height;
		this.clicked = clicked;
	}
	
	public Button(BufferedImage img, int x, int y, int width, int height, Runnable clicked) {
		image = img;
		toDraw = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.clicked = clicked;
	}
	
	public void update() {
		int ex = Mouse.getXOnDisplay();
		int ey = Mouse.getYOnDisplay();
		toDraw = image;
		if (ex > this.x && ex < this.x+this.width && ey > this.y && ey < this.y+this.height) {
			if (Mouse.mouseDown())
				click();
			toDraw = ImageProcessor.scaleToColor(image, Color.LIGHT_GRAY);
		}
	}
	
	private boolean mouseOver = false;
	public void setMouseOver(boolean t) {
		this.mouseOver = t;
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}
	
	public void draw(Graphics g) {
		g.drawImage(toDraw, x, y, width, height, null);
	}
	
	public void click() {
		clicked.run();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
