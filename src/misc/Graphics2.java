package misc;

import java.awt.*;
import java.awt.image.BufferedImage;

import display.ImageProcessor;
import main.Program;

//all methods must have a graphics reference passed to them to function.
//this is NOT an extension of a graphics object and cannot be instantiated
public class Graphics2 {
	//font should be set before hand
	public static void outlineText(Graphics g, String text, int x, int y, Color outlineColor, int width) {
		Color textColor = g.getColor();
		//draws
		//draw top left, bottom left, top right, bottom right
		g.setColor(outlineColor);
		g.drawString(text, x-width, y-width);
		g.drawString(text, x+width, y-width);
		g.drawString(text, x-width, y+width);
		g.drawString(text, x+width, y+width);
		g.setColor(textColor);
		g.drawString(text, x, y);
	}
	
	//same method as above but passes a default width of 1
	public static void outlineText(Graphics g, String text, int x, int y, Color outlineColor) {
		outlineText(g,text,x,y,outlineColor,1);
	}
	
	public static void drawRect(Graphics g, int x, int y, int width, int height, int borderWidth) {
		g.fillRect(x,y,width,borderWidth);
		g.fillRect(x, y, borderWidth, height);
		g.fillRect(x+width-borderWidth, y, borderWidth, height);
		g.fillRect(x, y+height-borderWidth, width, borderWidth);
	}
	
	public static Color getColorInLoop(Color[] loop, double percent) {
		double interval = 1.0/(loop.length-1);
		if (percent != 1)
			percent%=1;
		if (percent < 0)
			percent = 1+percent;
		int cInd = (int)(percent/interval);
		if (cInd > loop.length-2)
			cInd = loop.length-2;
		double partPercent = (percent/interval)-(int)(percent/interval);
		Color lowCol = loop[cInd];
		Color highCol = loop[cInd+1];
		int red = (int)((highCol.getRed()-lowCol.getRed())*partPercent)+lowCol.getRed();
		int green = (int)((highCol.getGreen()-lowCol.getGreen())*partPercent)+lowCol.getGreen();
		int blue = (int)((highCol.getBlue()-lowCol.getBlue())*partPercent)+lowCol.getBlue();
		red = MathUtils.clip(red, 0, 255);
		green = MathUtils.clip(green, 0, 255);
		blue = MathUtils.clip(blue, 0, 255);
		return (new Color(red,green,blue));
	}
	
	public static int getFontSize(double percent) { //percent of screen in vertical direction
		int height = (int) (percent*Program.DISPLAY_HEIGHT);
		return height;
	}
	
	public static int centerX(Graphics g, String s) {
		return Program.DISPLAY_WIDTH/2-g.getFontMetrics().stringWidth(s)/2;
	}
	
	public static int centerX(Graphics g, String s, int width) {
		return width/2-g.getFontMetrics().stringWidth(s)/2;
	}
	
	private static Image[] numbers = {Program.getImage("numbers/0.png"),Program.getImage("numbers/1.png"),Program.getImage("numbers/2.png"),Program.getImage("numbers/3.png"),Program.getImage("numbers/4.png"),Program.getImage("numbers/5.png"),Program.getImage("numbers/6.png"),Program.getImage("numbers/7.png"),Program.getImage("numbers/8.png"),Program.getImage("numbers/9.png")};
	public static BufferedImage getPixelatedNumber(int num, Color color) {
		BufferedImage newImage = new BufferedImage(("number_"+num).length()*4,5,BufferedImage.TYPE_INT_ARGB);
		Graphics g = newImage.getGraphics();
		int count = 0;
		while (num > 0) {
			int place = num%10;
			place--;
			if (place < 0)
				place = 9;
			g.drawImage(numbers[place], newImage.getWidth()-4*(count+1), 0, 4, 5, null);
			num/=10;
			count++;
		}
		newImage = ImageProcessor.scaleToColor(newImage, color);
		return newImage;
	}
	
	public static boolean isCapital(char a) {
		String capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < capitals.length(); i++) 
			if (a == capitals.charAt(i))
				return true;
		return false;
	}
}
