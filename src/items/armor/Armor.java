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
		super(piece.name);
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

	public Item replicate() {
		Item item = new Armor(this.piece);
		this.copyInfo(item);
		return item;
	}

	public static enum Piece {
		// wooden set
		WOODEN_HELMET("Wooden Helmet", Type.HELMET, "woodenhelmet.png", 5),
		WOODEN_CHESTPLATE("Wooden Chestplate", Type.CHESTPLATE, "woodenchestplate.png", 15),
		WOODEN_LEGGINGS("Wooden Leggings", Type.LEGGINGS, "woodenleggings.png", 10), 
		WOODEN_BOOTS("Wooden Boots", Type.BOOTS, "woodenboots.png", 5),
		
		// iron set
		IRON_HELMET("Iron Helmet", Type.HELMET, Color.LIGHT_GRAY, 10),
		IRON_CHESTPLATE("Iron Chestplate", Type.CHESTPLATE, Color.LIGHT_GRAY, 30),
		IRON_LEGGINGS("Iron Leggings", Type.LEGGINGS, Color.LIGHT_GRAY, 20),
		IRON_BOOTS("Iron Boots", Type.BOOTS, Color.LIGHT_GRAY, 10),

		// crystal set
		CRYSTAL_HELMET("Crystal Helmet", Type.HELMET, Color.MAGENTA, 15),
		CRYSTAL_CHESTPLATE("Crystal Chestplate", Type.CHESTPLATE, Color.MAGENTA, 40),
		CRYSTAL_LEGGINGS("Crystal Leggings", Type.LEGGINGS, Color.MAGENTA, 30),
		CRYSTAL_BOOTS("Crystal Boots", Type.BOOTS, Color.MAGENTA, 15),
		
		// specials
		TRAVELERS_BOOTS("Traveler\'s Boots", Type.BOOTS, Color.BLUE, 8), // increase the player's speed
		CAP_OF_VISION("Cap of Vision", Type.HELMET, Color.YELLOW, 4); // increases the strength of the player's light source

		String name;
		Type type;
		BufferedImage icon;
		public int protection;
		
		// protection of an entity is a value from 1 to 100, where 100 is very protected
		Piece(String name, Type type, String icon, int protection) {
			this.type = type;
			this.icon = Program.getImage("items/armor/"+icon);
			this.protection = protection;
			this.name = name;
		}

		Piece(String name, Type type, Color iconColor, int protection) {
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
			this.name = name;
		}
	}
	
	public static enum Type {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS;
	}
}
