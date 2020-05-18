package world;

import misc.*;
import java.awt.image.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import game.GameController;

//any part of the world such as entities, world components, etc.
public abstract class WorldObject implements Comparable<WorldObject>, Serializable {
	private static final long serialVersionUID = 1L;
	
	//has a position and dimension
	protected Position position; //values are to be interpreted as a meter..
	protected Dimension dimension; 
	protected World world;
	private Position hitboxPosition;
	private Dimension hitboxDimension;
	private double rotation = 0;
	private double lightValue = 1.0;
	private boolean collision = false;
	private java.awt.Point curChunkPos;
	
	private Position[] positionHistory = new Position[20]; //only saves 10 frames back
	
	public WorldObject(double x, double y, double width, double height) {
		this(new Position(x,y), new Dimension(width, height));
	}
	
	public WorldObject(double x, double y, Dimension d) {
		this(new Position(x,y), d);
	}
	
	public WorldObject(Position p, double width, double height) {
		this(p, new Dimension(width,height));
	}
	
	public WorldObject(Position p, Dimension d) {
		position = p;
		dimension = d;
		//by default the hitbox is what is drawn
		hitboxPosition = new Position(0,0);
		hitboxDimension = new Dimension(d.width,d.height);
		world = GameController.game().getWorld();
	}
	
	public abstract BufferedImage getImage();
	
	public abstract String getID();
	
	//does not NEED to be implemented.. optional. but a world object reference can still call this method
	public void update() {
		
	}
	
	public void updateForAll() {
		correctChunk();
		//update position/velocity history
		for (int i = positionHistory.length-1; i > 0; i--) {
			if (positionHistory[i-1] != null)
				positionHistory[i] = new Position(positionHistory[i-1].x,positionHistory[i-1].y);
		}
		positionHistory[0] = getPosition();
	}
	
	public void correctChunk() {
		if (world == null) return;
		
//		//if our x, y is outside of the current chunk boundary we are in.. then fix it..
//		//find what chunk we are in first
//		if (this.getPosition().equals(this.getPositionAtFrame(1)))
//			return;
		if (!(this instanceof LightSource))
			return;
		misc.Point chunkPosition = world.getChunkPositionOfObject(this);
		if (this.getX() >= chunkPosition.x && this.getX() <= chunkPosition.x+world.getChunkSize())
			if (this.getY() >= chunkPosition.y && this.getY() <= chunkPosition.y+world.getChunkSize())
				return; //we dont need to correct
		//else we need to remove it from the chunk then add it to the correct chunk
		List<WorldObject> curChunk = world.getChunkOfObject(this);
		curChunk.remove(this);
		List<WorldObject> nextChunk = world.getChunk(getX(), getY());
		if (nextChunk != null)
			nextChunk.add(this);
	}
	
	public void setChunkPosition(int x, int y) {
		this.curChunkPos = new java.awt.Point(x,y);
	}
	
	public java.awt.Point getChunkPosition() {
		return this.curChunkPos;
	}
	
	public Position getPositionAtFrame(int index) {
		if (positionHistory == null)
			positionHistory = new Position[20];
		if (index < 0 || index > positionHistory.length-1)
			return null;
		Position p = positionHistory[index];
		if (p == null) { return null; }
		return p.copy();
	}
	
	public boolean hasCollision() {
		return collision;
	}
	
	public void setHasCollision(boolean t) {
		this.collision = t;
	}
	
	public boolean colliding(WorldObject o) {
		//all world objects are rectangles.. so use basic collision detection if any lines intersect
		if (o == null)
			return false;
		
		//te = This Edge, oe = Other Edge
		
		Line[] te = this.getHitboxLines();
		Line[] oe = o.getHitboxLines();
		
		for (Line tl : te)
			for (Line ol : oe)
				if (tl.intersects(ol)) 
					return true;

		return false;
	}
	
	public Line[] getHitboxLines() {
		Line[] lines = new Line[4]; //4 sides
		double horizontalAngle = this.getRotation();
		double verticalAngle = horizontalAngle+Math.PI/2; //plus 90 degrees since rects form 90 degree angles
		double horizontalSlope = 100000;
		double verticalSlope = 100000; //essentially infinite slope.. resembles a vertical line
		if (!(horizontalAngle%(Math.PI/2) == 0 && horizontalAngle%Math.PI != 0)) //make sure not to try and find the tan such that the value is infinity
			horizontalSlope = Math.tan(horizontalAngle);
		if (!(verticalAngle%(Math.PI/2) == 0 && verticalAngle%Math.PI != 0))
			verticalSlope = Math.tan(verticalAngle);
		//how to find y-intercept?
		//we can find a point that would be on the line..
		//we know the vertical line will be width/2 away from the center.. so use that logic to determine x, y on line
		double x = getCenterX()+Math.cos(horizontalAngle)*(getWidth()/2);
		double y = getCenterY()+Math.sin(horizontalAngle)*(getWidth()/2);
		//from this we know the slope and a point on the line.. so we can find the y-intercept, which is where x = 0
		//y = mx+b
		//SYSTEM OF EQUATIONS:
		/*
		 * y = (slope)x+b
		 * y = (slope)0+b
		 * (y = b)
		 * find b using first equation
		 * b=y-(slope)x
		 */
		double b = y-verticalSlope*x;
		//from this the first equation is done.. 
		lines[0] = new Line(verticalSlope,b);
		//the equation parallel to this will have a change in b equal to the distance between an x, y1 on one and an x, y2 on the other
		x = getCenterX()-Math.cos(horizontalAngle)*(getWidth()/2);
		y = getCenterY()-Math.sin(horizontalAngle)*(getWidth()/2);
		b = y-verticalSlope*x;
		lines[1] = new Line(verticalSlope,b);
		//repeat the same process but use the verticalAngle
		x = getCenterX()+Math.cos(verticalAngle)*(getHeight()/2);
		y = getCenterY()+Math.sin(verticalAngle)*(getHeight()/2);
		b = y-horizontalSlope*x;
		lines[2] = new Line(horizontalSlope,b);
		x = getCenterX()-Math.cos(verticalAngle)*(getHeight()/2);
		y = getCenterY()-Math.sin(verticalAngle)*(getHeight()/2);
		b = y-horizontalSlope*x;
		lines[3] = new Line(horizontalSlope,b);
		//now to determine the domain.. 
		//we know the length of the lines and the angle that they create and their relative position to the center
		//with this information the left side and right side can be determined
		
		//or we just see where the lines we have already made intersect.
		/* 
		 * DIAGRAM:
		 *1---------2<-[3]
		 * |       | 
		 * |<-[1]  |<-[0]
		 * |       | 
		 * |       |
		 *4---------3<-[2]
		 */
		double p1 = lines[1].getXIntersects(lines[3]),
			   p2 = lines[3].getXIntersects(lines[0]),
			   p3 = lines[0].getXIntersects(lines[2]),
			   p4 = lines[1].getXIntersects(lines[2]);
		lines[3].setDomain(p1, p2);
		lines[0].setDomain(p3, p2);
		lines[2].setDomain(p4, p3);
		lines[1].setDomain(p1, p4);
		return lines;
	}
	
	public class Line {
		private double domainLeft, domainRight;
		private double a, b;
		
		public Line(double slope, double yIntercept) {
			this.a = slope;
			this.b = yIntercept;
		}
		
		public boolean intersects(Line other) {
			double x = getXIntersects(other);
			return (this.inDomain(x) && other.inDomain(x));
		}
		
		public double getXIntersects(Line other) {
			double a1 = this.a, b1 = this.b;
			double a2 = other.getSlope(), b2 = other.getYIntercept();
			if (Math.abs(a1 - a2) < 0.001) //make sure no "divide by zero" error is encountered
				return -999999; //lines are parallel.. this number is so small it is unlikely it is in any domain
			double x = (-(b1-b2))/(a1-a2); //returns the x where the two lines intersect
			return x;
		}
		
		public boolean inDomain(double x) {
			return (x >= domainLeft && x <= domainRight);
		}
		
		public void setDomain(double side1, double side2) {
			if (side1 <= side2) {
				this.domainLeft = side1;
				this.domainRight = side2;
			} else {
				this.domainLeft = side2;
				this.domainRight = side1;
			}
		}
		
		public double getSlope() {
			return a;
		}
		
		public double getYIntercept() {
			return b;
		}
		
		public String toString() {
			return "y = "+a+"x + "+b+" ["+domainLeft+", "+domainRight+"]";
		}
	}
	
	public double distance(WorldObject e) {
		return MathUtils.distance(this.getCenterX(),this.getCenterY(),e.getCenterX(),e.getCenterY());
	}
	
	public double angle(WorldObject e) {
		return MathUtils.angle(this.getCenterX(),this.getCenterY(),e.getCenterX(),e.getCenterY());
	}
	
	public double getX() {
		return position.x;
	}
	
	public double getCenterX() {
		return position.x+dimension.width/2;
	}
	
	public List<WorldObject> getObjectsAround() {
		if (world == null)
			return new ArrayList<WorldObject>();
		List<WorldObject> objectsAround = this.world.get(getX()-2, getY()-2, 4+dimension.width, 4+dimension.height);
		return objectsAround;
	}
	
	public List<WorldObject> getWallsAround() {
		List<WorldObject> objectsAround = this.getObjectsAround();
		List<WorldObject> wallsAround = new ArrayList<WorldObject>();
		for (WorldObject o : objectsAround) 
			if (o.hasCollision()) {
				wallsAround.add(o);
			}
		return wallsAround;
	}
	
	public boolean collidingWithWall() {
		List<WorldObject> walls = getWallsAround();
		for (WorldObject w : walls)
			if (this.colliding(w)) {
				return true;
			}
		return false;
	}
	
	public void moveX(double dx) {
		if (!this.hasCollision()) {
			this.setX(getX()+dx);
			return;
		}
		//checks between the path and stops moving when you hit a wall
		double inc = 0.001*(Math.abs(dx)/dx);
		double moved = 0;
		while (Math.abs(moved) < Math.abs(dx)) {
			position.x+=inc;
			if (this.collidingWithWall()) {
				position.x-=inc;
				return;
			}
			moved+=inc;
		}
	}
	
	public void moveY(double dy) {
		if (!this.hasCollision()) {
			this.setY(getY()+dy);
			return;
		}
		double inc = 0.001*(Math.abs(dy)/dy);
		double moved = 0;
		while (Math.abs(moved) < Math.abs(dy)) {
			position.y+=inc;
			if (this.collidingWithWall()) {
				position.y-=inc;
				return;
			}
			moved+=inc;
		}
	}
	
	public void setX(double value) {
		this.position.x = value;
	}
	
	public void setY(double value) {
		this.position.y = value;
	}
	
	public double getCenterY() {
		return position.y+dimension.height/2;
	}
	
	public double getY() {
		return position.y;
	}
	
	public double getWidth() {
		return dimension.width;
	}
	
	public double getHeight() {
		return dimension.height;
	}
	
	public int pixelWidth() {
		return (int)(World.PIXELS_TO_METER*dimension.width);
	}
	
	public int pixelHeight() {
		return (int)(World.PIXELS_TO_METER*dimension.height);
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setPosition(Position p) {
		this.setX(p.x);
		this.setY(p.y);
	}
	
	public void setPosition(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public Position getRelativeHitboxPosition() {
		return new Position(getX()+getHitboxPosition().x,getY()+getHitboxPosition().y);
	}
	
	public Position getHitboxPosition() {
		if (this.hitboxPosition == null)
			return new Position(0,0);
		return this.hitboxPosition;
	}
	
	public Dimension getHitboxDimension() {
		if (this.hitboxDimension == null)
			return new Dimension(getWidth(),getHeight());
		return this.hitboxDimension;
	}
	
	public void setHitboxDimension(Dimension d) {
		this.hitboxDimension = d;
	}
	
	public void setHitboxPosition(Position p) {
		this.hitboxPosition = p;
	}
	
	public double getLightValue() {
		return lightValue;
	}
	
	private double queuedLightValue = this.lightValue;
	public double getQueuedLightValue() {
		return queuedLightValue;
	}
	
	public void queueLightValue(double lv) {
		this.queuedLightValue = MathUtils.clip(lv,0,1);
	}
	
	public void updateLightValue() {
		this.lightValue = queuedLightValue;
	}
	
	public void setRotation(double rot) {
		this.rotation = rot;
	}
	
	public double getRotation() {
		return this.rotation;
	}
	
	public BufferedImage newImage() {
		int width = (pixelWidth() <= 0) ? 1 : pixelWidth();
		int height = (pixelHeight() <= 0) ? 1 : pixelHeight();
		return new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
	}
	
	public BufferedImage newImage(int width, int height) {
		if (width <= 0)
			width = 1;
		if (height <= 0)
			height = 1;
		return new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	}
	
	public int compareTo(WorldObject o) {
		if ((MathUtils.equals(o.getX(), getX())) &&
		 (MathUtils.equals(o.getY(), getY())) &&
		 (MathUtils.equals(o.getHeight(), getHeight())) &&
		 (MathUtils.equals(o.getWidth(), getWidth())) &&
		 MathUtils.equals(this.position.priority, o.position.priority))
			return 0;
		double thisArea = this.getWidth()*this.getHeight(),
				theirArea = o.getWidth()*o.getHeight();
		
		//if the have the same position and height and priority then sort by just priority
		if (this.position.priority > o.position.priority)
			return 1;
		else 
			return -1;
	}
}
