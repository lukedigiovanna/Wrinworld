package items.armor;

import java.awt.Image;

import items.*;

public class Armor extends Item {
	public Armor() {
		super("armor");
	}

	@Override
	public void use() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getDisplayImage() {
		return null;
	}

	@Override
	public Image getIconImage() {
		return null;
	}

	@Override
	public boolean used() {
		return false;
	}
	
	
	public static enum Piece {
		WOODEN_HELMET(Type.HELMET);
		
		Type type;
		
		Piece(Type type) {
			this.type = type;
		}
	}
	
	private static enum Type {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS;
	}
}
