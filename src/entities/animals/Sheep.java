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
import items.misc.Miscellaneous;

public class Sheep extends Animal {
	private static final long serialVersionUID = 1L;

	public Sheep(double x, double y) {
		super(x,y,0.6875,0.5);
		this.setHealth(new Health(15));
		EntityDrop[] drops = {new EntityDrop(0.6,2,new Food(Food.Type.RAW_MUTTON)),
				new EntityDrop(0.4,1,new Miscellaneous(Miscellaneous.Type.WOOL))};
		this.drops = drops;
	}

	private Path path = new Path(this);
	private double speed = 0.2;
	@Override
	public void individualUpdate() {
		if (Math.random() < 0.00125) {
			//make a new path..
			path = new Path(this);
			double radian = MathUtils.randomInRange(0, Math.PI*2);
			double distance = MathUtils.randomInRange(0.25, 0.5);
			path.add(new Point(Math.cos(radian)*distance+this.getX(),Math.sin(radian)*distance+this.getY()),speed);
		}
		path.walkPath();
	}

	@Override
	public Entity replicate() {
		Entity e = new Sheep(this.position.x,this.position.y);
		e.setHealth(new Health(e.getHealth().maxHealth));
		return e;
	}

	@Override
	public Image entityImage() {
		return Program.getImage("entities/animals/sheep.png");
	}
}
