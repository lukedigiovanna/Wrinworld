package entities.damageBox;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.miscellaneous.Particle;
import entities.projectiles.Projectile;
import misc.Dimension;
import misc.MathUtils;
import misc.Position;
import misc.Vector2D;

public class DamageBox extends Entity {
	
	private boolean destroyOnImpact = false; //default
	private double power = 1; //default
	private Entity owner;
	private int lifeSpan = 20*999999;
	
	//has dimension and position
	public DamageBox(Position p, Dimension d, double power, Entity owner) {
		super(p,d);
		this.power = power;
		this.owner = owner;
		this.showHealthBar = false;
	}
	
	public DamageBox destroyOnImpact() {
		destroyOnImpact = true;
		return this;
	}
	
	public DamageBox lifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
		return this;
	}

	@Override
	public void individualUpdate() {
		if (this.age >= this.lifeSpan)
			this.destroy();
		if (this.owner != null)
			this.setRotation(owner.getRotation());
		//loop through all entities
		for (int i = 0; i < game.entities().get().size(); i++) {
			Entity e = game.entities().get(i);
			if (e == null || e == this || e == owner || e instanceof DamageBox || e instanceof Projectile || this.isSameTeam(e) || e.getTeam() == Entity.Team.MISCELLANEOUS || e.getTeam() == Entity.Team.NPC || e.getTeam() == Entity.Team.ITEM)
				continue; // no need to check itself or the owner
			if (this.colliding(e)) {
				e.hurt(power);
				e.setKiller(owner);
				e.knockBack(velocity.getMagnitude(), velocity.getRadian());
				for (int k = 0; k < MathUtils.randomInRange(2, 4); k++) {
					Particle p = new Particle(Particle.Type.BLOOD,this.getX()+MathUtils.randomInRange(-0.1, 0.1),this.getY()+MathUtils.randomInRange(-0.1, 0.1),1.5);
					p.setVelocity(new Vector2D(0,Math.cos(velocity.radian),Vector2D.FORM_BY_COMPONENTS));
					game.entities().add(p);
				}
				if (destroyOnImpact) {
					this.destroy();
					destroyed = true;
				}
			}
		}
	}
	
	private boolean destroyed = false;
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public Image entityImage() {
		//returns nothing (will return nothing.. red box is for testing)
		BufferedImage img = newImage();
		Graphics g = img.getGraphics();
		g.setColor(new Color(255,0,0,125));
		//g.fillRect(0, 0, img.getWidth(), img.getHeight());
		return img;
	}
	
	public Entity replicate() {
		DamageBox d = new DamageBox(position,dimension,power,owner);
		d.destroyOnImpact = destroyOnImpact;
		return d;
	}
}
