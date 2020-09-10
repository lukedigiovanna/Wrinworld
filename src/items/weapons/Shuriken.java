package items.weapons;

import java.awt.Image;

import entities.projectiles.Projectile;
import items.Item;
import main.Program;
import misc.Vector2D;

public class Shuriken extends Weapon {
	private static final long serialVersionUID = 1L;

	public Shuriken() {
		super("Shuriken");
		cooldownPeriod = 0;
		loadPeriod = 4;
		this.maxCount = 16;
	}

	private boolean used = false;
	@Override
	public void use() {
		double radian = getGame().angleToMouse();
		double speed = 5;
		Vector2D velocity = new Vector2D(speed,radian,Vector2D.FORM_BY_RADIAN);
		getGame().entities().add(new Projectile(Projectile.Type.SHURIKEN,this.getOwner().getCenterX(),this.getOwner().getCenterY(),velocity,this.getOwner()));
		used = true;
	}
	
	@Override
	public boolean used() {
		boolean ret = used;
		used = false;
		return ret;
	}
	
	@Override
	public Image getDisplayImage() {
		return Program.getImage("entities/weapons/projectiles/shuriken.png");
	}

	@Override
	public Image getIconImage() {
		return Program.getImage("items/weapons/shurikenicon.png");
	}

	public Item replicate() {
		Item item = new Shuriken();
		this.copyInfo(item);
		return item;
	}
}
