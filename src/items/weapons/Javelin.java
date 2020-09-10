package items.weapons;

import java.awt.Image;

import entities.projectiles.Projectile;
import items.Item;
import main.Program;
import misc.Vector2D;

public class Javelin extends Weapon {
	private static final long serialVersionUID = 1L;

	public Javelin() {
		super("Javelin");
		cooldownPeriod = 1;
		loadPeriod = 15;
	}

	private boolean used = false;
	@Override
	public void use() {
		double radian = getGame().angleToMouse();
		double speed = 5;
		Vector2D velocity = new Vector2D(speed,radian,Vector2D.FORM_BY_RADIAN);
		getGame().entities().add(new Projectile(Projectile.Type.JAVELIN,this.getOwner().getCenterX(),this.getOwner().getCenterY(),velocity,this.getOwner()));
		used = true;
	}
	
	@Override
	public boolean used() {
		return used;
	}
	
	@Override
	public Image getDisplayImage() {
		return Program.getImage("items/weapons/javelinicon.png");
	}

	@Override
	public Image getIconImage() {
		return Program.getImage("items/weapons/javelinicon.png");
	}

	public Item replicate() {
		Item item = new Javelin();
		this.copyInfo(item);
		return item;
	}
}
