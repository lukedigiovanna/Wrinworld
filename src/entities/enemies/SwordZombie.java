package entities.enemies;

import java.awt.*;

import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.damageBox.DamageBox;
import entities.miscellaneous.Coin;
import entities.player.Player;
import items.consumables.Food;
import items.weapons.BroadSword;
import main.Program;
import misc.*;
import misc.Dimension;
import sound.SoundManager;

public class SwordZombie extends Enemy {
	public SwordZombie(double x, double y) {
		super(x,y,0.5,1.0);
		this.health = new Health(35);
		EntityDrop[] drops = {new EntityDrop(0.6,2,new Food(Food.Type.ZOMBIE_MEAT)), new EntityDrop(0.1,1,new BroadSword(BroadSword.Type.STONE)),
				new EntityDrop(0.3,3, new Coin(0,0,Coin.Type.BRONZE)),
				new EntityDrop(0.1,2, new Coin(0,0,Coin.Type.SILVER)),
				new EntityDrop(0.05,1, new Coin(0,0,Coin.Type.GOLD)),
				new EntityDrop(0.01,1, new Coin(0,0,Coin.Type.JEWEL))};
		this.drops = drops;
		xpOnDrop = 2;
	}
	
	private double followDistance = MathUtils.randomInRange(5, 7), minDistance = 0.6, speed = MathUtils.randomInRange(0.15, 0.2);
	private double cooldownPeriod = 5, cooldownStatus = 0;
	private String[] sounds = {"zombiegroan1","zombiegroan2"};
	public void individualUpdate() {
		//play a sound
		if (Math.random() < 0.0025) 
			if (this.distance(game.player()) < 8)
				SoundManager.playSound(sounds[(int)(Math.random()*sounds.length)]);
		Player p = game.player();
		this.clearVelocity();
		if (this.distance(p) < followDistance && this.distance(p) > minDistance)
			this.setVelocity(new Vector2D(speed,this.angle(p),Vector2D.FORM_BY_RADIAN));
		if (this.distance(p) < minDistance+0.01)
			cooldownStatus++;
		if (cooldownStatus >= cooldownPeriod) {
			cooldownStatus = 0;
			//we need to summon a damage box in front of the zombie in the direction near the player
			double radian = angle(p);
			double out = 0.6;
			double size = 0.2;
			double x = getCenterX()+Math.cos(radian)*out-size;
			double y = getCenterY()+Math.sin(radian)*out-size;
			DamageBox box = (new DamageBox(new Position(x,y),new Dimension(size,size),8,this)).destroyOnImpact();
			box.setVelocity(new Vector2D(0.4,radian,Vector2D.FORM_BY_RADIAN));
			game.entities().add(box);
		}
	}
	
	public Image entityImage() {
		return Program.getImage("entities/enemies/swordzombie.png");
	}
	
	public Entity replicate() {
		Entity e = new SwordZombie(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
