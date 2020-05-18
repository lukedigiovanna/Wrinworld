package items.misc;

import java.awt.Image;

import game.GameController;
import items.Item;
import main.Program;

public class Miscellaneous extends Item {
	
	private Type type;
	
	public Miscellaneous(Type type) {
		super(type.displayName);
		this.type = type;
		this.maxCount = 16;
	}
	
	@Override
	public void use() {
		switch (type) {
		case BONE:
			GameController.game().console().log("Used bone");
			break;
		case WOOL:
			GameController.game().console().log("Used wool");
		}
		used = true;
	}
	
	private boolean used = false;
	@Override
	public boolean used() {
		return used;
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
		BONE("Bone","boneicon.png","boneicon.png"),
		WOOL("Wool","woolicon.png","woolicon.png"),
		SLIME("Slime","slimeicon.png","slimeicon.png");
		
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
