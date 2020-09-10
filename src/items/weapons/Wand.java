package items.weapons;

import java.awt.Image;

import main.Program;
import misc.Vector2D;
import entities.projectiles.*;
import game.GameController;
import items.Item;
import misc.MathUtils;

public class Wand extends Weapon {
	private static final long serialVersionUID = 1L;

	private Type type;
	
	public Wand(Type type) {
		super(type.name);
		this.type = type;
		this.cooldownPeriod = type.cooldown;
		this.loadPeriod = type.load;
		this.allowHold();
	}
	
	public enum Type {
		ORANGE_WAND("Weak Orange Wand","orangewand.png","orangewandicon.png", 20, 20,15),
		BLUE_WAND("Blue Fireball Wand","bluewand.png","bluewandicon.png",20,20,20),
		RED_WAND("Red Fireball Wand","redwand.png","redwandicon.png",20,20,25),
		GREEN_WAND("Strong Green Wand","greenwand.png","greenwandicon.png",20,20,30),
		FIRE_WAND("Fire Wand","firewand.png","firewandicon.png", 0, 0,1);
		String name;
		Image display, icon;
		int cooldown, load, mana;
		
		Type(String name, String display, String icon, int cooldown, int load, int mana) {
			this.name = name;
			this.display = Program.getImage("items/weapons/wands/"+display);
			this.icon = Program.getImage("items/weapons/wands/"+icon);
			this.cooldown = cooldown;
			this.load = load;
			this.mana = mana;
		}
	}

	@Override
	public void use() {
		if (!this.getOwner().useMana(type.mana))
			return; //if you couldn't use the mana then don't use the weapon.
		switch (type) {
		case ORANGE_WAND:
			this.getGame().entities().add(new Projectile(Projectile.Type.ORANGE_FIREBALL, this.getOwner().getCenterX(), this.getOwner().getCenterY(), new Vector2D(5,GameController.game().angleToMouse(),Vector2D.FORM_BY_RADIAN), this.getOwner()));
			break;
		case BLUE_WAND:
			this.getGame().entities().add(new Projectile(Projectile.Type.BLUE_FIREBALL, this.getOwner().getCenterX(), this.getOwner().getCenterY(), new Vector2D(5,GameController.game().angleToMouse(),Vector2D.FORM_BY_RADIAN), this.getOwner()));
			break;
		case RED_WAND:
			this.getGame().entities().add(new Projectile(Projectile.Type.RED_FIREBALL, this.getOwner().getCenterX(), this.getOwner().getCenterY(), new Vector2D(5,GameController.game().angleToMouse(),Vector2D.FORM_BY_RADIAN), this.getOwner()));
			break;
		case GREEN_WAND:
			this.getGame().entities().add(new Projectile(Projectile.Type.GREEN_FIREBALL, this.getOwner().getCenterX(), this.getOwner().getCenterY(), new Vector2D(5,GameController.game().angleToMouse(),Vector2D.FORM_BY_RADIAN), this.getOwner()));
			break;
		case FIRE_WAND:
			this.getGame().entities().add(new Projectile(Projectile.Type.FIRE, getOwner().getCenterX(), getOwner().getCenterY(), new Vector2D(3,GameController.game().angleToMouse()+MathUtils.randomInRange(-Math.PI/8,Math.PI/8),Vector2D.FORM_BY_RADIAN), GameController.game().player()));
			break;
		}
		
		//mana usage too.
	}

	@Override
	public Image getDisplayImage() {
		return type.display;
	}

	@Override
	public Image getIconImage() {
		return type.icon;
	}

	@Override
	public boolean used() {
		return false;
	}

	public Item replicate() {
		Item item = new Wand(this.type);
		this.copyInfo(item);
		return item;
	}
}
