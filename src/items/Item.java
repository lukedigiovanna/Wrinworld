package items;

import java.awt.Image;
import java.io.Serializable;

import entities.Entity;
import game.Game;
import misc.MathUtils;

//how this needs to work:
/*
 * as you hold the cooldown goes up.. then check when the mouse is released if the cooldown is atleast the cooldown period
 * then use the item. think of pulling a bow back
 */

//includes all items including: food, armor, weapons (swords, bows, etc.), potions
public abstract class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//instance vars for all itmes
	protected String name, description;
	protected int durability = 0, maxDurability = 0;
	protected Entity owner;
	protected int maxCount = 1; //default
	protected int cooldownPeriod = 1, cooldownStatus = 1;
	protected int loadPeriod = 0, loadStatus = 0;
	
	//holds the image for the use and the icon for when displayed in inventory or a chest
	public Item(String name) {
		this.name = name;
	}
	
	public boolean tryUse() {
		if (this.loadStatus >= this.loadPeriod && this.cooldownStatus >= this.cooldownPeriod) { 
			use();
			this.loadStatus = 0;
			this.cooldownStatus = 0;
			return true;
		}
		return false;
	}
	
	public void update() {
		this.cooldownStatus++;
	}
	
	public void load() {
		if (this.cooldownStatus >= this.cooldownPeriod)
		this.loadStatus++;
		if (this.loadStatus > this.loadPeriod)
			this.loadStatus = this.loadPeriod;
	}
	
	private boolean allowHold = false;
	
	public void allowHold() {
		allowHold = true;
	}
	
	public boolean allowsHold() {
		return allowHold;
	}
	
	public void resetLoad() {
		this.loadStatus = 0;
	}
	
	public abstract void use();
	
	public abstract boolean used(); //condition when the item should be removed from the inventory
	
	public abstract Image getDisplayImage();
	
	public abstract Image getIconImage();
	
	public abstract Item replicate();

	protected void copyInfo(Item item) {
		item.setOwner(this.owner);
	}

	public void setOwner(Entity owner) {
		this.owner = owner;
	}
	
	public Entity getOwner() {
		return owner;
	}
	
	public String getName() {
		return name;
	}
	
	public int maxCount() {
		return maxCount;
	}
	
	public double cooldownStatusPercent() {
		if (this.cooldownPeriod == 0)
			return 0;
		return MathUtils.max((double)this.cooldownStatus/this.cooldownPeriod,1.0);
	}
	
	public double loadStatusPercent() {
		if (this.loadPeriod == 0)
			return 0;
		return MathUtils.max((double)this.loadStatus/this.loadPeriod,1.0);
	}
	
	public int getLoadStatus() {
		return this.loadStatus;
	}
	
	public boolean equals(Item item) {
		if (item == null)
			return false;
		return this.getName().equals(item.getName());
	}
	
	public Game getGame() {
		if (owner == null) return null;
		return owner.getGame();
	}
	
	public int getCooldownPeriod() {
		return this.cooldownPeriod;
	}
	
	public int getDurability() {
		return durability;
	}
	
	public int getMaxDurability() {
		return this.maxDurability;
	}
	
	public void setInitialDurability(int num) {
		this.durability = num;
		this.maxDurability = num;
	}
	
	//returns a string like: zombie_meat..
	//RULES: all lowercase, use underscores as spaces.
	public String getID() {
		//parse the name
		String[] words = this.name.split(" ");
		String s = words[0].toLowerCase();
		for (int i = 1; i < words.length; i++) {
			s+="_"+words[i].toLowerCase();
		}
		return s;
	}	
}
