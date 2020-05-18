package game;

import items.Inventory;
import items.Item;
import main.Program;
import misc.Graphics2;
import misc.Mouse;
import entities.ItemEntity;
import entities.player.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerInventoryGUI extends GUI {

	private Player player;
	private Inventory inventory;
	
	public PlayerInventoryGUI(Player player) {
		super();
		this.player = player;
		this.inventory = player.getInventory();
		hotbar = inventory.getHotbarImage();
		inv = inventory.getInventoryImage();
		equip = inventory.getEquipmentImage();
		
		width = (int)(widthPercent*img.getWidth());
		
		hotbarHeight = (int)((double)width/hotbar.getWidth()*hotbar.getHeight());
		hotbarWidth = width;
		hotbarX = img.getWidth()/2-hotbarWidth/2;
		hotbarY = img.getHeight()-img.getHeight()/5;
		
		invWidth = width;
		invHeight = (int)((double)width/inv.getWidth()*inv.getHeight());
		invX = img.getWidth()/2-invWidth/2;
		invY = img.getHeight()-img.getHeight()/5-50-invHeight;
		
		equipWidth = (int)((double)equip.getWidth()/hotbar.getWidth()*width);
		equipHeight = (int)(((double)equip.getHeight()*equipWidth)/equip.getWidth());
		equipX = img.getWidth()/2-equipWidth/2;
		equipY = invY-equipHeight-20;
	}

	@Override
	public void update() {
		//look for mouse info..
		if (Mouse.mouseDown() && !mouseDown) 
			click();
		mouseDown = Mouse.mouseDown();
	}
	
	private boolean mouseDown = false;
	
	private Item heldItem = null;
	private int heldCount = 0;
	
	private void click() {
		/*
		 * if the mouse is within a boundary either select, add, or place the slot
		 * 		*deal with individual sections : hotbar, equipment, and inventory
		 * else throw the current item count.
		 */
		int index = -1;
		int mx = Mouse.getXOnDisplay(), my = Mouse.getYOnDisplay();
		int cellWidth = hotbarWidth/inventory.getHotbarLength();
		if (mx > hotbarX && mx < hotbarX+hotbarWidth && my > hotbarY && my < hotbarY+hotbarHeight) { //mouse on hotbar
			//now convert to x, y on the hotbar image
			int hx = mx-hotbarX, hy = my-hotbarY;
			//we only really care about the x value here..
			index = hx/cellWidth;
		} else if (mx > invX && mx < invX+invWidth && my > invY && my < invY+invHeight) { //mouse on inventory
			int hx = mx-invX, hy = my-invY;
			//and since the cells are squares the cellHeight should be the same..
			int x = hx/cellWidth, y = hy/cellWidth;
			index = y*9+x+inventory.getHotbarLength();
		} else if (mx > equipX && mx < equipX+equipWidth && my > equipY && my < equipY+equipHeight) { //mouse on equipment
			int hx = mx-equipX, hy = my-equipY;
			index = hx/cellWidth;
			if (index == 0) {
				index = hy/cellWidth+inventory.getSize()-inventory.getEquipmentLength();
			} else if (index == 1) {
				index = inventory.getSize()-1;
			}
		} 
		else { //mouse not on anything .. throw the selected item item
			if (heldItem != null && heldCount > 0) { //only if you are actually holding an item
				for (int i = 0; i < heldCount; i++) {
					ItemEntity item = new ItemEntity(heldItem,player.getX(),player.getY()+player.getHeight()+Math.random()*0.2);
					player.getGame().entities().add(item);
				}
				heldItem = null;
				heldCount = 0;
				return;
			}
		}
		
		if (index > -1) { //then we selected a slot and we need to handle that..
			if (heldItem == null) { //then we need to set the current held item to the selected
				heldItem = inventory.getItem(index);
				heldCount = inventory.getCount(index);
				inventory.set(index, null, 0);
			} else { //drop the item
				Item slotItem = inventory.get(index);
				if (slotItem == null) {
					inventory.set(index, heldItem, heldCount);
					heldItem = null;
					heldCount = 0;
					return;
				}
				if (slotItem.getID().equals(heldItem.getID())) {
					//combine the counts
					int count = inventory.getCount(index)+heldCount;
					//count to put in
					if (count > slotItem.maxCount()) {
						heldCount = count-slotItem.maxCount();
						heldItem = slotItem;
						count = slotItem.maxCount();
					} else {
						heldItem = null;
					}
					inventory.getCounts()[index] = count;
				} else { //items are different.. so just swap them
					Object[] temp = inventory.set(index, heldItem, heldCount);
					heldItem = (Item)temp[0];
					heldCount = (Integer)temp[1];
				}
			}
		}
	}

	private BufferedImage img = this.getImage();
	private BufferedImage hotbar;
	private BufferedImage inv;
	private BufferedImage equip;
	private double widthPercent = 0.7;
	private	int width;
	private	int hotbarHeight, hotbarWidth, hotbarX, hotbarY;
	private int invWidth, invHeight, invX, invY;
	private int equipWidth, equipHeight, equipX, equipY;
	/*
	 * THIS DRAWS THE FULL INVENTORY GUI.. INCLUDING HOTBAR, INVENTORY, AND ARMOR/EQUIPMENT ITEMS 
	 * Uses methods of the inventory class that can give an image of particular parts
	*/
	public void repaint() {
		this.clear();
		Graphics g = this.getGraphics();
		//draw the hotbar
		hotbar = inventory.getHotbarImage();
		g.drawImage(hotbar, hotbarX, hotbarY, hotbarWidth, hotbarHeight, null);
		//draw the inventory
		inv = inventory.getInventoryImage();
		g.drawImage(inv, invX, invY, invWidth, invHeight, null);
		//draw the equipment
		equip = inventory.getEquipmentImage();
		g.drawImage(equip, equipX, equipY, equipWidth, equipHeight, null);
		//if the selected item is not null then draw it at the cursor
		if (heldItem != null) {
			int mx = Mouse.getXOnDisplay();
			int my = Mouse.getYOnDisplay();
			Image itemImage = heldItem.getIconImage();
			Image numberImage = Graphics2.getPixelatedNumber(heldCount, Color.YELLOW);
			BufferedImage img = new BufferedImage(8,8,BufferedImage.TYPE_INT_ARGB);
			img.getGraphics().drawImage(itemImage, 0, 0, 8, 8, null);
			if (heldCount > 1)
				img.getGraphics().drawImage(numberImage, 8-numberImage.getWidth(null), 8-numberImage.getHeight(null), numberImage.getWidth(null), numberImage.getHeight(null), null);
			int width = (int)(0.07*Program.DISPLAY_WIDTH), height = (int)(0.07*Program.DISPLAY_HEIGHT);
			g.drawImage(img, mx, my, width, height, null);
		}
	}
}
