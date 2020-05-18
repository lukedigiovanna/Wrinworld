package entities;

import java.io.Serializable;

import items.Item;

public class EntityDrop implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//holds how many possible and what the chance is
	private int upTo;
	private double chance; //its the chance of upTo
	private Entity entity;
	
	public EntityDrop(double chance, int upTo, Entity entity) {
		this.upTo = upTo;
		this.chance = chance;
		this.entity = entity;
	}
	
	public EntityDrop(double chance, int upTo, Item item) {
		this(chance, upTo, new ItemEntity(item,0,0));
	}
	
	public Entity getEntity() {
		return entity.replicate();
	}
	
	public int getHowMany() {
		int count = 0;
		double itChance = Math.pow(chance, 1.0/upTo);
		for (int i = 0; i < upTo; i++) 
			if (Math.random() < itChance)
				count++;
		return count;
	}
}
