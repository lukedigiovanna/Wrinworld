package entities.miscellaneous;

import java.awt.Image;

import entities.Entity;
import entities.EntityDrop;
import entities.player.Player;
import items.Item;
import items.consumables.*;
import items.armor.*;
import items.weapons.*;
import main.Program;
import misc.MathUtils;

import game.GameController;

import java.awt.image.BufferedImage;

public class LootBox extends Entity {
    private static final long serialVersionUID = 1L;
    
    private static class PossibleItem {
        public Item item;
        public double rarity;

        public PossibleItem(Item item, double rarity) {
            this.item = item;
            this.rarity = rarity;
        }
    }

    public static final PossibleItem[] POSSIBLE_ITEMS = {
        // FOODS
        new PossibleItem(new Food(Food.Type.BERRIES), 0.9),
        new PossibleItem(new Food(Food.Type.COOKED_BEEF), 0.6),
        new PossibleItem(new Food(Food.Type.RAW_BEEF), 0.8),
        new PossibleItem(new Food(Food.Type.COOKED_PORK), 0.6),
        new PossibleItem(new Food(Food.Type.RAW_PORK), 0.8),
        new PossibleItem(new Food(Food.Type.COOKED_MUTTON), 0.6),
        new PossibleItem(new Food(Food.Type.RAW_MUTTON), 0.8),
        new PossibleItem(new Food(Food.Type.ZOMBIE_MEAT), 0.95),
        // WEAPONS
        new PossibleItem(new Bow(), 0.6),
        new PossibleItem(new BroadSword(BroadSword.Type.WOODEN), 0.8),
        new PossibleItem(new BroadSword(BroadSword.Type.STONE), 0.5),
        new PossibleItem(new BroadSword(BroadSword.Type.IRON), 0.3),
        new PossibleItem(new BroadSword(BroadSword.Type.DIAMOND), 0.1),
        new PossibleItem(new Javelin(), 0.6),
        new PossibleItem(new Shuriken(), 0.9),
        new PossibleItem(new Lazer(), 0.001),
        new PossibleItem(new Wand(Wand.Type.ORANGE_WAND), 0.4),
        new PossibleItem(new Wand(Wand.Type.BLUE_WAND), 0.15),
        new PossibleItem(new Wand(Wand.Type.RED_WAND), 0.05),
        new PossibleItem(new Wand(Wand.Type.GREEN_WAND), 0.01),
        // POTIONS
        new PossibleItem(new Potion(Potion.Type.HEAL), 0.4),
        new PossibleItem(new Potion(Potion.Type.REGENERATION), 0.4),
        new PossibleItem(new Potion(Potion.Type.QUICK_FEET), 0.4),
        // ARMOR
        new PossibleItem(new Armor(Armor.Piece.WOODEN_BOOTS), 0.7),
        new PossibleItem(new Armor(Armor.Piece.WOODEN_LEGGINGS), 0.6),
        new PossibleItem(new Armor(Armor.Piece.WOODEN_CHESTPLATE), 0.6),
        new PossibleItem(new Armor(Armor.Piece.WOODEN_HELMET), 0.7),
        new PossibleItem(new Armor(Armor.Piece.IRON_BOOTS), 0.4),
        new PossibleItem(new Armor(Armor.Piece.IRON_LEGGINGS), 0.3),
        new PossibleItem(new Armor(Armor.Piece.IRON_CHESTPLATE), 0.3),
        new PossibleItem(new Armor(Armor.Piece.IRON_HELMET), 0.4),
        new PossibleItem(new Armor(Armor.Piece.CRYSTAL_BOOTS), 0.1),
        new PossibleItem(new Armor(Armor.Piece.CRYSTAL_LEGGINGS), 0.05),
        new PossibleItem(new Armor(Armor.Piece.CRYSTAL_CHESTPLATE), 0.05),
        new PossibleItem(new Armor(Armor.Piece.CRYSTAL_HELMET), 0.1),
        new PossibleItem(new Armor(Armor.Piece.CAP_OF_VISION), 0.15),
        new PossibleItem(new Armor(Armor.Piece.TRAVELERS_BOOTS), 0.15),
   };

   private Item[] items;

    public LootBox(double x, double y) {
        super(x, y, 0.5, 0.5);
        this.setInvulnerable(true);
    }

    private void setDrops(Player player) {
        // use the player's experience to make better drops more likely when the player has more XP
        double rarityScale = Math.max(player.getExperience().getLevel()/5,1);
        items = new Item[MathUtils.randomInRange(1, 4)];
        for (int i = 0; i < items.length; i++) {
            int randIndex = (int)(Math.random() * POSSIBLE_ITEMS.length);
            if (POSSIBLE_ITEMS[randIndex].rarity * rarityScale > Math.random()) 
                items[i] = POSSIBLE_ITEMS[randIndex].item.replicate();
            else {
                i--;
                continue;
            }
        }
        this.drops = new EntityDrop[items.length];
        for (int i = 0; i < this.items.length; i++) {
            this.drops[i] = new EntityDrop(1.0, 1, items[i]);
        }
    }

	@Override
	public void individualUpdate() {
		double bob = Math.sin(getAge()/10)*0.007;
        this.position.y+=bob;
        
        if (this.colliding(GameController.game().player())) {
            this.setDrops(GameController.game().player());
            this.dropItems();
            this.destroy();
        }
	}

	@Override
	public Entity replicate() {
		return new LootBox(this.getX(), this.getY());
	}

    private static BufferedImage image = Program.getImage("entities/lootbox.png");
	@Override
	public Image entityImage() {
		return image;
	}
}
