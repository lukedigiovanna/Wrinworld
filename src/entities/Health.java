package entities;

import java.io.Serializable;

import misc.MathUtils;

public class Health implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int INVULNERABLE = 999999;
	
	public double health, maxHealth, displayHealth;
	
	public Health(double max) {
		this.health = max;
		this.maxHealth = max;
		this.displayHealth = health;
	}
	
	public void update() {
		roundHealth();
		double dx = this.health-this.displayHealth;
		dx = dx/10;
		this.displayHealth+=dx;
		displayHealth = MathUtils.clip(displayHealth, 0, maxHealth);
	}
	
	public void set(double val) {
		this.health = MathUtils.clip(val, 0, maxHealth);
	}
	
	public boolean dead() {
		if (this.health <= 0)
			return true;
		return false;
	}
	
	public double percent() {
		return MathUtils.round(this.health/this.maxHealth, 0.01);
	}
	
	public void hurt(double amount) {
		this.health-=amount;
		if (this.health <= 0)
			this.health = 0;
	}
	
	public double heal(double amount) {
		if (this.health >= this.maxHealth)
			return 0;
		this.health+=amount;
		double over = 0;
		if (this.health > this.maxHealth) {
			//lets see how much over..
			over = this.health-this.maxHealth;
			this.health = this.maxHealth;
		}
		return amount-over; //returns how much we actually healed
	}
	
	public void roundHealth() {
		health = MathUtils.round(this.health, 0.01);
		displayHealth = MathUtils.round(this.health, 0.01);
	}
	
	public double getDisplayHealth() {
		return displayHealth;
	}
	
	public double displayPercent() {
		return MathUtils.round(this.displayHealth/this.maxHealth, 0.01);
	}
	
	public String toString() {
		String s = MathUtils.round(this.health, 0.1)+"/"+MathUtils.round(this.maxHealth, 0.1)+" ("+MathUtils.round((percent()*100),0.01)+"%)";
		return s;
	}
	
}
