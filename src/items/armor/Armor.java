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
	
	
	public static enum Helmet {
		
	}
	
	public static enum Chestplate {
		
	}
	
	public static enum Leggings {
		
	}
	
	public static enum Boots {
		
	}
}
