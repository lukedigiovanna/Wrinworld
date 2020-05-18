package entities.enemies;

import java.awt.Image;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.player.Player;
import entities.projectiles.Projectile;
import entities.miscellaneous.*;
import items.misc.Miscellaneous;
import items.weapons.Bow;
import main.Program;
import misc.MathUtils;
import misc.Position;
import misc.Vector2D;
import sound.SoundManager;

public class Skeleton extends Enemy {

	public Skeleton(double x, double y) {
		super(x, y, 0.5, 1.0);
		setHealth(new Health(20));
		EntityDrop[] drops = {new EntityDrop(0.4,2,new Miscellaneous(Miscellaneous.Type.BONE)),
							new EntityDrop(0.1,1,new Bow()),
							new EntityDrop(0.6,2, new Coin(0,0,Coin.Type.BRONZE)),
							new EntityDrop(0.1,1, new Coin(0,0,Coin.Type.SILVER)),
							new EntityDrop(0.05,1, new Coin(0,0,Coin.Type.GOLD)),
							new EntityDrop(0.005,1, new Coin(0,0,Coin.Type.JEWEL)),
							};
		this.drops = drops;
		xpOnDrop = 2;
	}

	private double followDistance = 9, minDistance = MathUtils.randomInRange(4, 5), speed = MathUtils.randomInRange(0.5, 0.9);
	int cooldownPeriod = MathUtils.randomInRange(50, 70), cooldownStatus = 0;
	public void individualUpdate() {
		Player p = game.player();
		this.clearVelocity();
		if (this.distance(p) < followDistance && this.distance(p) > minDistance)
			this.setVelocity(new Vector2D(speed,this.angle(p),Vector2D.FORM_BY_RADIAN));
		if (this.distance(p) < minDistance+0.1)
			cooldownStatus++;
		if (cooldownStatus >= cooldownPeriod) {
			//summon an arrow
			Position pp = p.getPositionAtFrame(5);
			Vector2D velocity = new Vector2D(8,MathUtils.angle(getPosition(),pp)+MathUtils.randomInRange(-Math.PI/8, Math.PI/8),Vector2D.FORM_BY_RADIAN);
			game.entities().add(new Projectile(Projectile.Type.ARROW,this.getCenterX(),this.getCenterY(),velocity,this));
			SoundManager.playSound("arrowshoot");
			cooldownStatus = 0;
		}
		
		if (Math.random() < 0.005)
			if (this.distance(game.player()) < 8)
				SoundManager.playSound("skeletonbones");
	}

	@Override
	public Image entityImage() {
		return Program.getImage("entities/enemies/skeleton.png");
	}
	
	public Entity replicate() {
		Entity e = new Skeleton(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
