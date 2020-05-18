package entities.animals;

import entities.Entity;

public abstract class Animal extends Entity {
	public Animal(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.setTeam(Entity.Team.ANIMAL);
		xpOnDrop = 1;
	}
}
