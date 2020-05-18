package entities.enemies;

import java.awt.Image;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.damageBox.DamageBox;
import entities.miscellaneous.Coin;
import entities.miscellaneous.Particle;
import entities.player.Player;
import entities.projectiles.Projectile;
import main.Program;
import misc.MathUtils;
import misc.Vector2D;

//trail of fire that hops around near the player
public class FireDevil extends Enemy {
	public FireDevil(double x, double y) {
		super(x,y,5.0/16,5.0/16);
		this.health = new Health(12);
		EntityDrop[] drops = {
			new EntityDrop(0.5,2, new Coin(0,0,Coin.Type.BRONZE)),
			new EntityDrop(0.15,1, new Coin(0,0,Coin.Type.SILVER)),
			new EntityDrop(0.025,1, new Coin(0,0,Coin.Type.GOLD)),
			//new EntityDrop(0.6,2,new Food(Food.Type.ZOMBIE_MEAT)),
		};
		this.drops = drops;
		this.xpOnDrop = 4;
	}
	
	private double followDistance = MathUtils.randomInRange(8, 9), minDistance = -1, speed = MathUtils.randomInRange(1.525, 1.675);
	public void individualUpdate() {
		this.add(Particle.Type.FIRE,0.5);
		Player p = game.player();
		if (this.distance(p) < followDistance && this.distance(p) > minDistance && age % 5 == 0) {
			double radian = this.angle(p);
			radian+=MathUtils.randomInRange(-Math.PI/5, Math.PI/5);
			double addtRadian = ((double)age%15.0/15.0*Math.PI/2);
			double mult = (double)age%15.0/15.0*speed;
			double xv = Math.cos(radian+addtRadian)*mult;
			double yv = Math.sin(radian+addtRadian)*mult;
			this.setVelocity(new Vector2D(xv,yv,Vector2D.FORM_BY_COMPONENTS));
		}
		
		if (this.colliding(p)) {
			DamageBox db = new DamageBox(this.getPosition(),this.getDimension(),7.5,this);
			db.setVelocity(new Vector2D(0.5,this.angle(p),Vector2D.FORM_BY_RADIAN));
			db = db.destroyOnImpact();
			db = db.lifeSpan(10); //in case it doesn't hit the player we still want it to be destroyed
			game.entities().add(db);
		}
	}
	
	public Image entityImage() {
		BufferedImage img = Program.getImage("entities/enemies/firedevilhead.png");
		return img;
	}
	
	public Entity replicate() {
		Entity e = new FireDevil(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
