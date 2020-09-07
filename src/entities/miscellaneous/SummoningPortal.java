package entities.miscellaneous;

import java.awt.Image;

import entities.*;
import main.Program;

import java.util.*;

import misc.*;

public class SummoningPortal extends Entity {
	private static final long serialVersionUID = 1L;
	
	/*
	 * holds a list of entities and their summoning frequency
	 */
	
	private List<Entity> entities;
	private List<Double> chances;
	
	private List<Entity> summonedEntities;
	
	public SummoningPortal(double x, double y, double health) {
		super(x,y,1.5,1.5);
		initList();
		this.team = Team.ENEMY;
		this.setHealth(new Health(health));
		this.setKnockbackValue(0);
		this.xpOnDrop = MathUtils.randomInRange(10, 30);
		EntityDrop[] drops = {
			new EntityDrop(0.4,6, new Coin(0,0,Coin.Type.BRONZE)),
			new EntityDrop(0.3,2, new Coin(0,0,Coin.Type.SILVER)),
			new EntityDrop(0.2,2, new Coin(0,0,Coin.Type.GOLD)),
			new EntityDrop(0.025,1, new Coin(0,0,Coin.Type.JEWEL)),
		};
		this.drops = drops;
	}
	
	public SummoningPortal setEntities(Entity ... entities) {
		for (Entity e : entities)
			this.entities.add(e);
		return this;
	}
	
	public SummoningPortal setChances(double ... doubles) {
		for (double d : doubles)
			this.chances.add(d);
		return this;
	}
	
	private void initList() {
		entities = new ArrayList<Entity>();
		chances = new ArrayList<Double>();
		summonedEntities = new ArrayList<Entity>();
	}

	private double spinsPerSecond = 0.6;
	private double spinSpeed = spinsPerSecond*(Math.PI*2/Program.TICKS_PER_SECOND);
	@Override
	public void individualUpdate() {
		this.setRotation(this.getRotation()+spinSpeed);
		
		for (int i = 0; i < entities.size(); i++) {
			if (Math.random() < chances.get(i)) {
				//summon the entity
				Entity e = entities.get(i).replicate();
				e.setPosition(this.getCenterX()-e.getWidth()/2,this.getCenterY()-e.getHeight()/2);
				Path p = new Path(e);
				double radian = Math.random()*2*Math.PI;
				double distance = 2.0;
				for (double d = 0; d < distance; d+=0.2) {
					double s = 3*(1-d/distance);
					s = MathUtils.min(s, 0.1);
					p.add(new Point(e.getX()+Math.cos(radian)*d,e.getY()+Math.sin(radian)*d),s);
				}
				e.setPath(p);
				this.getGame().entities().add(e);
			}
		}
		
		for (int i = 0; i < summonedEntities.size(); i++) {
			if (summonedEntities.get(i).dead()) {
				summonedEntities.remove(i); //remove the reference.. it doesn't matter
				i--;
			}
		}
	}

	public String toString() {
		String s = "[x="+this.getX()+",y="+this.getY()+"]";
		s+="\n";
		for (int i = 0; i < entities.size(); i++) {
			s+=entities.get(i).getID()+" at "+chances.get(i)+"%\n";
		}
		return s;
	}
	
	@Override
	public Entity replicate() {
		Entity e = new SummoningPortal(this.getX(),this.getY(),this.getHealth().maxHealth);
		return e;
	}

	@Override
	public Image entityImage() {
		return Program.getImage("entities/misc/portal.png");
	}

	@Override
	public void destroy() {
		this.game.entities().reducePortalCount();
		super.destroy();
	}
}
