package world.gridComponent;

import java.awt.image.*;

import misc.*;
import misc.Dimension;
import world.WorldObject;
import animation.Animation;
import main.Program;

//any component of the map that is not an entity.
//things like: paths, trees, water, wood, chests
//models can be made with these grid components 

public class GridComponent extends WorldObject {
	private static final long serialVersionUID = 1L;
	
	public static final double STANDARD_SIZE = 0.5;
	
	private Component type;
	
	public GridComponent(Component type, double x, double y, boolean t) {
		super(new Position(x,y),new Dimension(type.width,type.height));
		this.type = type;
		this.position.priority = type.priority;
		this.setHasCollision(t);
	}
	
	public GridComponent(Component type, double x, double y) {
		this(type,x,y,false);
	}
	
	public void update() {
		type.update();
	}
	
	public BufferedImage getImage() {
		return type.getImage();
	}
	
	public String getID() {
		String s = type.name();
		s = s.toLowerCase();
		return s;
	}
	
	public static final Component[] COMPONENTS = {Component.GRASS,Component.WATER,Component.DIRT,Component.COBBLESTONE,Component.SAND,Component.TREE,
			Component.BRICK,Component.GLASS,Component.PLANKS,Component.RAINBOW,Component.PATH,Component.ROSE1,Component.DANDELION1,Component.CLOSED_CHEST,Component.VOID,
			Component.HORIZONTAL_FENCE,Component.VERTICAL_FENCE,Component.BOTTOM_LEFT_FENCE,Component.BOTTOM_RIGHT_FENCE,Component.TOP_LEFT_FENCE,Component.TOP_RIGHT_FENCE,Component.PURPLE_FLOWER,Component.BLUE_FLOWER,Component.ORANGE_FLOWER,
			Component.FALLEN_LOG,Component.BERRY_BUSH,Component.NO,Component.ROCKS,Component.FIRE};
	
	public static final Component[] FLOWERS = {Component.ROSE1,Component.DANDELION1,Component.BLUE_FLOWER,Component.ORANGE_FLOWER,Component.PURPLE_FLOWER};
	
	public enum Component {
		GRASS("grass.png"),
		WATER("water","water_",4),
		DIRT("dirt.png"),
		COBBLESTONE("cobblestone.png"),
		SAND("sand.png"),
		TREE("tree.png", 0.5, 1.0, 1.0),
		BRICK("bricks.png"),
		GLASS("glass.png",2.0),
		RAINBOW("rainbow","rainbow_",3),
		PLANKS("planks.png"),
		PATH("path.png"),
		ROSE1("rose1.png", 1.0),
		DANDELION1("dandelion1.png", 1.0),
		PURPLE_FLOWER("purpleflower.png",1.0),
		BLUE_FLOWER("blueflower.png",1.0),
		ORANGE_FLOWER("orangeflower.png",1.0),
		FALLEN_LOG("log.png",1.0),
		BERRY_BUSH("berrybush.png",1.0),
		NO("no.png",1.0),
		FIRE("fire","fire_",5,1.0),
		CLOSED_CHEST("closedchest.png",1.0),
		HORIZONTAL_FENCE("horizontalfence.png",1.0),
		VERTICAL_FENCE("verticalfence.png",1.0),
		BOTTOM_RIGHT_FENCE("bottomrightfence.png",1.0),
		BOTTOM_LEFT_FENCE("bottomleftfence.png",1.0),
		TOP_RIGHT_FENCE("toprightfence.png",1.0),
		TOP_LEFT_FENCE("topleftfence.png",1.0),
		ROCKS("rocks.png",1.0),
		VOID("void.png"); //its a blank image
		
		//either uses an animation, a single image, or a random image from an array
		Animation animation;
		BufferedImage image; 
		BufferedImage[] images;
		int index = -1;
		
		double width = STANDARD_SIZE, height = STANDARD_SIZE, priority = 0;
		
		String texturePack = "default";
		String tileFolder = "tile_textures/texture_packs/"+texturePack+"/";
		//a lot of constructors
		Component(String folderName, String prefix, int fps) { //uses an animation
			animation = new Animation(tileFolder+folderName,prefix,fps);
		}
		
		Component(String fileName) {
			image = Program.getImage(tileFolder+fileName);
		}
		
		Component(String fileName, double width, double height) {
			this.width = width;
			this.height = height;
			image = Program.getImage(tileFolder+fileName);
		}
		
		Component(String folderName, String prefix, int fps, double width, double height) {
			this.width = width;
			this.height = height;
			animation = new Animation(tileFolder+folderName,prefix,fps);
		}
		
		//with priorities
		Component(String folderName, String prefix, int fps, double priority) { //uses an animation
			animation = new Animation(tileFolder+folderName,prefix,fps);
			this.priority = priority;
		}
		
		Component(String fileName, double priority) {
			image = Program.getImage(tileFolder+fileName);
			this.priority = priority;
		}
		
		Component(String fileName, double width, double height, double priority) {
			this.width = width;
			this.height = height;
			image = Program.getImage(tileFolder+fileName);
			this.priority = priority;
		}
		
		Component(String folderName, String prefix, int fps, double width, double height, double priority) {
			this.width = width;
			this.height = height;
			animation = new Animation(tileFolder+folderName,prefix,fps);
			this.priority = priority;
		}
		
		public void update() {
			if (animation != null)
				animation.update();
		}
		
		public void randomizeIndex() {
			this.index = (int)(Math.random()*images.length);
		}
		
		public BufferedImage getImage() {
			if (animation != null) 
				return animation.getCurrentFrame();
			else if (images != null)
				return images[index];
			else 
				return image;
		}
	}
}