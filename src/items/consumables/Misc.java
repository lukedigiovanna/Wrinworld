package items.consumables;

import java.awt.Image;

import entities.player.Player;
import items.Item;
import main.Program;
import sound.SoundManager;

public class Misc extends Item {
	
	private Type type;
	private boolean consumed = false;
	
	public Misc(Type type) {
		super(type.displayName);
		this.type = type;
		this.maxCount = 16;
		this.cooldownPeriod = 0;
	}
	
	@Override
	public void use() {
		switch (this.type) {
		case MANA_STAR:
			consumed = this.getOwner().addMana(25);
			break;
		}
	}
	
	@Override
	public boolean used() {
		return consumed;
	}

	@Override
	public Image getDisplayImage() {
		return type.display;
	}

	@Override
	public Image getIconImage() {
		return type.icon;
	}
	
	public static enum Type {
		MANA_STAR("Mana Star","manastar.png","manastar.png");
	
		Image display;
		Image icon;
		String displayName;
		
		Type(String name, String display, String icon) {
			this.displayName = name;
			this.display = Program.getImage("items/miscellaneous/"+display);
			this.icon = Program.getImage("items/miscellaneous/"+icon);
		}
	}
}
