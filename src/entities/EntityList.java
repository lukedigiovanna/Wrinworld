package entities;

import java.io.Serializable;
import java.util.*;

import entities.miscellaneous.SummoningPortal;
import entities.npc.*;
import entities.player.*;
import misc.MathUtils;
import world.World;

public class EntityList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//stores array lists of certain entity types
	private ArrayList<Entity> entities;
	private World world;
	
	public EntityList(World world) {
		entities = new ArrayList<Entity>();
		this.world = world;
	}
	
	public void add(Entity e) {
		entities.add(e);
	}
	
	public Entity get(int index) {
		return entities.get(index);
	}
	
	public ArrayList<Entity> get() {
		return entities;
	}
	
	public Player player() {
		for (Entity e : entities)
			if (e instanceof Player)
				return (Player)e;
		return null;
	}
	
	public void update() {
		//remove null entities.
		for (int i = 0; i < entities.size(); i++)
			if (entities.get(i) == null) {
				entities.remove(i);
				i--;
			}
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null) 
				entities.get(i).update();
			if (entities.get(i) != null)
				entities.get(i).updateForAll();
		}
		
	}
	
	public void speakNPC() {
		List<NPC> npcs = new ArrayList<NPC>();
		for (Entity e : entities)
			if (e instanceof NPC)
				npcs.add((NPC)e);
		
		//find the npc with the shortest distance.. within a radius of 5
		NPC closest = null;
		double closestDist = 999999999.0;
		double maxDist = 5;
		for (NPC npc : npcs) {
			if (npc.hasSpoken())
				continue;
			double dist = MathUtils.distance(npc.getX(), npc.getY(), player().getX(), player().getY());
			if (dist < maxDist && (closest == null || dist < closestDist)) {
				closest = npc;
				closestDist = dist;
			}
		}
		
		if (closest != null)	
		closest.speak();
	}
	
	private int portalCount = 0;
	public void setPortalCount(int count) {
		this.portalCount = count;
	}

	public void reducePortalCount() {
		this.portalCount--;
	}

	public int portalCount() {
		return this.portalCount;
	}

	/**
	 * Finds the closest portal to the given entity
	 * @param e
	 * @return
	 */
	public Entity closestPortal(Entity e) {
		double closestDistance = 99999;
		SummoningPortal closest = null;
		for (int i = 0; i < entities.size(); i++) {
			if (i >= entities.size())
				break;
			Entity p = entities.get(i);
			if (p instanceof SummoningPortal) {
				double distance = p.distance(e);
				if (distance < closestDistance) {
					closestDistance = distance;
					closest = (SummoningPortal)p;
				}
			}
		}
		return closest;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
}
