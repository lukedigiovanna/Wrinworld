package entities.projectiles;

import java.awt.Image;
import java.awt.image.BufferedImage;

import animation.Animation;
import entities.Entity;
import entities.Health;
import entities.ItemEntity;
import entities.damageBox.DamageBox;
import items.Item;
import items.weapons.Javelin;
import items.weapons.Shuriken;
import main.Program;
import misc.Dimension;
import misc.Vector2D;

//includes stuff like arrows, bullets, potions, etc.
public class Projectile extends Entity {
	private static final long serialVersionUID = 1L;

	private DamageBox damageBox;
	private int lifeSpan = 1000;
	private Type type;
	
	public Projectile(Type type, double x, double y, Vector2D velocity, Entity owner) {
		super(x,y,type.dimension.width,type.dimension.height);
		this.setHealth(new Health(Health.INVULNERABLE));
		this.type = type;
		this.setVelocity(velocity); 
		//add the damage box
		damageBox = (new DamageBox(getPosition(),getDimension(),type.damage,this)).destroyOnImpact();
		game.entities().add(damageBox);
		if (owner != null) {
			this.team = owner.getTeam();
			damageBox.setTeam(owner.getTeam());
		} else {
			this.team = Entity.Team.MISCELLANEOUS;
			damageBox.setTeam(Entity.Team.MISCELLANEOUS);
		}
		this.showHealthBar = false;
	}
	
	public enum Type {
		SKELETON_ARROW("arrow.png", new Dimension(0.5,0.25),2.5,4), // the arrow that a skeleton will shoot
		ARROW("arrow.png", new Dimension(0.5,0.25),8.0,4), // the arrow that the player will shoot
		ORANGE_FIREBALL("fireballs/orangeball.png", new Dimension(0.25,0.25),10.0,5), //d = 10
		BLUE_FIREBALL("fireballs/blueball.png", new Dimension(0.25,0.25), 20.0,5), //d = 20
		RED_FIREBALL("fireballs/redball.png", new Dimension(0.25,0.25), 40.0,5), //d = 40
		GREEN_FIREBALL("fireballs/greenball.png", new Dimension(0.25,0.25), 80.0,5), //d = 80
		ROCK("rock.png",new Dimension(0.375,0.1875),15.0,5),
		FIRE("fire","fireprojectile",5,new Dimension(0.375,0.375),2.0,1.5),
		JAVELIN("javelin.png", new Dimension(1.0,0.25),30,1.5),
		SHURIKEN("shuriken.png", new Dimension(0.25,0.25),5,1.5);
		
		Dimension dimension;
		Image image;
		Animation ani;
		double damage;
		int lifeSpan;
		
		Type(String filePath, Dimension dimension, double damage, double lifeSpan) {
			image = Program.getImage("entities/weapons/projectiles/"+filePath);
			this.dimension = dimension;
			this.damage = damage;
			this.lifeSpan = (int) (lifeSpan*Program.TICKS_PER_SECOND);
		}
		
		Type(String foldPath, String prefix, int fps, Dimension dimension, double damage, double lifeSpan) {
			ani = new Animation("entities/weapons/projectiles/"+foldPath,prefix,fps);
			ani.randomize();
			this.dimension = dimension;
			this.damage = damage;
			this.lifeSpan = (int) (lifeSpan*Program.TICKS_PER_SECOND);
		}
		
		public Image getImage() {
			if (ani != null) {
				ani.update();
				return ani.getCurrentFrame();
			} else {
				return image;
			}
		} 
	}

	//override the destroy method to also destroy the damage box
	@Override
	public void destroy() {
		if (this.type == Type.JAVELIN) {
			//summon the item
			Item i = new Javelin();
			ItemEntity ie = new ItemEntity(i,getCenterX(),getCenterY());
			this.getGame().entities().add(ie);
		} else if (this.type == Type.SHURIKEN) {
			Item i = new Shuriken();
			ItemEntity ie = new ItemEntity(i,getCenterX(),getCenterY());
			this.getGame().entities().add(ie);
		}
		damageBox.destroy();
		super.destroy();
	}
	
	@Override
	public void individualUpdate() {
		if (this.age >= this.type.lifeSpan) 
			this.destroy();
		if (damageBox == null)
			return;
		if (damageBox.isDestroyed())
			this.destroy();
		
		double angle = getVelocity().radian;
		switch (type) {
		case FIRE:
			this.speedMultiplier = 0.8-this.age/this.lifeSpan;
			angle+=((double)age/5);
			break;
		case ROCK:
			angle+=((double)age/3);
			break;
		default:
			break;
		}
		
		//note that the 'velocity' of the damage box is not its actually velocity
		damageBox.setVelocity(new Vector2D(0.1,angle,Vector2D.FORM_BY_RADIAN));
		damageBox.setPosition(getPosition().copy()); //damage box should always be with the projectile
	
		this.setRotation(angle);
	}

	@Override
	public Image entityImage() {
		BufferedImage i = newImage();
		i.getGraphics().drawImage(type.getImage(), 0, 0, i.getWidth(), i.getHeight(), null);
		return i;
	}
	
	public Entity replicate() {
		return new Projectile(type,position.x,position.y,velocity,null);
	}
}
