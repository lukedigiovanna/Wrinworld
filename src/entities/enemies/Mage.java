package entities.enemies;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.miscellaneous.Coin;
import entities.player.Player;
import entities.projectiles.Projectile;
import main.Program;
import misc.*;

public class Mage extends Enemy {
	public Mage(double x, double y) {
		super(x,y,0.5,1.0);
		this.health = new Health(40);
		EntityDrop[] drops = {
			//new EntityDrop(0.6,2,new Food(Food.Type.ZOMBIE_MEAT)),
			new EntityDrop(0.3,4, new Coin(0,0,Coin.Type.BRONZE)),
			new EntityDrop(0.05,2, new Coin(0,0,Coin.Type.SILVER)),
			new EntityDrop(0.025,1, new Coin(0,0,Coin.Type.GOLD)),
			new EntityDrop(0.005,1, new Coin(0,0,Coin.Type.JEWEL)),
		};
		this.drops = drops;
		this.xpOnDrop = 7;
	}
	
	private double followDistance = MathUtils.randomInRange(8, 9), minDistance = 3.5, speed = MathUtils.randomInRange(0.125, 0.175);
	private double cooldownPeriod = 60, cooldownStatus = 0;
	public void individualUpdate() {
		Player p = game.player();
		this.clearVelocity();
		if (this.distance(p) < followDistance && this.distance(p) > minDistance)
			this.setVelocity(new Vector2D(speed,this.angle(p),Vector2D.FORM_BY_RADIAN));
		if (this.distance(p) < minDistance+0.01)
			cooldownStatus++;
		if (cooldownStatus >= cooldownPeriod) {
			cooldownStatus = 0;
			double radian = angle(p);
			double out = 0.6;
			double size = 0.2;
			double x = getCenterX()+Math.cos(radian)*out-size;
			double y = getCenterY()+Math.sin(radian)*out-size;
			Projectile toThrow = new Projectile(Projectile.Type.RED_FIREBALL,x,y,new Vector2D(3,radian,Vector2D.FORM_BY_RADIAN), this);
			game.entities().add(toThrow);
		}
	}
	
	public Image entityImage() {
		BufferedImage img = Program.getImage("entities/enemies/mage.png");
		return img;
	}
	
	public Entity replicate() {
		Entity e = new Mage(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
