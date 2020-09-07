package entities.enemies;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.damageBox.DamageBox;
import entities.miscellaneous.Coin;
import entities.player.Player;
import main.Program;
import misc.*;
import misc.Dimension;

public class Minion extends Enemy {
	private static final long serialVersionUID = 1L;

	public Minion(double x, double y) {
		super(x,y,0.5,0.5);
		this.health = new Health(10);
		EntityDrop[] drops = {
			new EntityDrop(0.6,2, new Coin(0,0,Coin.Type.BRONZE)),
		};
		this.drops = drops;
		this.xpOnDrop = 2;
	}
	
	private double followDistance = MathUtils.randomInRange(8, 9), minDistance = 1, speed = MathUtils.randomInRange(0.6, 0.7);
	private double radian = 0;
	private int cooldownStatus = 0, cooldownPeriod = 100;
	public void individualUpdate() {
		Player p = game.player();
		this.clearVelocity();
		if (this.age % 20 == 0)
			radian = this.angle(p);
		if (this.distance(p) < followDistance && this.distance(p) > minDistance)
			this.setVelocity(new Vector2D(speed,radian,Vector2D.FORM_BY_RADIAN));
		if (this.distance(p) < minDistance+0.1) 
			cooldownStatus++;
		if (cooldownStatus >= cooldownPeriod) {
			cooldownStatus = 0;
			//summon a damage box towards the player
			double out = 0.6;
			double size = 0.2;
			double x = getCenterX()+Math.cos(radian)*out-size;
			double y = getCenterY()+Math.sin(radian)*out-size;
			DamageBox box = (new DamageBox(new Position(x,y),new Dimension(size,size),2,this)).destroyOnImpact();
			box.setVelocity(new Vector2D(0.5,radian,Vector2D.FORM_BY_RADIAN));
			game.entities().add(box);
		}
	}
	
	public Image entityImage() {
		BufferedImage img = Program.getImage("entities/enemies/minion.png");
		return img;
	}
	
	public Entity replicate() {
		Entity e = new Minion(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
