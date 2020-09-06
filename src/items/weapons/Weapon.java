package items.weapons;

import items.Item;

public abstract class Weapon extends Item {
	private static final long serialVersionUID = 1L;

	public Weapon(String name) {
		super(name);
		maxCount = 1;
	}
}
