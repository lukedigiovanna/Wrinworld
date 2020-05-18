package entities.enemies;

import java.awt.*;
import java.awt.image.BufferedImage;

import display.ImageProcessor;
import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.miscellaneous.Coin;
import entities.miscellaneous.Particle;
import entities.player.Player;
import entities.projectiles.Projectile;
import main.Program;
import misc.*;

public class WildBoar extends Enemy {
	public WildBoar(double x, double y) {
		super(x,y,12/16.0,7/16.0);
		this.health = new Health(25);
		EntityDrop[] drops = {
			//new EntityDrop(0.6,2,new Food(Food.Type.ZOMBIE_MEAT)),
			new EntityDrop(0.3,3, new Coin(0,0,Coin.Type.BRONZE)),
			new EntityDrop(0.05,2, new Coin(0,0,Coin.Type.SILVER))
		};
		this.drops = drops;
		this.xpOnDrop = 4;
	}
	
	private double followDistance = MathUtils.randomInRange(10, 12), minDistance = 3, speed = MathUtils.randomInRange(0.525, 0.775);
	public void individualUpdate() {
		Player p = game.player();
		double dist = this.distance(p);
		double rad = this.angle(p);
		if (dist < followDistance && dist > minDistance && !rage)
			this.setVelocity(new Vector2D(speed,rad,Vector2D.FORM_BY_RADIAN));
		if (dist < minDistance+0.01)
			rageStatus++;
		if (rageStatus >= ragePeriod) {
			chargeRadian = rad;
			rageStatus = 0;
			if (rageStatus < rageDuration-10)
				this.setVelocity(new Vector2D(speed*4,chargeRadian,Vector2D.FORM_BY_RADIAN));
			rage = true;
		}
		if (rage) {
			rageCountdown--;
		}
		if (rageCountdown <= 0) {
			rage = false;
			rageCountdown = rageDuration;
		}
		
		if (this.colliding(p) && rage) {
			//shoot the player far.. heavy knock
			//and deal heavy damage
			p.hurt(12);
			p.knockBack(2.5,rad);
		}
	}
	
	private int rageStatus = 0, ragePeriod = 60, rageDuration = 40;
	private int rageCountdown = rageDuration;
	private boolean rage = false;
	private double chargeRadian;
	
	public Image entityImage() {
		BufferedImage img = Program.getImage("entities/enemies/wildboar.png");
		if (rage)
			img = ImageProcessor.scaleToColor(img, new Color(255,0,0));
		return img;
	}
	
	public Entity replicate() {
		Entity e = new WildBoar(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
