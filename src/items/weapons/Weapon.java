package items.weapons;

import items.Item;

public abstract class Weapon extends Item {
	public Weapon(String name) {
		super(name);
		maxCount = 1;
	}
}
