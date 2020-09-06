package mapMaking;

import java.awt.event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.image.*;


import world.*;
import world.gridComponent.*;
import misc.*;
import misc.Dimension;

public class MapMakerPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private World world;
	private Camera camera;
	
	int button = -1;
	java.awt.Point previousMousePosition = null;
	public MapMakerPanel() {
		this.setFocusable(true);
		
		String input = JOptionPane.showInputDialog(null,"Enter:\nA size to make a new world\nA world name to load one");
		int size = 0;
		try {
			size = Integer.parseInt(input);
			world = new World(size,size);
			for (double x = -world.width()/2.0; x < world.width()/2.0; x+=0.5)
				for (double y = -world.width()/2.0; y < world.width()/2.0; y+=0.5)
					world.add(new GridComponent(GridComponent.Component.GRASS,x,y));
		} catch (NumberFormatException e) {
			if (input.contentEquals(""))
				System.exit(0);
			world = World.loadFromFile("library/worlds/"+input+".WORLD");
		}
		
		//DEFAULT DIMENSION
		camera = new Camera(new Position(-4,-4), new Dimension(8,8));
		
		img = new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB);
		
		 
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (button == MouseEvent.BUTTON1)
					drawTile(e);
				else if (button == MouseEvent.BUTTON2){
					//if control is down
					if (previousMousePosition == null) {
						previousMousePosition = e.getPoint();
						return;
					}
					
					double xChange = e.getX()-previousMousePosition.getX(), yChange = e.getY()-previousMousePosition.getY();
					//one change is one change on the panel.. so lets change this to one change on the world
					double worldXChange = (xChange/getWidth())*camera.dimension().width, worldYChange = (yChange/getHeight())*camera.dimension().height;
					camera.position().x-=worldXChange;
					camera.position().y-=worldYChange;
					
					previousMousePosition = e.getPoint();
				}
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				button = e.getButton();
				if (e.getButton() == MouseEvent.BUTTON1)
					drawTile(e);
				previousMousePosition = e.getPoint();
			}
			
			public void mouseReleased(MouseEvent e) {
				button = -1;
				previousMousePosition = e.getPoint();
			}
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double rot = e.getPreciseWheelRotation();
				rot = MathUtils.clip(rot, -0.1, 0.1);
				double factor = 1+rot;
				camera.zoom(factor);
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_S:
					saveWorld("library/worlds/"+JOptionPane.showInputDialog(null,"What is the name?")+".WORLD");
					break;
				case KeyEvent.VK_L:
					loadWorld("library/worlds/"+JOptionPane.showInputDialog(null,"What is the name?")+".WORLD");
				}
			}
		});
		
		GridComponent.Component[] comps = GridComponent.COMPONENTS;
		cb = new JComboBox<Object>(comps);
		add(cb);
		
		final JButton toggleWall = new JButton("Toggle wall on?");
		toggleWall.addActionListener(new ActionListener() { 
		@Override
		public void actionPerformed(ActionEvent e) {
			putWall = !putWall;
			if (putWall)
				toggleWall.setText("Toggle wall off?");
			else
				toggleWall.setText("Toggle wall on?");
		} } );
		add(toggleWall);
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						
					}
					draw();
				}
			}
		});
		t.start();
	}	
	JComboBox<Object> cb;
	
	private boolean putWall = false;
	public void drawTile(MouseEvent e) {
		requestFocus();
		world.add(new GridComponent((GridComponent.Component)cb.getSelectedItem(),mouseLocationOnWorld(e).x,mouseLocationOnWorld(e).y,putWall));
	}
	
	public misc.Position mouseLocationOnWorld(MouseEvent e) {
		//Point imgLoc = mouseLocationOnImage(e);
		double x = (double)e.getX()/getWidth()*camera.dimension().width+camera.position().x, y = (double)e.getY()/getHeight()*camera.dimension().height+camera.position().y;
		x = MathUtils.floor(x, 0.5);
		y = MathUtils.floor(y, 0.5);
		return new Position(x,y);
	}
	
	public Point mouseLocationOnImage(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		//convert to where the x, y is on the buffered image
		double xScale = (double)getWidth()/img.getWidth();
		double yScale = (double)getHeight()/img.getHeight();
		int iX = (int)(x/xScale), iY = (int)(y/yScale);
		return (new Point(iX,iY));
	}
	
	private BufferedImage img = new BufferedImage(600,600,BufferedImage.TYPE_INT_ARGB);
	
	public void draw() {
		Graphics g = img.getGraphics();
		g.drawImage(camera.get(world, null), 0, 0, img.getWidth(), img.getHeight(), null);
		//what the fuck
		//ok now draw the coordinate numbers
		g.setColor(Color.BLACK);
		g.fillRect(img.getWidth()/2-1, 0, 2, img.getHeight());
		g.fillRect(0, img.getHeight()/2, img.getWidth(), 2);
//		double inc = camera.dimension().width/5;
//		for (double x1 = x; x1 < x+width; x1+=inc) {
//			int px = (int) ((x1-x)/camera.dimension().width*img.getWidth()),
//				py = img.getHeight()/2;
//			g.drawString(MathUtils.round(x1, 0.25)+"", px, py);
//		}
//		inc = camera.dimension().height/5;
//		for (double y1 = y; y1 < y+height; y1+=inc) { 
//			int px = img.getWidth()/2,
//					py = (int) ((y1-y)/camera.dimension().height*img.getHeight());
//			g.drawString(""+MathUtils.round(y1, 0.25), px, py);
//		}
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
	}
	
	public void saveWorld(String fp) {
		World.saveToFile(world, fp);
	}
	
	public void loadWorld(String fp) {
		world = World.loadFromFile(fp);
	}
}
