package entities.enemies;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.miscellaneous.Coin;
import entities.miscellaneous.Particle;
import entities.player.Player;
import entities.projectiles.Projectile;
import main.Program;
import misc.*;

public class Wizard extends Enemy {
	private static final long serialVersionUID = 1L;

	public Wizard(double x, double y) {
		super(x,y,0.5,1.0);
		this.health = new Health(35);
		EntityDrop[] drops = {
			//new EntityDrop(0.6,2,new Food(Food.Type.ZOMBIE_MEAT)),
			new EntityDrop(0.3,4, new Coin(0,0,Coin.Type.BRONZE)),
			new EntityDrop(0.05,2, new Coin(0,0,Coin.Type.SILVER)),
			new EntityDrop(0.025,1, new Coin(0,0,Coin.Type.GOLD)),
			new EntityDrop(0.0025,1, new Coin(0,0,Coin.Type.JEWEL)),
		};
		this.drops = drops;
		this.xpOnDrop = 5;
	}
	
	private double followDistance = MathUtils.randomInRange(8, 9), minDistance = 3.5, speed = MathUtils.randomInRange(0.125, 0.175);
	private double cooldownPeriod = 70, cooldownStatus = 0;
	public void individualUpdate() {
		Player p = game.player();
		this.clearVelocity();
		double dist = this.distance(p);
		double rad = this.angle(p);
		if (dist < followDistance && dist > minDistance)
			this.setVelocity(new Vector2D(speed,rad,Vector2D.FORM_BY_RADIAN));
		if (dist < minDistance+0.01)
			cooldownStatus++;
		if (cooldownStatus >= cooldownPeriod) {
			cooldownStatus = 0;
			double out = 0.6;
			double size = 0.2;
			double x = getCenterX()+Math.cos(rad)*out-size;
			double y = getCenterY()+Math.sin(rad)*out-size;
			Projectile toThrow = new Projectile(Projectile.Type.BLUE_FIREBALL,x,y,new Vector2D(3,rad,Vector2D.FORM_BY_RADIAN), this);
			game.entities().add(toThrow);
		}
		if (Math.random() < 0.01 && this.distance(p) < followDistance) {
			//teleport across the player
			double transform = dist*2;
			for (int i = 0; i < MathUtils.randomInRange(6, 9); i++)
				this.add(Particle.Type.MAGIC,1.0);
			this.setPosition(new Position(getX()+Math.cos(rad)*transform,getY()+Math.sin(rad)*transform));
		}
	}
	
	public Image entityImage() {
		BufferedImage img = Program.getImage("entities/enemies/bluewizard.png");
		return img;
	}
	
	public Entity replicate() {
		Entity e = new Wizard(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
