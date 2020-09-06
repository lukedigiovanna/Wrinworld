package display;

import java.awt.*;
import java.awt.image.*;

public class ImageProcessor {
	/*
	 * glossary of functions
	 * -grayScale
	 * -inverse
	 * -leaveRed
	 * -leaveBlue
	 * -leaveGreen
	 * -brighten
	 * -darken
	 * -scaleToColor - pass a color that the image will scale to
	 */
	/*
	public static BufferedImage template(BufferedImage img) {
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				Color nc = getColor(red,green,blue);
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	*/
	
	public static BufferedImage grayScale(BufferedImage img) {
		return scaleToColor(img,Color.GRAY);
	}
	
	public static BufferedImage scaleToColor(BufferedImage img, Color scaleColor) {
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				int redToScale = scaleColor.getRed();
				int blueToScale = scaleColor.getBlue();
				int greenToScale = scaleColor.getGreen();
				double redScale = redToScale/255.0;
				double blueScale = blueToScale/255.0;
				double greenScale = greenToScale/255.0;
				int avg = (red+blue+green)/3;
				Color nc = getColor((int)(redScale*avg),(int)(greenScale*avg),(int)(blueScale*avg),c.getAlpha());
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage leaveRed(BufferedImage img) {
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				Color nc = getColor(red,0,0,c.getAlpha());
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage leaveBlue(BufferedImage img) {
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int blue = c.getBlue();
				Color nc = getColor(0,0,blue,c.getAlpha());
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage leaveGreen(BufferedImage img) {
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int green = c.getGreen();
				Color nc = getColor(0,green,0,c.getAlpha());
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage brighten(BufferedImage img, double intensity) {
		while (intensity > 1)
			intensity--;
		intensity++;
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				red = cap255((int)(red*intensity));
				blue = cap255((int)(blue*intensity));
				green = cap255((int)(green*intensity));
				Color nc = getColor(red,green,blue,c.getAlpha());
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage darken(BufferedImage img, double intensity) {
		while (intensity > 1)
			intensity--;
		intensity = 1-intensity; //this is necessary because the intensity will be given as a range (0-1) where 20% is less dark than 80% so it must be inversed
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				int alpha = c.getAlpha();
				red = cap255((int)(red*intensity));
				blue = cap255((int)(blue*intensity));
				green = cap255((int)(green*intensity));
				Color nc = getColor(red,green,blue,alpha);
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage inverse(BufferedImage img) {
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				red = 255-red;
				blue = 255-blue;
				green = 255-green;
				Color nc = getColor(red,green,blue,c.getAlpha());
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage transparent(BufferedImage img, double transparency) {
		//note: transparency is a value 0-1, 0 being invisible, 1 being opaque
		if (transparency > 1)
			transparency = 1; //provide a limit
		int[][] rgbs = getRGB(img);
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				Color c = getColor(rgbs[x][y]);
				int red = c.getRed();
				int blue = c.getBlue();
				int green = c.getGreen();
				int alpha = c.getAlpha();
				if (alpha > 0)
					alpha = (int)(255*transparency); //the alpha is uniform and maintains invisible portions of the image
				Color nc = getColor(red,green,blue,alpha);
				rgbs[x][y] = nc.getRGB();
			}
		}
		return getImage(img,rgbs);
	}
	
	public static BufferedImage fadeEdges(BufferedImage img) {
		//note: transparency is a value 0-1, 0 being invisible, 1 being opaque
		int[][] rgbs = getRGB(img);
		int[][] newRGB = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				double intensity = 0;
				double max = rgbs.length/2+rgbs[x].length/2;
				double combo = 0;
				if (x < rgbs.length/2 && y < rgbs[x].length/2) 
					combo = x+y;
				else if (x >= rgbs.length/2 && y < rgbs[x].length/2) 
					combo = (rgbs.length-x)+y;
				else if (x < rgbs.length/2 && y >= rgbs[x].length/2)
					combo = x+(rgbs[x].length-y);
				else
					combo = (rgbs.length-x)+(rgbs[x].length-y);
				
				intensity = (1-(combo/max))*0.7;
				newRGB[x][y] = darkenRGB(new Color(rgbs[x][y]),intensity);
			}
		}
		return getImage(img,newRGB);
	}
	
	public static BufferedImage fadeEdges(BufferedImage img, Color fade) {
		//note: transparency is a value 0-1, 0 being invisible, 1 being opaque
		int[][] rgbs = getRGB(img);
		int[][] newRGB = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				double intensity = 0;
				double max = rgbs.length/2+rgbs[x].length/2;
				double combo = 0;
				if (x < rgbs.length/2 && y < rgbs[x].length/2) 
					combo = x+y;
				else if (x >= rgbs.length/2 && y < rgbs[x].length/2) 
					combo = (rgbs.length-x)+y;
				else if (x < rgbs.length/2 && y >= rgbs[x].length/2)
					combo = x+(rgbs[x].length-y);
				else
					combo = (rgbs.length-x)+(rgbs[x].length-y);
				
				intensity = (1-(combo/max))*0.7;
				
				Color c = new Color(rgbs[x][y]);
				int red = (int) ((fade.getRed()-c.getRed())*intensity+c.getRed());
				int green = (int) ((fade.getGreen()-c.getGreen())*intensity+c.getGreen());
				int blue = (int) ((fade.getBlue()-c.getBlue())*intensity+c.getBlue());
				red = cap255(red);
				green = cap255(green);
				blue = cap255(blue);
				newRGB[x][y] = darkenRGB(new Color(red,green,blue),intensity);
			}
		}
		return getImage(img,newRGB);
	}
	
	public static BufferedImage fadeEdges(BufferedImage img, Color fade, double factor) {
		//note: transparency is a value 0-1, 0 being invisible, 1 being opaque
		int[][] rgbs = getRGB(img);
		int[][] newRGB = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				double intensity = 0;
				double max = rgbs.length/2+rgbs[x].length/2;
				double combo = 0;
				if (x < rgbs.length/2 && y < rgbs[x].length/2) 
					combo = x+y;
				else if (x >= rgbs.length/2 && y < rgbs[x].length/2) 
					combo = (rgbs.length-x)+y;
				else if (x < rgbs.length/2 && y >= rgbs[x].length/2)
					combo = x+(rgbs[x].length-y);
				else
					combo = (rgbs.length-x)+(rgbs[x].length-y);
				
				intensity = (1-(combo/max))*factor;
				
				Color c = new Color(rgbs[x][y]);
				int red = (int) ((fade.getRed()-c.getRed())*intensity+c.getRed());
				int green = (int) ((fade.getGreen()-c.getGreen())*intensity+c.getGreen());
				int blue = (int) ((fade.getBlue()-c.getBlue())*intensity+c.getBlue());
				red = cap255(red);
				green = cap255(green);
				blue = cap255(blue);
				newRGB[x][y] = darkenRGB(new Color(red,green,blue),intensity);
			}
		}
		return getImage(img,newRGB);
	}
	
	
	
	private static int darkenRGB(Color c, double intensity) {
		intensity%=1; //get just the decimal
		intensity = 1-intensity;
		int red = cap255((int)(c.getRed()*intensity));
		int green = cap255((int)(c.getGreen()*intensity));
		int blue = cap255((int)(c.getBlue()*intensity));
		return (new Color(red,green,blue)).getRGB();
	}
	
	public static BufferedImage flipY(BufferedImage img) {
		//note: transparency is a value 0-1, 0 being invisible, 1 being opaque
		int[][] rgbs = getRGB(img);
		int[][] newRGB = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				newRGB[newRGB.length-x-1][y] = rgbs[x][y];
			}
		}
		return getImage(img,newRGB);
	}
	
	public static BufferedImage flipX(BufferedImage img) {
		//note: transparency is a value 0-1, 0 being invisible, 1 being opaque
		int[][] rgbs = getRGB(img);
		int[][] newRGB = new int[rgbs.length][rgbs[0].length];
		for (int x = 0; x < rgbs.length; x++) {
			for (int y = 0; y < rgbs[x].length; y++) {
				newRGB[x][newRGB[0].length-1-y] = rgbs[x][y];
			}
		}
		return getImage(img,newRGB);
	}
	
	private static int cap255(int num) {
		if (num > 255)
			return 255;
		if (num < 0)
			return 0;
		return num;
	}
	
	private static Color getColor(int r, int g, int b, int a) {
		return new Color(r,g,b,a);
	}
	
	private static Color getColor(int rgb) {
		Color c = new Color(rgb,true); //the true means that it has alpha
		return c;
	}
	
	private static int[][] getRGB(BufferedImage img) {
		int[][] rgbs = new int[img.getWidth()][img.getHeight()];
		for (int x = 0; x < rgbs.length; x++) 
			for (int y = 0; y < rgbs[x].length; y++) 
				rgbs[x][y] = img.getRGB(x, y);
		return rgbs;
	}
	
	private static BufferedImage getImage(BufferedImage imgToWrite, int[][] rgbs) {
		BufferedImage newImg = new BufferedImage(imgToWrite.getWidth(),imgToWrite.getHeight(),BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < rgbs.length; x++) 
			for (int y = 0; y < rgbs[x].length; y++) 
				newImg.setRGB(x, y, rgbs[x][y]);
		return newImg;
	}
}
