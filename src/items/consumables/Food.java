package items.consumables;

import java.awt.Image;

import entities.player.Player;
import items.Item;
import main.Program;
import sound.SoundManager;

public class Food extends Item {
	
	private Type type;
	private boolean consumed = false;
	
	public Food(Type type) {
		super(type.displayName);
		this.type = type;
		this.maxCount = 16;
		this.cooldownPeriod = 0;
	}
	
	@Override
	public void use() {
		consumed = this.getOwner().heal(type.health);
		if (consumed && this.getOwner() instanceof Player)
			SoundManager.playSound("burp");
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
		RAW_PORK("Raw Pork","rawpork.png","rawporkicon.png",3.5),
		COOKED_PORK("Cooked Pork","cookedporkicon.png","cookedporkicon.png",7),
		RAW_BEEF("Raw Beef","rawbeeficon.png","rawbeeficon.png",4),
		COOKED_BEEF("Cooked Beef","cookedbeeficon.png","cookedbeeficon.png",8),
		RAW_MUTTON("Raw Mutton","rawmuttonicon.png","rawmuttonicon.png",4),
		COOKED_MUTTON("Cooked Mutton","cookedmuttonicon.png","cookedmuttonicon.png",8),
		ZOMBIE_MEAT("Zombie Meat","zombiemeaticon.png","zombiemeaticon.png",1),
		BERRIES("Berries","berriesicon.png","berriesicon.png",2);
		
		Image display;
		Image icon;
		String displayName;
		
		double health;
		
		Type(String name, String display, String icon, double health) {
			this.displayName = name;
			this.display = Program.getImage("items/food/"+display);
			this.icon = Program.getImage("items/food/"+icon);
			this.health = health;
		}
	}
}
