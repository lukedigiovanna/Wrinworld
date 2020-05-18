package items.consumables;

import java.awt.*;
import java.awt.image.BufferedImage;

import display.ImageProcessor;
import entities.StatusEffect;
import items.Item;
import main.Program;

public class Potion extends Item {
	private Type type;
	
	public Potion(Type type) {
		super(type.name);
		this.type = type;
	}
	
	private boolean used = false;
	public void use() {
		switch (type) {
		case REGENERATION:
			this.owner.addStatusEffect(StatusEffect.Effect.REGENERATION, 100);
			break;
		case HEAL:
			this.owner.addStatusEffect(StatusEffect.Effect.HEAL, 10);
			break;
		case QUICK_FEET:
			this.owner.addStatusEffect(StatusEffect.Effect.SPEED, 300);
			break;
		}
		used = true;
	}

	public Image getDisplayImage() {
		return type.image;
	}

	public Image getIconImage() {
		return type.image;
	}

	public boolean used() {
		return used;
	}
	
	public static enum Type {
		REGENERATION("Regeneration",Color.PINK), //applies 2.5/sec regen for 10 seconds
		HEAL("Heal",Color.RED), //heals the player 20 points
		QUICK_FEET("Quick Feet",Color.BLUE.brighter()), //player speed temporarily boosted
		POSEIDONS_TOUCH("Poseidon\'s Touch",Color.YELLOW); //coin drops greatly increase
		
		String name;
		BufferedImage image;
		
		Type(String name, Color col) {
			this.name = name+" Potion";
			image = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();
			g.drawImage(Program.getImage("items/miscellaneous/potionicon.png"), 0, 0, 8, 8, null);
			image = ImageProcessor.scaleToColor(image, col);
		}
	}
}
