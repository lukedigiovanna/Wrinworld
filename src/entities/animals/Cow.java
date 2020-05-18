package entities.animals;

import java.awt.Image;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.Path;
import entities.enemies.Zombie;
import items.consumables.Food;
import items.consumables.Potion;
import main.Program;
import misc.MathUtils;
import misc.Point;

public class Cow extends Animal {
	public Cow(double x, double y) {
		super(x,y,0.6875,0.5);
		this.setHealth(new Health(15));
		EntityDrop[] drops = {new EntityDrop(0.5,2,new Food(Food.Type.RAW_BEEF)),
				new EntityDrop(0.5,2,new Food(Food.Type.BERRIES)),
						new EntityDrop(1,1,new Potion(Potion.Type.REGENERATION)),
						new EntityDrop(1,1,new Potion(Potion.Type.QUICK_FEET)),
						new EntityDrop(1,1,new Potion(Potion.Type.HEAL)),
						new EntityDrop(1,1,new Potion(Potion.Type.POSEIDONS_TOUCH)),
				};
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
		Entity e = new Cow(this.position.x,this.position.y);
		e.setHealth(new Health(e.getHealth().maxHealth));
		return e;
	}

	@Override
	public Image entityImage() {
		return Program.getImage("entities/animals/cow.png");
	}
}
