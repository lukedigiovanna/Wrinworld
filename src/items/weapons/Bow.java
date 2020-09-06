package items.weapons;

import java.awt.Image;
import entities.projectiles.Projectile;
import main.Program;
import misc.Vector2D;
import sound.SoundManager;

public class Bow extends Weapon {
	private static final long serialVersionUID = 1L;
	
	public Bow() {
		super("Bow");
		cooldownPeriod = 3;
		loadPeriod = 15;
		this.setInitialDurability(30);
	}

	@Override
	public void use() {
		double radian = getGame().angleToMouse();
		double speed = 5;
		Vector2D velocity = new Vector2D(speed,radian,Vector2D.FORM_BY_RADIAN);
		getGame().entities().add(new Projectile(Projectile.Type.ARROW,this.getOwner().getCenterX(),this.getOwner().getCenterY(),velocity,this.getOwner()));
		
		SoundManager.playSound("arrowshoot");
		this.durability--;
	}
	
	@Override
	public boolean used() {
		return (this.durability <= 0);
	}
	
	private static final Image
		display = Program.getImage("items/weapons/bow.png"),
		icon = Program.getImage("items/weapons/bowicon.png");
	
	@Override
	public Image getDisplayImage() {
		return display;
	}

	@Override
	public Image getIconImage() {
		return icon;
	}
}
