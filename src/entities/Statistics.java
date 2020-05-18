package entities;

import java.io.Serializable;

import misc.MathUtils;

public class Statistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int entitiesKilled,
		monstersKilled,
		animalsKilled,
		lifetimeEarnings,
		deaths;
	
	private double distanceTraveled,
		damageDealt,
		damageTaken,
		healthHealed;
	
	public int getDeaths() {
		return this.deaths;
	}
	
	public void addDeath() {
		this.deaths++;
	}
	
	public double getHealthHealed() {
		return healthHealed;
	}
	
	public void addHealthHealed(double amount) {
		this.healthHealed+=amount;
	}
	
	public int getLifetimeEarnings() {
		return lifetimeEarnings;
	}
	
	public void addEarnings(int amount) {
		this.lifetimeEarnings+=amount;
	}
	
	public double getDistanceTraveled() {
		return MathUtils.round(distanceTraveled,0.01);
	}
	
	public void addDistanceTraveled(double dist) {
		this.distanceTraveled+=dist;
	}
	
	public double getDamageDealt() {
		return this.damageDealt;
	}
	
	public void addDamageDealt(double amount) {
		this.damageDealt+=amount;
	}
	
	public double getDamageTaken() {
		return this.damageTaken;
	}
	
	public void addDamageTaken(double amount) {
		this.damageTaken+=amount;
	}
	
	public int getEntitiesKilled() {
		return entitiesKilled;
	}
	
	public int getMonstersKilled() {
		return monstersKilled;
	}
	
	public int getAnimalsKilled() {
		return animalsKilled;
	}
	
	public void addEntityKill() {
		entitiesKilled++;
	}
	
	public void addMonsterKill() {
		monstersKilled++;
		addEntityKill();
	}
	
	public void addAnimalKill() {
		animalsKilled++;
		addEntityKill();
	}
	
	public String toString() {
		//go through each statistic..
		String s = "";
		String[] stats = {
			"Entities Killed: "+this.getEntitiesKilled(),
		    "Animals Killed: "+this.getAnimalsKilled(),
		    "Monsters Killed: "+this.getMonstersKilled(),
		    "Distance Traveled: "+this.getDistanceTraveled(),
		    "Damage Dealt: "+this.getDamageDealt(),
		    "Damage Taken: "+this.getDamageTaken(),
		    "Health Healed: "+this.getHealthHealed(),
		    "Lifetime Earnings: "+this.getLifetimeEarnings(),
		    "Deaths: "+this.getDeaths(),
		};
		for (int i = 0; i < stats.length; i++) {
			s+=stats[i];
			if (i < stats.length-1)
				s+="\n";
		}
		return s;
	}
}
