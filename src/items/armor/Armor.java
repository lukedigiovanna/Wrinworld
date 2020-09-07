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
		
	}

	public Type getPieceType() {
		return this.piece.type;
	}

	@Override
	public Image getDisplayImage() {
		return this.piece.icon;
	}

	@Override
	public Image getIconImage() {
		return this.piece.icon;
	}

	@Override
	public boolean used() {
		return false;
	}
	
	
	public static enum Piece {
		WOODEN_HELMET(Type.HELMET, "woodenhelmet.png", 5),
		WOODEN_CHESTPLATE(Type.CHESTPLATE, "woodenchestplate.png", 15),
		WOODEN_LEGGINGS(Type.LEGGINGS, "woodenleggings.png", 10),
		WOODEN_BOOTS(Type.BOOTS, "woodenboots.png", 5);
		
		Type type;
		Image icon;
		int protection;
		
		// protection of an entity is a value from 1 to 100, where 100 is very protected
		Piece(Type type, String icon, int protection) {
			this.type = type;
			this.icon = Program.getImage("items/armor/"+icon);
			this.protection = protection;
		}
	}
	
	public static enum Type {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS;
	}
}
