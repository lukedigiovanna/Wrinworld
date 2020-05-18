package display.popup;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.npc.NPC;
import main.Program;

public class PopupBar {
	public static ArrayList<PopupBar> queue = new ArrayList<PopupBar>();
	
	public static void popup(String title, String message) {
		queue.add(new PopupBar(title,message));
	}
	
	public static void popup(PopupBar bar) {
		queue.add(bar);
	}
	
	public static void updateCurrent() {
		if (queue.size() < 1)
			return;
		PopupBar bar = queue.get(0);
		if (bar != null)
			bar.update();
	}
	
	public static void next() {
		if (queue.size() > 0 && (!FORCE_READ || queue.get(0).spoken()))
			queue.remove(0);
		//else nothing
	}
	
	public static boolean hasNext() {
		return queue.size() > 0;
	}
	
	public final static boolean FORCE_READ = true;
	
	public static final double WIDTH_PERCENT = 0.8, HEIGHT_PERCENT = 0.15;
	
	public static final int WIDTH = (int)(Program.DISPLAY_WIDTH*WIDTH_PERCENT), HEIGHT = (int)(Program.DISPLAY_HEIGHT*HEIGHT_PERCENT);
	
	//instance vars
	private String title, message, drawnMessage;
	private NPC npcOwner = null;
	
	public PopupBar(String title, String message) {
		this.title = title;
		this.message = message;
		drawnMessage = "";
	}
	
	public PopupBar(String title, String message, NPC owner) {
		this(title,message);
		this.npcOwner = owner;
	}
	
	private int rate = 2;
	public void update() {
		for (int i = 0; i < rate; i++)
			if (!spoken()) 
				drawnMessage+=message.charAt(drawnMessage.length());
	}
	
	public boolean spoken() {
		if (drawnMessage.length() >= message.length())
			return true;
		return false;
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		GradientPaint gradient = new GradientPaint(0,0,Color.BLUE,0,img.getHeight(),new Color(100,100,240));
		g.setPaint(gradient);
		int borderWidth = 3;
		g.fillRect(borderWidth, borderWidth, img.getWidth()-(borderWidth*2), img.getHeight()-(borderWidth*2));
		
		g.setFont(new Font("Arial",Font.BOLD,18));
		g.setColor(Color.BLACK);
		if (title == null)
			title = "Unknown Speaker";
		g.drawString(title, 20, 20);
		g.setFont(new Font("Arial",Font.PLAIN,13));
		String toDraw = drawnMessage;
		int lines = 0;
		while (toDraw.length() > 0) {
			//find the index
			int index = toDraw.length()-1;
			while (g.getFontMetrics().stringWidth(toDraw.substring(0,index+1)) > WIDTH-20) {
				//cut off a word
				index--;
				while (toDraw.charAt(index) != ' ')
					index--;
			}
			//now draw that
			String s = toDraw.substring(0,index+1);
			toDraw = toDraw.substring(index+1);
			if (s.charAt(0) == ' ')
				s = s.substring(1);
			misc.Graphics2.outlineText(g,s, 20, 40+(lines*g.getFont().getSize()),Color.GRAY.darker(),1);
			lines++;
		}
		if (spoken()) {
			g.setColor(Color.RED);
			g.setFont(new Font("Arial",Font.BOLD,13));
			g.drawString("< CLICK >", WIDTH-g.getFontMetrics().stringWidth("< CLICK >")-6, HEIGHT-13);
		}
		return img;
	}
}
