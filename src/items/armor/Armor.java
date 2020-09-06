package items.armor;

import java.awt.Image;

import items.*;
import main.Program;

public class Armor extends Item {
	private static final long serialVersionUID = 1L;

	private Piece piece;
	
	public Armor(Piece piece) {
		super("armor");
		this.piece = piece;
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
		WOODEN_HELMET(Type.HELMET, "woodenhelmet.png", 5);
		
		Type type;
		Image icon;
		int protection;
		
		// protection of an entity is a value from 1 to 100, where 100 is very protected
		Piece(Type type, String icon, int protection) {
			this.type = type;
			this.icon = Program.getImage(icon);
			this.protection = protection;
		}
	}
	
	private static enum Type {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS;
	}
}
