package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import misc.*;

//holds a list of points that an entity will follow
public class Path implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Point> points;
	private List<Double> speeds;
	private int position = 0;
	private Entity entity;
	private boolean loop =  false;
	
	public Path(Entity entity) {
		this.entity = entity;
		points = new ArrayList<Point>();
		speeds = new ArrayList<Double>();
	}
	
	public Path loop() {
		loop = true;
		return this;
	}
	
	public void walkPath() {
		if (completed()) // just forget about this path.
			return;

		double angle = MathUtils.angle(entity.getX(), entity.getY(), currentPoint().x, currentPoint().y);
		
		entity.setVelocity(new Vector2D(this.currentSpeed(),angle,Vector2D.FORM_BY_RADIAN));
		
		//if the entity is within 0.1 of the point were looking at then move on to the next one
		if (MathUtils.distance(entity.getX(), entity.getY(), currentPoint().x, currentPoint().y) < 0.1)
			position++; 
	}
	
	public Point getPoint(int index) {
		if (index < 0 || index >= points.size())
			return null;
		return points.get(index);
	}
	
	public boolean completed() {
		if (position >= points.size()) {
			if (loop)
				position = 0;
			else
				return true;
		}
		return false;
	}
	
	public double getSpeed(int index) {
		if (index < 0 || index >= speeds.size())
			return 0;
		return speeds.get(index);
	}
	
	public double currentSpeed() {
		if (completed())
			return 0;
		return getSpeed(position);
	}
	
	public Point currentPoint() {
		if (completed())
			return null;
		return getPoint(position);
	}
	
	public void add(Point p, double speed) {
		points.add(p);
		speeds.add(speed);
	}
}
