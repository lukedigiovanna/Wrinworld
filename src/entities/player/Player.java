package entities.player;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import game.*;
import entities.*;
import entities.miscellaneous.Trader;
import main.Program;
import misc.*;
import misc.Dimension;
import sound.SoundManager;
import animation.Animation;
import display.popup.PopupBar;
import items.*;
import items.armor.Armor;
import items.weapons.*;
import world.*;

public class Player extends Entity {
	private static final long serialVersionUID = 1L;
	
	transient KeyAdapter key;
	transient MouseAdapter mouse;
	
	private Experience experience;
	private boolean[] move = {false,false,false,false}; //wasd ([0] is up [1] is left [2] is down [3] is right
	private int money = 0;
	private boolean sprint = false;
	private int maxStamina = 300, stamina = maxStamina, minStamina = maxStamina/3;
	private int lives = 0;
	
	public Player(double x, double y) {
		super(x, y, new Dimension(0.5,1));
		game.getWorld().add(ls);
		itemPosition = new Position(0.15, 0.4);
		
		this.setMaxMana(100);
		this.setMana(this.getMaxMana());
		
		this.setHasCollision(true);
		
		this.setTeam(Entity.Team.PLAYER);
		
		setHealth(new Health(50));
		
		maxSpeed = 2;
		
		inventory = new Inventory(9*4+5,this);
//		inventory.add(new Misc(Misc.Type.MANA_STAR));
		// inventory.add(new Wand(Wand.Type.ORANGE_WAND));
		// inventory.add(new Wand(Wand.Type.BLUE_WAND));
		// inventory.add(new Wand(Wand.Type.RED_WAND));
		// inventory.add(new Wand(Wand.Type.GREEN_WAND));
//		inventory.add(new Bow());
		// inventory.add(new Wand(Wand.Type.FIRE_WAND));
//		inventory.add(new Javelin());
//		inventory.add(new BroadSword(BroadSword.Type.DIAMOND));
		// inventory.add(new Lazer());
//		for (int i = 0; i < 16; i++)
//			inventory.add(new Shuriken());
		inventory.add(new BroadSword(BroadSword.Type.WOODEN));
		// inventory.add(new Armor(Armor.Piece.IRON_HELMET));
		// inventory.add(new Armor(Armor.Piece.CRYSTAL_CHESTPLATE));
		// inventory.add(new Armor(Armor.Piece.WOODEN_LEGGINGS));
		// inventory.add(new Armor(Armor.Piece.CRYSTAL_BOOTS));
		// inventory.add(new Armor(Armor.Piece.CRYSTAL_HELMET));
		// inventory.add(new Armor(Armor.Piece.TRAVELERS_BOOTS));
		// inventory.add(new Armor(Armor.Piece.CAP_OF_VISION));
		
		experience = new Experience();
		
		mouse = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				//nothin
			}
		};
		
		key = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (Player.this.getGame().console().isCommandActive())
					return;
				switch (e.getKeyCode()) {
				case Controls.MOVE_RIGHT:
					move[3] = true;
					break;
				case Controls.MOVE_LEFT:
					move[1] = true;
					break;
				case Controls.MOVE_UP:
					move[0] = true;
					break;
				case Controls.MOVE_DOWN:
					move[2] = true;
					break;
				case Controls.THROW_ITEM:
					//summon the item.. then remove it from the inventory
					if (inventory.getSelectedItem() == null)
						break;
					ItemEntity item = new ItemEntity(inventory.getSelectedItem(),getX(),getY()+getHeight()+0.1);
					game.entities().add(item);
					inventory.removeSelectedSlot();
					break;
				case Controls.TALK_TO_TRADER:
					//look for the nearest trader.. if they are within 1.5 meters of you, then open up their display
					GUI active = Player.this.getGame().getActiveGUI();
					if (active != null && active.getID().equals("trade_shop")) {
						Player.this.getGame().setGUI("none");
						break;
					}
					Trader closest = null;
					double distance = 9999;
					EntityList ents = Player.this.getGame().entities();
					for (Entity ent : ents.get()) {
						if (ent == null)
							continue;
						double eDist = ent.distance(Player.this);
						if (ent instanceof Trader && eDist < distance) {
							closest = (Trader)ent;
							distance = eDist;
						}
					}
					if (distance <= 1.5) {
						//open up the trader window
						Player.this.getGame().setGUI("trade_shop");
						System.out.println(Player.this.getGame().getActiveGUI().getID());
						((TradeShopGUI)Player.this.getGame().getActiveGUI()).setTrader(closest);
					}
					break;
				//testing
				case KeyEvent.VK_X:
					// Player.this.hurt(5);
					experience.addXP(1);
					break;
				case KeyEvent.VK_SHIFT:
					if (stamina >= minStamina)
						sprint = true;
					break;
				}
				int ind = e.getKeyCode()-49; //49 is the code of 1.. so if ind is between [0,8] then set the inventory hotbar slot to that
				if (ind >= 0 && ind <= 8)
					inventory.setSelectedSlot(ind);
			}
			
			public void keyReleased(KeyEvent e) {
				if (Player.this.getGame().console().isCommandActive())
					return;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_D:
					move[3] = false;
					break;
				case KeyEvent.VK_A:
					move[1] = false;
					break;
				case KeyEvent.VK_W:
					move[0] = false;
					break;
				case KeyEvent.VK_S:
					move[2] = false;
					break;
				case KeyEvent.VK_SHIFT:
					sprint = false;
				}
			}
		};
	}
	
	public int getLives() {
		return this.lives;
	}
	
	public void useLife() {
		this.lives--;
	}
	
	private static final double DEFAULT_LIGHT_SOURCE_POWER = 1;
	private LightSource ls = new LightSource(getCenterX(),getCenterY(),DEFAULT_LIGHT_SOURCE_POWER);
	private boolean wearingTravelersBoots = false;
	private boolean usedItem = false;
	public void individualUpdate() {
		Position p = new Position(getCenterX(),getCenterY());
		p.priority = 2;
		ls.setPosition(p);
		//player drops
		if (beenDeadFor == 1)
			this.statistic.addDeath();
		if (playerDead)
			beenDeadFor++;
		this.drops = new EntityDrop[inventory.getItemCount()];
		int index = 0;
		for (int i = 0; i < inventory.getAllItems().length; i++) 
			if (inventory.get(i) != null) {
				drops[index] = new EntityDrop(1,inventory.getCount(i),inventory.get(i));
				index++;
			}
		
		//using item
		boolean popupDone = (PopupBar.queue.size() > 0 && PopupBar.queue.get(0).spoken());
		Item selectedItem = inventory.getSelectedItem();
		if (game.isMouseDown() && !usedItem && !popupDone && game.getActiveGUI() == null) {
			usedItem = inventory.useSelectedItem();
			if (selectedItem != null && selectedItem.allowsHold())
				usedItem = false;
		} else if (popupDone && game.isMouseDown() && PopupBar.hasNext()) {
			PopupBar.next();
			game.setMouseDown(false);
		}
		else if (!game.isMouseDown()) {
			usedItem = false;
			if (selectedItem != null)
				selectedItem.resetLoad();
		}
		
		// WASD  0 - up, 1 - left, 2 - down, 3 - right
		//determine our speed
		//apply speed multiplier based on held item status
		double sm = 
				(inventory.getSelectedItem() == null || inventory.getSelectedItem().getLoadStatus() == 0) 
				? 
				speedMultiplier : speedMultiplier/4;
		if (idle())
			sprint = false;
		if (sprint && !idle()) 
			stamina-=3;
		else if (idle())
			stamina+=2;
		else
			stamina++;
		stamina = MathUtils.clip(stamina, 0, maxStamina);
		if (stamina == 0)
			sprint = false;
		double speed = maxSpeed*sm;
		if (!sprint)
			speed/=2;
	
		velocity.zero();
		if (move[0])
			velocity.addYV(-speed);
		if (move[1])
			velocity.addXV(-speed);
		if (move[2])
			velocity.addYV(speed);
		if (move[3])
			velocity.addXV(speed);

		// handle special armor stuff
		// cap of vision
		Item helmet = inventory.get(36);
		if (helmet != null && ((Armor)helmet).getPiece() == Armor.Piece.CAP_OF_VISION) 
			this.ls.setPower(DEFAULT_LIGHT_SOURCE_POWER * 3);
		else
			this.ls.setPower(DEFAULT_LIGHT_SOURCE_POWER);
		// traveler's boots
		Item boots = inventory.get(39);
		if (!wearingTravelersBoots && boots != null && ((Armor)boots).getPiece() == Armor.Piece.TRAVELERS_BOOTS) {
			this.setSpeedMultiplier(this.getSpeedMultiplier() + 0.5);
			wearingTravelersBoots = true;
		} else if (wearingTravelersBoots) { // then we must have taken them off!
			this.setSpeedMultiplier(this.getSpeedMultiplier() - 0.5);
			wearingTravelersBoots = false;
		}
	}
	
	public double staminaPercent() {
		return (double)stamina/maxStamina;
	}

	private Animation run_forward = new Animation("entities/player/run_forward","run_forward_",10),
					run_side = new Animation("entities/player/run_side","run_side_",10),
					run_back = new Animation("entities/player/run_back","run_back_",10);
	
	public Image entityImage() {
		BufferedImage img = newImage();
		Graphics g = img.getGraphics();
		
		if (move == null)
			return null;
		
		if (move[3]) {
			run_side.update();
			g.drawImage(run_side.getCurrentFrame(), 0, 0, img.getWidth(), img.getHeight(), null);
		} else if (move[1]) {
			run_side.update();
			g.drawImage(run_side.getCurrentFrame(), 0, 0, img.getWidth(), img.getHeight(), null);
		} else if (move[0]) {//moving up.. use run_back animtion
			run_back.update();
			g.drawImage(run_back.getCurrentFrame(), 0, 0, img.getWidth(), img.getHeight(), null);
		} else if (move[2]) { //moving down.. use run_forward animation
			run_forward.update();
			g.drawImage(run_forward.getCurrentFrame(), 0, 0, img.getWidth(), img.getHeight(), null);
		} else //idling
			g.drawImage(Program.getImage("entities/player/run_forward/run_forward_0.png"), 0, 0, img.getWidth(), img.getHeight(), null);
		
		return img;
	}
	
	public void reset() {
		this.setPosition(new Position(0,0));
		this.setHealth(new Health(this.getHealth().maxHealth));
		this.getInventory().clear();
		for (int i = 0; i < move.length; i++)
			move[i] = false;
		playerDead = false;
		beenDeadFor = 0;
	}
	
	public void addMoney(int amount) {
		this.money+=amount;
		if (amount > 0)
			this.statistic.addEarnings(amount);
		SoundManager.playSound("coindrop");
	}
	
	public int getMoney() {
		return money;
	}

	private boolean playerDead = false;
	private int beenDeadFor = 0;
	public boolean dead() {
		if (getHealth().dead() && !playerDead) {
			playerDead = true;
			dropItems();
		}
		if (playerDead) 
			hurted = true;
		return false;
	}
	
	public void hurt(double amount) {
		super.hurt(amount);
		SoundManager.playSound("playerhurt");
	}
	
	public boolean sendToDeathScreen() {
		if (beenDeadFor > 4) {
			useLife();
			return true;
		}
		return false;
	}
	
	public boolean idle() {
		return !(move[0] || move[1] || move[2] || move[3]);
	}
	
	public KeyListener getKey() {
		return key;
	}
	
	public MouseListener getMouse() {
		return mouse;
	}
	
	public Experience getExperience() {
		return experience;
	}
	
	public Entity replicate() {
		return null; //dont replicate players
	}
}