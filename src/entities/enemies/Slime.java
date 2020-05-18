package entities.enemies;

import java.awt.Image;

import animation.Animation;
import entities.Entity;
import entities.EntityDrop;
import entities.Health;
import entities.damageBox.DamageBox;
import entities.miscellaneous.Coin;
import entities.player.Player;
import items.misc.Miscellaneous;
import main.Program;
import misc.Dimension;
import misc.MathUtils;
import misc.Position;
import misc.Vector2D;
import sound.SoundManager;

public class Slime extends Enemy {
	private Animation ani;
	
	public Slime(double x, double y) {
		super(x,y,0.5,0.5);
		this.health = new Health(15);
		EntityDrop[] drops = {new EntityDrop(0.6,2,new Miscellaneous(Miscellaneous.Type.SLIME)),
				new EntityDrop(0.4,3, new Coin(0,0,Coin.Type.BRONZE)),
				};
		this.drops = drops;
		this.xpOnDrop = 1;
		ani = new Animation("entities/enemies/slime", "slime_", 8);
	}
	
	private double followDistance = MathUtils.randomInRange(5.0, 7), minDistance = 0.6, speed = MathUtils.randomInRange(0.3, 0.45);
	private double cooldownPeriod = 5, cooldownStatus = 0;
	private double timer = 0, period = 60;
	public void individualUpdate() {
		Player p = game.player();
		//change.
		timer++;
		if (this.distance(p) < followDistance && this.distance(p) > minDistance) {
			this.setVelocity(new Vector2D(speed*((period-timer)/period),this.angle(p),Vector2D.FORM_BY_RADIAN));
		}
		timer%=period;
		if (this.distance(p) < minDistance+0.01)
			cooldownStatus++;
		//change.
		if (cooldownStatus >= cooldownPeriod) {
			cooldownStatus = 0;
			//we need to summon a damage box in front of the zombie in the direction near the player
			double radian = angle(p);
			double out = 0.6;
			double size = 0.2;
			double x = getCenterX()+Math.cos(radian)*out-size;
			double y = getCenterY()+Math.sin(radian)*out-size;
			DamageBox box = (new DamageBox(new Position(x,y),new Dimension(size,size),4,this)).destroyOnImpact();
			box.setVelocity(new Vector2D(0.8,radian,Vector2D.FORM_BY_RADIAN));
			game.entities().add(box);
		}
	}
	
	public Image entityImage() {
		ani.update();
		return ani.getCurrentFrame();
	}
	
	public Entity replicate() {
		Entity e = new Slime(position.x,position.y);
		e.setHealth(new Health(getHealth().maxHealth));
		return e;
	}
}
