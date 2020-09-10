package items.weapons;

import java.awt.Image;

import entities.damageBox.DamageBox;
import items.Item;
import main.Program;
import misc.Dimension;
import misc.Position;
import misc.Vector2D;

public class BroadSword extends Weapon {
	private static final long serialVersionUID = 1L;

	private transient Image displayImage, iconImage;
	private double damage;
	private Type type;
	
	public BroadSword(Type type) {
		super(type.name);
		this.type = type;
		this.displayImage = type.displayImage;
		this.iconImage = type.iconImage;
		this.damage = type.damage;
		this.cooldownPeriod = 15;
	}
	
	public enum Type {
		WOODEN("Wooden","woodensword.png","woodenswordicon.png",3),
		STONE("Stone","stonesword.png","stoneswordicon.png",6),
		IRON("Iron","ironsword.png","ironswordicon.png",12),
		DIAMOND("Diamond","diamondsword.png","diamondswordicon.png",24);
		
		String name;
		Image displayImage;
		Image iconImage;
		double damage;
		
		Type(String name, String display, String icon, double damage) {
			this.name = name+" Sword";
			this.damage = damage;
			this.displayImage = Program.getImage("items/weapons/swords/"+display);
			this.iconImage = Program.getImage("items/weapons/swords/"+icon);
		}
	}

	@Override
	public Image getDisplayImage() {
		return displayImage;
	}

	@Override
	public Image getIconImage() {
		return iconImage;
	}

	@Override
	public boolean used() {
		return false;
	}

	public Item replicate() {
		Item item = new BroadSword(this.type);
		this.copyInfo(item);
		return item;
	}

	@Override
	public void use() {
		//put a lil damage box in direction of mouse
		double radian = this.getGame().angleToMouse();
		double width = 0.35, height = 0.35;
		double dx = Math.cos(radian)*0.3;
		if (dx < 0)
			dx-=width;
		double dy = Math.sin(radian)*0.3;
		if (dy < 0)
			dy-=height;
		double x = getOwner().getCenterX()+dx,
				y = getOwner().getCenterY()+dy;
		DamageBox box = new DamageBox(new Position(x,y), new Dimension(width,height), damage, getOwner());
		box = box.destroyOnImpact();
		box = box.lifeSpan(6);
		box.setVelocity(new Vector2D(0.4,radian,Vector2D.FORM_BY_RADIAN));
		this.getGame().entities().add(box);
	}
}
