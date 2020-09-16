package entities;

import misc.*;
import world.WorldObject;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import display.ImageProcessor;
import game.Game;
import items.Inventory;
import items.Item;
import items.armor.Armor;
import entities.animals.Animal;
import entities.enemies.Enemy;
import entities.miscellaneous.*;
import entities.player.Player;
import main.Program;
import world.LightSource;

//information for all entities including 
// players, monsters, npcs, floating items, projectiles etc.

// contains a position and some rudimentary functions that are useful

public abstract class Entity extends WorldObject {
	private static final long serialVersionUID = 1L;
	
	private static Game gameToInitializeTo = null;
	
	public static void setGame(Game game) {
		gameToInitializeTo = game;
	}
	
	protected Vector2D velocity = new Vector2D(0,0,Vector2D.FORM_BY_COMPONENTS);
	protected double maxSpeed = 999999, speedMultiplier = 1;
	protected Health health = new Health(1);
	protected Game game;
	protected Team team;
	protected boolean showHealthBar = true;
	protected Inventory inventory; //entities have a default holding capacity of 3 items
	protected Position itemPosition = new Position(0,0); //relative to the entities x, y
	private double mana = 0, maxMana = 0;
	private double resistance = 0; //0 defelcts no damage and 1 deflects all damage (effectivly invincible)

	private int armorLevel = 0;
	
	private Vector2D[] velocityHistory = new Vector2D[10];
	
	protected int age = 0;
	protected int xpOnDrop = 0;
	
	protected boolean invulernable = false;
	
	protected EntityDrop[] drops;
	
	private LightSource lightSource;
	
	public Statistics statistic = new Statistics();
	
	private ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
	
	public Entity(double x, double y, double width, double height) {
		this (new Position(x,y), new Dimension(width, height));
	}
	
	public Entity(double x, double y, Dimension d) {
		this(new Position(x,y), d);
	}
	
	public Entity(Position p, double width, double height) {
		this(p, new Dimension(width,height));
	}
	
	//main constructor
	public Entity(Position p, Dimension d) {
		super(p,d);
		game = gameToInitializeTo;
		inventory = new Inventory(3,this);
		this.setHasCollision(true);
	}
	
	public enum Team {
		PLAYER("Player"), ENEMY("Enemy"), NPC("NPC"), MISCELLANEOUS("Miscellaneous"), ANIMAL("Animal"), ITEM("Item");
		
		String name;
		
		Team(String name) {
			this.name = name;
		}
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public boolean isSameTeam(Entity e) {
		if (e == null || this.team == null || e.getTeam() == null)
			return false;
		if (this.team == e.getTeam())
			return true;
		return false;
	}
	
	public void setInvulnerable(boolean t) {
		this.invulernable = t;
	}
	
	public void setHealth(Health health) {
		this.health = health;
	}
	
	public void setResistance(double val) {
		this.resistance = MathUtils.clip(val, 0, 1);
	}
	
	public void addResistance(double add) {
		this.setResistance(this.resistance+add);
	}
	
	public double getResistance() {
		return this.resistance;
	}
	
	protected boolean hurted = false;
	private static final double MAX_ARMOR_PROTECTION = 0.8;
	public void hurt(double amount) {
		if (this.invulernable)
			return;
		
		// apply armor protection
		double armorPercent = armorLevel/100.0;
		armorPercent*=MAX_ARMOR_PROTECTION;
		amount = (amount*(1-armorPercent));

		amount = (amount*(1-this.resistance));
		
		this.health.hurt(amount);
		hurted = true;
		
		this.statistic.addDamageTaken(amount);
		if (this.killer != null)
			this.killer.statistic.addDamageDealt(amount);
	}
	
	public boolean heal(double amount) {
		double healed = this.health.heal(amount);
		this.statistic.addHealthHealed(healed);
		if (healed > 0) {
			//add some heart particles
			int particles = (int)MathUtils.clip(amount/3, 1, 3);
			for (int i = 0; i < particles; i++) 
				this.add(Particle.Type.HEART,1.5);
			return true;
		}
		return false;
	}

	public int getArmorLevel() {
		return this.armorLevel;
	}
	
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	public Health getHealth() {
		return health;
	}
	
	public void setMaxSpeed(double max) {
		this.maxSpeed = max;
	}
	
	public Vector2D getVelocity() {
		return this.velocity;
	}
	
	public void setVelocity(Vector2D vector) {
		if (vector.magnitude > this.maxSpeed*this.speedMultiplier)
			vector.setMagnitude(this.maxSpeed*this.speedMultiplier);
		this.velocity = vector;
	}
	
	public void setSpeedMultiplier(double m) {
		this.speedMultiplier = m;
	}
	
	public double getSpeedMultiplier() {
		return this.speedMultiplier;
	}
	
	public void addStatusEffect(StatusEffect.Effect e, int lifeSpan) {
		game.console().log("<c=g><s=i>Applied effect "+e.name+" for "+(double)lifeSpan/Program.TICKS_PER_SECOND+" seconds");
		this.effects.add(new StatusEffect(e,lifeSpan,this));
	}
	
	public void clearVelocity() {
		this.velocity.zero();
	}
	
	private Entity killer = null;
	public void setKiller(Entity killer) {
		this.killer = killer;               
	}
	
	public void update() {
		for (int i = 0; i < effects.size(); i++) {
			StatusEffect e = effects.get(i);
			e.update();
			if (e.notActive()) {
				//remove it
				effects.remove(e);
				i--;
			}
		}
		
		individualUpdate();

		// get armor stuff
		armorLevel = 0;
		for (int index = 36; index < 40; index++) {
			Item piece = this.inventory.get(index);
			if (piece == null || !(piece instanceof Armor))
				continue;
			Armor armPiece = (Armor)piece;
			armorLevel += armPiece.getPiece().protection;
		}
		
		this.addMana(0.25);
		
		this.move();
		velocity.applyAcceleration();
		health.update();
		inventory.update();

		this.correctOutOfWall();
		
		//if we are totally out of the world just kill ourself
		double xBound = game.getWorld().width()/2;
		double yBound = game.getWorld().height()/2;
		if (this.getCenterX() < -xBound || this.getCenterX() > xBound || this.getCenterY() < -yBound || this.getCenterY() > yBound)
			this.destroy();
		
		//statistic handling
		try {
			if (this.dead() && this.getTeam() != Team.PLAYER) {
				if (this instanceof Enemy)
					this.killer.statistic.addMonsterKill();
				else if (this instanceof Animal)
					this.killer.statistic.addAnimalKill();
				else
					this.killer.statistic.addEntityKill();
				this.destroy();
			}
		} catch (NullPointerException e) {
			//do nothing..
		}
		
		//update position/velocity history
		for (int i = velocityHistory.length-1; i > 0; i--) {
			if (velocityHistory[i-1] != null)
				velocityHistory[i] = new Vector2D(velocityHistory[i-1]);
		}
		velocityHistory[0] = getVelocity();
		
		age++;
	}
	
	public Vector2D getVelocityAtFrame(int index) {
		if (index < 0 || index > velocityHistory.length-1) 
			return null;
		return velocityHistory[index];
	}
	
	public int getAge() {
		return age;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
	public abstract void individualUpdate();
	
	public void move() {
		if (velocity == null)
			return;
		
		if (path != null) 
			path.walkPath(); //if the path has completed then it will not run this.
		
		//velocity is represented in m/s.. so need to make conversion to m/t (meters per tick)
		double xv = velocity.xv()/Program.TICKS_PER_SECOND, yv = velocity.yv()/Program.TICKS_PER_SECOND;
		
		this.moveX(xv);
		this.moveY(yv);
		
		//lets look at our current position now and the one from before so that we can see how mcuh we moved
		Position previous = this.getPositionAtFrame(1); //go 1 frame back
		if (previous == null)
			return;
		double dx = previous.x-position.x, dy = previous.y-position.y;
		double dist = Math.sqrt(dx*dx+dy*dy);
		this.statistic.addDistanceTraveled(dist);
	}
	
	//accessors, mutators, etc.
	
	public void destroy() {
		if (destroyed) return; // don't destroy if we've already done so

		if (this.dead()) {
			//summon xp and drop items
			dropItems();
			if (xpOnDrop > 0)
				game.entities().add(new XpOrb(getX()+MathUtils.randomInRange(-0.1, 0.1),getY()+MathUtils.randomInRange(-0.1, 0.1),xpOnDrop));
		}  
		
		if (tagged != null) //that is we are tagged by a spawner
			tagged.decrementLivingSpawns();
		
		//summon smoke particles
		if ((this instanceof Animal || this instanceof Enemy || this instanceof Player) && this.distance(this.getGame().player()) < 8)
			for (int i = 0; i < 8; i++) 
				this.add(Particle.Type.WHITE_SMOKE,1.5);
		
		//set the entitylist reference to null. will be removed later
		int index = game.entities().get().indexOf(this);
		if (index >= 0)
			game.entities().get().set(game.entities().get().indexOf(this), null);
		
		destroyed = true;
	}
	
	public void dropItems() {
		if (drops != null)
			for (EntityDrop drop : drops) {
				if (drop == null)
					continue;
				for (int i = 0; i < drop.getHowMany(); i++) {
					double distance = MathUtils.randomInRange(-0.4, 0.4);
					double radian = MathUtils.randomInRange(0, 2*Math.PI);
					Entity e = drop.getEntity();
					double dx = Math.cos(radian)*distance, dy = Math.sin(radian)*distance;
					double x = (dx > 0) ? getCenterX()+dx : getCenterX()-e.getWidth()+dx,
							y = (dy > 0) ? getCenterY()+dy : getCenterY()-e.getHeight()+dy;
					e.setPosition(new Position(x,y));
					game.entities().add(e);
				}
			}
	}
	
	private boolean destroyed = false;
	
	public boolean dead() {
		return health.dead() || destroyed;
	}
	
	private Spawner tagged = null;
	public void tagSpawner(Spawner s) {
		tagged = s;
	}
	
	private Path path = null;
	public void setPath(Path path) {
		this.path = path;
	}
	
	public abstract Entity replicate();
	
	public abstract Image entityImage();
	
	public BufferedImage getImage() {
		BufferedImage i = newImage(pixelWidth(),pixelHeight());
		Graphics g = i.getGraphics();
		double angle = this.getVelocity().getAngleDegree();
		if (angle < 90 || angle > 270)
			g.drawImage(entityImage(), 0, 0, i.getWidth(), pixelHeight(),null);
		else
			g.drawImage(entityImage(), i.getWidth(), 0, -i.getWidth(), pixelHeight(),null);
		
		//show red flash if hit..
		if (hurted) {
			i = ImageProcessor.scaleToColor(i, new Color(255,00,00));
			hurted = false;
		}
		
		//draw the item on the entity
//		Item item = null;
//		if (inventory != null)
//			item = inventory.getSelectedItem();
//		Image disp = null;
//		if (item != null)
//			disp = item.getDisplayImage();
//		if (disp != null) {
//			g.drawImage(disp, (int)(itemPosition.x*World.PIXELS_TO_METER), (int)(itemPosition.y*World.PIXELS_TO_METER), disp.getWidth(null), disp.getHeight(null), null);
//		}
		
		//return
		return i;
	}
	
	public void setLightSourcePower(double power) {
		if (this.lightSource == null) return;
		this.lightSource.setPower(power);
	}
	
	private double knockbackValue = 1; //full knockback
	public void knockBack(double weight, double radian) {
		weight*=knockbackValue;
		this.moveX(Math.cos(radian)*weight);
		this.moveY(Math.sin(radian)*weight);
	}
	
	public void setHealth(double val) {
		this.health.set(val);
	}
	
	public void setKnockbackValue(double val) {
		this.knockbackValue = MathUtils.min(val, 0.0);
	}
	
	public void setMaxMana(int max) {
		this.maxMana = max;
	}
	
	public void setMana(double mana) {
		this.mana = MathUtils.clip(mana, 0, maxMana);
	}
	
	public boolean addMana(double amt) {
		if (this.mana >= this.maxMana)
			return false;
		this.mana = MathUtils.max(this.mana+amt, this.maxMana);
		return true;
	}
	
	public double getMana() {
		return this.mana;
	}
	
	public double getMaxMana() {
		return this.maxMana;
	}
	
	//returns whether or not you used the mana
	//if you do not have enough mana you wont use any and will return false
	public boolean useMana(int amount) {
		if (this.getMana() >= amount) {
			this.setMana(this.getMana()-amount);
			return true;
		}
		return false;
	}
	
	public double getManaPercent() {
		if (this.maxMana == 0)
			return 0;
		else
			return (double)mana/this.maxMana;
	}
	
	public String toString() {
		String s = "E"+game.entities().get().indexOf(this);
		s+=this.getClass().getName();
		if (team != null)
			s+=" team "+team.name;
		return s;
	}
	
	private Color[] loop = {Color.RED,Color.YELLOW,Color.GREEN};
	public BufferedImage getHealthBar() {
		if (!this.showHealthBar)
			return null;
		BufferedImage img = new BufferedImage(50,7,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		Color color = Graphics2.getColorInLoop(loop, health.displayPercent());
		if (this.getHealth().displayPercent() >= 1)
			color = loop[loop.length-1];
		g.setColor(Color.GRAY.darker().darker());
		g.fillRect(1, 1, img.getWidth()-2, img.getHeight()-2);
		g.setColor(color);
		g.fillRect(1, 1, (int)(img.getWidth()*health.displayPercent())-2, img.getHeight()-2);
		return img;
	}
	
	public String getID() {
		String id = this.getClass().getName();
		while (id.indexOf(".") > -1)
			id = id.substring(id.indexOf(".")+1);
		for (int i = 0; i < id.length(); i++) {
			//if the character is capital and its index is above 0.. then make it lowercase and add an underscore
			if (isCapital(id.charAt(i))) {
				//make it not capital
				id = id.substring(0,i)+id.substring(i,i+1).toLowerCase()+id.substring(i+1);
				//add underscore
				if (i > 0)
					id = id.substring(0,i)+"_"+id.substring(i);
			}
		}
		return "entities:"+id;
	}
	
	public void add(Particle.Type partType, double lifeSpan) {
		Particle p = new Particle(partType, getCenterX()+MathUtils.randomInRange(-getWidth()*0.4, getWidth()*0.4), getCenterY()+MathUtils.randomInRange(-getHeight()*0.1,getHeight()*0.1), lifeSpan);
		game.entities().add(p);
	}
	
	private boolean isCapital(char a) {
		String capitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < capitals.length(); i++)
			if (capitals.charAt(i) == a)
				return true;
		return false;
	}
	
	public Game getGame() {
		return game;
	}
}
