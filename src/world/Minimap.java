package world;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

import game.Game;

public class Minimap implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private World world;
	private WorldObject focus;
	private Game game;
	
	private double radius = 20;
	private double resolution = 0.25; //how much space in the world per pixel
	
	private boolean active = true;
	
	public Minimap(Game game) {
		world = game.getWorld();
		focus = game.getCamera().getFocus();
		this.game = game;
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {};
					if (active)
						redraw();
				}
			}
		});
		t.start();
	}

	private BufferedImage img = new BufferedImage((int)(radius/resolution),(int)(radius/resolution),BufferedImage.TYPE_INT_ARGB);
	
	public void redraw() {
		Graphics g = img.getGraphics();
		double x = focus.getX(), y = focus.getY(); //the middle
		List<WorldObject> list = world.get(x-radius,y-radius,radius*2,radius*2);
		int size = (int)(img.getWidth()/radius*resolution);
		for (int k = list.size()-1; k >= 0; k--) {
			WorldObject o = list.get(k);
			//if the object is not within viewing of the map
			if (o.getX() < x-radius || o.getX()+o.getWidth() > x+radius || o.getY() < y-radius || o.getY()+o.getHeight() > y+radius)
				continue; //this object is not within viewing
			double gx = (int)(x+radius/2)-(int)o.getX(), gy = (int)(y+radius/2)-(int)o.getY();
			gx/=resolution;gy/=resolution;
			int ax = (int)gx, ay = (int)gy;
			if (ax < 0 || ax >= img.getWidth() || ay < 0 || ay >= img.getHeight())
				continue;
			
			
			g.drawImage(o.getImage(), ax, ay, (int)(1/resolution), (int)(1/resolution), null);
			
//			BufferedImage i = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
//			i.getGraphics().drawImage(o.getImage(), 0, 0, 1, 1, null);
//			img.setRGB(ax, ay, i.getRGB(0, 0));
		}
		img.setRGB(img.getWidth()/2, img.getHeight()/2, java.awt.Color.YELLOW.getRGB());
	}
	
	public Image getImage() {
		return display.ImageProcessor.flipX(display.ImageProcessor.flipY(img));
	}
}
