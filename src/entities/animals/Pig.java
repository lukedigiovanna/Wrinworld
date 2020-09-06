package entities.animals;

import java.awt.Image;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.Path;
import main.Program;
import misc.MathUtils;
import misc.Point;
import items.consumables.Food;

public class Pig extends Animal {
	private static final long serialVersionUID = 1L;

	public Pig(double x, double y) {
		super(x,y,0.6875,0.5);
		this.setHealth(new Health(15));
		EntityDrop[] drops = {new EntityDrop(1,2,new Food(Food.Type.RAW_PORK))};
		this.drops = drops;
	}

	private Path path = new Path(this);
	private double speed = 0.2;
	@Override
	public void individualUpdate() {
		if (Math.random() < 0.00125) {
			//make a new path..
			path = new Path(this);
			double distance = MathUtils.randomInRange(0.25, 0.5);
			for (double i = 0; i < Math.PI*2; i+=0.3) {
				path.add(new Point(Math.cos(i)*distance+this.getX(),Math.sin(i)*distance+this.getY()),speed);
			}
		}
		path.walkPath();
	}

	@Override
	public Entity replicate() {
		Entity e = new Pig(this.position.x,this.position.y);
		e.setHealth(new Health(e.getHealth().maxHealth));
		return e;
	}

	@Override
	public Image entityImage() {
		return Program.getImage("entities/animals/pig.png");
	}
}
