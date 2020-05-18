package entities.miscellaneous;

import java.awt.Image;

import entities.*;
import misc.MathUtils;
import misc.Position;

public class Spawner extends Entity {
	private double radius, chance;
	private int maxSpawns = 5, livingSpawns = 0;
	private Entity toClone;
	
	public Spawner(double x, double y, double radius, double chance, Entity toClone) {
		super(x,y,0,0);
		this.radius = radius;
		this.chance = chance;
		this.toClone = toClone;
		setHealth(new Health(Health.INVULNERABLE));
		
		this.setTeam(Entity.Team.MISCELLANEOUS);
	}
	
	public void individualUpdate() {
		if (livingSpawns < maxSpawns && Math.random() < chance) //random tick chance
			summon();
	}
	
	public void summon() {
		if (toClone == null)
			return;
		double x = MathUtils.randomInRange(-radius, radius)+position.x,
				y = MathUtils.randomInRange(-radius, radius)+position.y;
		Entity e = toClone.replicate();
		e.setPosition(new Position(x,y));
		e.tagSpawner(this);
		game.entities().add(e);
		livingSpawns++;
	}

	public void decrementLivingSpawns() {
		livingSpawns--;
	}
	
	@Override
	public Image entityImage() {
		return newImage(); //has no image
	}
	
	public Entity replicate() {
		return new Spawner(position.x,position.y,radius,chance,toClone);
	}
}
