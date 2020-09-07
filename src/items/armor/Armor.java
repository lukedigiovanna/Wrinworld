package items.armor;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Color;

import display.ImageProcessor;
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

	public Piece getPiece() {
		return this.piece;
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
		WOODEN_BOOTS(Type.BOOTS, "woodenboots.png", 5),
		
		DIAMOND_HELMET(Type.HELMET, Color.CYAN, 15),
		DIAMOND_CHESTPLATE(Type.CHESTPLATE, Color.CYAN, 40),
		DIAMOND_LEGGINGS(Type.LEGGINGS, Color.CYAN, 30),
		DIAMOND_BOOTS(Type.BOOTS, Color.CYAN, 15);

		Type type;
		BufferedImage icon;
		public int protection;
		
		// protection of an entity is a value from 1 to 100, where 100 is very protected
		Piece(Type type, String icon, int protection) {
			this.type = type;
			this.icon = Program.getImage("items/armor/"+icon);
			this.protection = protection;
		}

		Piece(Type type, Color iconColor, int protection) {
			this.type = type;
			String typeString = "";
			switch (type) {
			case HELMET:
				typeString = "helmet";
				break;
			case CHESTPLATE:
				typeString = "chestplate";
				break;
			case LEGGINGS:
				typeString = "leggings";
				break;
			case BOOTS:
				typeString = "boots";
			}
			this.icon = Program.getImage("items/armor/generic"+typeString+".png");
			this.icon = ImageProcessor.scaleToColor(this.icon, iconColor);
			this.protection = protection;
		}
	}
	
	public static enum Type {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS;
	}
}
