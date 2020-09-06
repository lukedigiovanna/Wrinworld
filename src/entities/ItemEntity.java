package entities;

import java.awt.Image;

import items.Item;
import main.Program;

//NOTE THAT ONLY PLAYERS CAN PICK UP ITEM ENTITIES.
public class ItemEntity extends Entity {
	private static final long serialVersionUID = 1L;

	private Item item;
	private double bob = 0.0;
	
	public ItemEntity(Item item, double x, double y) {
		super(x,y,0.5,0.5);
		this.item = item;
		this.showHealthBar = false;
		this.setHealth(new Health(Health.INVULNERABLE));
		this.setTeam(Entity.Team.ITEM);
	}

	@Override
	public void individualUpdate() {
		//if a player is intersecting with it.. at the item to the player's inventory and then delete this 
		if (this.colliding(game.player()) && age > 1*Program.tps()) {
			if (game.player().getInventory().add(item)) 
				this.destroy();
		}
		
		bob = Math.sin(getAge()/10)*0.007;
		
		this.position.y+=bob;
		
		//all item entities have a lifespan of 5 minutes until they are deleted
		if (age > Program.TICKS_PER_SECOND*300)
			this.destroy();
			
		//ok now lets look at grid components around us.. 
		// List<WorldObject> componentsAround = this.getObjectsAround();
		// for (WorldObject o : componentsAround) {
		// 	if (this.colliding(o)) {
		// 		//now cast it to a grid component.. cuz it most likely is one
		// 		GridComponent c = null;
		// 		try {
		// 			c = (GridComponent) o;
		// 		} catch (ClassCastException e) {
		// 			continue;
		// 		}
				
		// 		//if (c.)
		// 	}
		// }
	}

	@Override
	public Entity replicate() {
		ItemEntity e = new ItemEntity(item, position.x, position.y);
		return e;
	}

	@Override
	public Image entityImage() {
		if (item == null)
			return null;
		return item.getIconImage();
	}
}
