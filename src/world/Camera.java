package world;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import display.ImageProcessor;
import entities.*;
import game.GameController;
import main.Program;
import misc.*;
import misc.Dimension;

public class Camera implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//position is the top left of the thing.
	private Position position;
	private Dimension dimension; //the dimension on the World. not the pixel dimension (which is dictated by the program defined width, height)
	private Entity focus;
	
	public Camera(Position p, Dimension d, Entity focus) {
		this.position = p;
		this.dimension = d;
		this.focus = focus;
	}
	
	public Camera(Position p, Dimension d) {
		this(p,d,null);
	}
	
	private double margin = 0.4; //0 is off the screen, 0.5 is the max
	private double speedReducer = 10;
	private void adjustToFocus() {
		if (focus == null)
			return;
		margin = MathUtils.clip(margin, 0, 0.5);
		double x = focus.getX()-position.x, y = focus.getY()-position.y;
		
		double ox = (dimension.width/2-(dimension.width/2*margin)), oy = (dimension.height/2-(dimension.height/2*margin));
		double w = (dimension.width/2+(dimension.width/2*margin)-((dimension.width/2)-(dimension.width/2*margin)));
		double h = ((dimension.height/2)+(dimension.height/2*margin)-((dimension.height/2)-(dimension.height/2*margin)));

		double leftBound = ox, rightBound = ox+w;
		double downBound = oy, upBound = oy+h;
		
		if (x < leftBound)
			position.x-=Math.sqrt(Math.abs(leftBound-x)*2)/speedReducer*focus.getMaxSpeed()/2;
		if (x+focus.getWidth() > rightBound)
			position.x+=Math.sqrt(Math.abs(x+focus.getWidth()-rightBound)*2)/speedReducer*focus.getMaxSpeed()/2;
		if (y < downBound)
			position.y-=Math.sqrt(Math.abs(downBound-y)*2)/speedReducer*focus.getMaxSpeed()/2;
		if (y+focus.getHeight() > upBound)
			position.y+=Math.sqrt(Math.abs(y+focus.getHeight()-upBound)*2)/speedReducer*focus.getMaxSpeed()/2;
	}
	
	public BufferedImage get(World world, EntityList entities) {
		adjustToFocus();
		
		int pixelWidth = Program.DISPLAY_WIDTH, pixelHeight = Program.DISPLAY_HEIGHT;
		BufferedImage img = new BufferedImage(pixelWidth,pixelHeight,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 50));
		int width = g.getFontMetrics().stringWidth("CAMERA VIEW");
		g.drawString("CAMERA VIEW", img.getWidth()/2-width/2, img.getHeight()/3);
		
		ArrayList<WorldObject> objects = new ArrayList<WorldObject>();
		if (world != null)
			objects.addAll(world.get(this));
		if (entities != null)
			objects.addAll(entities.get());
		
		for (WorldObject o : objects) {
			//convert from world object location to location on the camera pixel
			if (o == null)
				continue;
				
			double x = o.getX()-position.x, y = o.getY()-position.y;
			if (x+o.getWidth() < 0 || x-o.getWidth() > dimension.width || y+o.getHeight() < 0 || y-o.getHeight() > dimension.height)
				continue; //dont worry about drawing this object since it is not within viewing of the camera
			
			int pX = (int)((x/dimension.width)*pixelWidth), pY = (int)((y/dimension.height)*pixelHeight);
			int pWidth = (int)((o.getWidth()/dimension.width)*pixelWidth), pHeight = (int)((o.getHeight()/dimension.height)*pixelHeight);
			
			pWidth+=1;pHeight+=1;
			
			BufferedImage oImg = o.getImage();
			double rotation = o.getRotation();
			double rx = pX+pWidth/2, ry = pY+pHeight/2;
			g.rotate(rotation,rx,ry); //is rotated about the center of the thing
			g.drawImage(ImageProcessor.darken(oImg,1-o.getLightValue()), pX, pY, pWidth, pHeight, null);
			g.rotate(-rotation,rx,ry);
			
			if (o instanceof Entity) {
				BufferedImage hb = ((Entity)o).getHealthBar();
				if (hb != null) {
					//draw it
					int height = (int)((pHeight/oImg.getHeight())*2); 
					g.drawImage(hb, pX, pY-height*2, pWidth, height, null);
				}
			}
		}
		//darkens the image to the time and fades the edges
		if (focus != null) {
			Color c = Color.BLACK;
			double inten = 0.5;
			inc+=0.4;
			double bonus = Math.sin(inc)*0.1;
			if (focus.getHealth().percent() < 0.2) {
				c = Color.RED;
				inten = 0.4-focus.getHealth().displayPercent()+bonus;
			}
			//img = ImageProcessor.fadeEdges(ImageProcessor.darken(img,darkenIntensity),c,inten);
			img = ImageProcessor.fadeEdges(img,c,inten);
		}
		
		g.dispose();
		
		return img;
	}
	
	private double inc = 0;
	
	public Entity getFocus() {
		return focus;
	}
	
	public Position position() {
		return position;
	}
	
	public Dimension dimension() {
		return dimension;
	}
	
	public void zoomOut() {
		zoom(1.25);
	}
	
	public void zoomIn() {
		zoom(0.75);
	}
	
	public void zoom(double factor) {
		position.x-=(dimension.width*factor-dimension.width)/2;
		position.y-=(dimension.height*factor-dimension.height)/2;
		dimension.width*=factor;
		dimension.height*=factor;
	}
}
