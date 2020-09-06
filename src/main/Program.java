package main;

import java.awt.*;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import animation.Animation;
import display.DisplayController;
import misc.*;

//class with information and methods helpful to the application
public class Program {
	public static final String 
			VERSION = "Indev 0.6",
			CREATOR = "Luke DiGiovanna",
			BUSINESS = "LAD Games",
			GAME_NAME = "Wrinworld",
			RELEASE_INFO = "Development June 2019",
			FRAME_NAME = GAME_NAME+" "+VERSION+" | "+RELEASE_INFO;
	
	public static JFrame frame = new JFrame(FRAME_NAME);
	public static Panel panel;
	
	public static final int 
			SCREEN_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 
			SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
			DISPLAY_WIDTH = 580,
			DISPLAY_HEIGHT = 500,
			DISPLAY_SCALE = (int)Math.sqrt(DISPLAY_WIDTH*DISPLAY_HEIGHT);
	
	public static final int TICKS_PER_SECOND = 20;
	
	public static final Animation FIREWORK_ANIMATION = new Animation("firework","firework_",8);
	
	public static void initWithoutRunning() {
		DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
	}
	
	public static void init() {
		//this should be the first thing called in the main method
		panel = new Panel();
		Keyboard.init();
		Mouse.init();
		JFrame frame = new JFrame(FRAME_NAME);
		frame.setSize(SCREEN_WIDTH/2,(int)(SCREEN_HEIGHT/1.45));
		frame.setLocation(SCREEN_WIDTH/4,SCREEN_HEIGHT/2-(int)(SCREEN_HEIGHT/1.45/2));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(Program.panel);
		frame.requestFocus();
		frame.setVisible(true);
		
		BufferedImage img = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(img.getWidth()/2-1,0,2,img.getHeight());
		g.fillRect(0, img.getHeight()/2-1, img.getWidth(), 2);
		
//		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(/*getImage("cursor.png")*/img, new Point(8,8), "red");
//		frame.getContentPane().setCursor(c);
		
		Image i = (new ImageIcon("library/assets/images/icon.png")).getImage();
		frame.setIconImage(i);
		
		DisplayController.setDisplay(DisplayController.MAIN_DISPLAY);
	}
	
	public static double fps() {
		if (panel == null)
			return 0;
		return panel.fps();
	}
	
	public static double tps() {
		if (panel == null)
			return 0;
		return panel.tps();
	}
	
	private static BufferedImage spriteSheet;
	public static BufferedImage getImage(String filePath) {
//		if (filePath.indexOf("library/assets/images/") == -1) 
//			filePath = "library/assets/images/"+filePath;
//		File file = new File(filePath);
//		if (file.exists()) {//ie.. the image exists
//			Image img = Toolkit.getDefaultToolkit().getImage(filePath);
//			return img;
//		} else {
//			//return the texture not found error image.. black and purple checkerboard thing
//		}
		
		if (spriteSheet == null) {
			//initialize sprite sheet
			Image ss = (new ImageIcon("library/assets/images/sprites.png")).getImage();
			if (ss.getWidth(null) < 1 || ss.getHeight(null) < 1)
				spriteSheet = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
			else
				spriteSheet = new BufferedImage(ss.getWidth(null),ss.getHeight(null),BufferedImage.TYPE_INT_ARGB);
			spriteSheet.getGraphics().drawImage(ss, 0, 0, spriteSheet.getWidth(), spriteSheet.getHeight(), null);
		}
		
		
		//if the path to the sheet was included, remove it
		if (filePath.indexOf("library/assets/images/") == 0)
			filePath = filePath.substring("library/assets/images/".length());
		//traverse the JSON file for the image info
		File file = new File("library/assets/images/sprites.JSON");
		if (!file.exists()) 
			return getImageNotFound();
		try {
			//open a reading stream
			BufferedReader br = new BufferedReader(new FileReader(file));
			//find the image path.. if it doesn't exist in the sprite look for it as a file. then quit
			String s = "";
			while ((s = br.readLine()) != null) {
				if (s.indexOf("\"filename\": ") > -1) {
					String fileName = s.substring(s.indexOf("\"filename\": ")+"\"filename\": \"".length(),s.length()-2);
					if (filePath.equals(fileName)) {
						//we found the image to get
						//now get the x, y, w, h
						String frameInfo = br.readLine();						
						//find index of "x", "y", etc..
						frameInfo = frameInfo.substring(frameInfo.indexOf("\"x\"")+4);
						int x = Integer.parseInt(frameInfo.substring(0,frameInfo.indexOf(",")));
						frameInfo = frameInfo.substring(frameInfo.indexOf(",")+5);
						int	y = Integer.parseInt(frameInfo.substring(0,frameInfo.indexOf(",")));
						frameInfo = frameInfo.substring(frameInfo.indexOf(",")+5);
						int	w = Integer.parseInt(frameInfo.substring(0,frameInfo.indexOf(","))); 
						frameInfo = frameInfo.substring(frameInfo.indexOf(",")+5,frameInfo.indexOf("}"));
						int	h = Integer.parseInt(frameInfo);

						return spriteSheet.getSubimage(x, y, w, h);
					}
				}
			}
			//at this point the image was not in the sprite sheet. so just look for it in the file directory
			file = new File("library/assets/images/"+filePath);
			if (file.exists()) {
				Image img = (new ImageIcon(file.getPath())).getImage();
				//convert to buffered image
				BufferedImage i = new BufferedImage(MathUtils.min(img.getWidth(null),1),MathUtils.min(img.getHeight(null),1),BufferedImage.TYPE_INT_ARGB);
				i.getGraphics().drawImage(img, 0, 0, i.getWidth(), i.getHeight(), null);
				return i;
			}
			br.close();
			return getImageNotFound();
		} catch (Exception e) {
			e.printStackTrace();
			return getImageNotFound();
		}
	}
	
	//black and magenta checkerboard error
	public static BufferedImage getImageNotFound() {
		BufferedImage img = new BufferedImage(2,2,BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, Color.MAGENTA.getRGB());
		img.setRGB(0, 1, Color.BLACK.getRGB());
		img.setRGB(1, 0, Color.BLACK.getRGB());
		img.setRGB(1,1,Color.MAGENTA.getRGB());
		return img;
	}
	
	public static Point getFrameLocation() {
		return panel.getMousePosition();
		//return new Point(frame.getX(),frame.getY());
	}
	
	public static Point getMouseLocationOnFrame() {
		Point locationOnScreen = getMouseLocationOnScreen();
		Point frameLocation = null;
//		try {
//			frameLocation = frame.getLocationOnScreen();
//		} catch (Exception e) {
//			return new Point(-1,-1);
//		}
		frameLocation = new Point(frame.getX(),frame.getY());
		int x = locationOnScreen.x-frameLocation.x;
		int y = locationOnScreen.y-frameLocation.y;
		return new Point(x,y);
	}
	
	public static Point getMouseLocationOnPanel() {
		boolean frameDecoration = !frame.isUndecorated();
		Point locationOnFrame = getMouseLocationOnFrame();
		int x = locationOnFrame.x;
		int y = locationOnFrame.y;
		if (frameDecoration)
			y-=30;
		return new Point(x,y);
	}
	
	public static Point getMouseLocationOnScreen() {
		Point p  = MouseInfo.getPointerInfo().getLocation();
		return p;
	}
}
