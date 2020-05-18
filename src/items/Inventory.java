package items;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.Serializable;

import entities.Entity;
import entities.player.Player;
import game.GameController;
import main.Program;
import misc.Graphics2;
import sound.SoundManager;

public class Inventory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final BufferedImage TILE_IMAGE = Program.getImage("inventory/tile.png");
	
	public static final int DEFAULT_HOTBAR_LENGTH = 9, DEFAULT_EQUIPMENT_LENGTH = 5;
	//equipment: helmet, chest plate, leggings, boots, and some other special
	
	//holds slots for items for the player or other entities
	private Entity owner;
	private int selectedSlot = 0;
	private Item[] items;
	private int[] itemCounts;
	private int hotbarLength = DEFAULT_HOTBAR_LENGTH; //default of 9
	private int equipmentLength = DEFAULT_EQUIPMENT_LENGTH;
	
	public Inventory(int size, Entity owner) {
		items = new Item[size];
		itemCounts = new int[size];
		this.owner = owner;
	}
	
	public void update() {
		for (Item i : items)
			if (i != null)
				i.update();
	}
	
	//returns an array such that ind[0] = item and ind[1] = item count
	public Object[] set(int index, Item item, int count) {
		Object[] val = {items[index],itemCounts[index]};
		items[index] = item;
		itemCounts[index] = count;
		return val;
	}
	
	public boolean add(Item item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				itemCounts[i] = 1;
				if (items[i] != null)
					items[i].setOwner(owner);
				if (owner instanceof Player) 
					SoundManager.playSound("itempickup");
				return true;
			} else if (items[i].equals(item) && itemCounts[i] < items[i].maxCount) {
				itemCounts[i]++;
				if (owner instanceof Player)
					SoundManager.playSound("itempickup");
				return true;
			}
		}
		return false;
	}
	
	//does not return item stack counts.. rather just the different item slots
	public Item[] getItems() {
		Item[] newItems = new Item[getItemCount()];
		for (int i = 0; i < newItems.length; i++) 
			if (items[i] != null) 
				newItems[i] = items[i];
		return newItems;
	}
	
	//number of slots filled
	public int getItemCount() {
		int count = 0;
		for (int i = 0; i < items.length; i++)
			if (items[i] != null)
				count++;
		return count;
	}
	
	public Item get(int index) {
		if (index < 0 || index >= items.length)
			return null;
		return items[index];
	}
	
	public int has(String id) {
		//returns the count of a certain item id
		int count = 0;
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null && items[i].getID().equals(id))
				count+=itemCounts[i];
		}
		return count;
	}
	
	public boolean useSelectedItem() {
		if (getSelectedItem() == null)
			return false;
		getSelectedItem().load();
		boolean used = getSelectedItem().tryUse(); //only uses if it can
		if (getSelectedItem().used()) {
			itemCounts[selectedSlot]--;
			if (itemCounts[selectedSlot] <= 0)
				items[selectedSlot] = null;
		}
		return used;
	}
	
	public int getCount(int index) {
		if (index < 0 || index >= items.length)
			return 0;
		return itemCounts[index];
	}
	
	public Item getItem(int index) {
		if (index < 0 || index >= items.length)
			return null;
		return items[index];
	}
	
	public Item getSelectedItem() {
		return items[selectedSlot];
	}
	
	public int getSelectedSlot() {
		return selectedSlot;
	}
	
	public int getHotbarLength() {
		return this.hotbarLength;
	}
	
	public void setSelectedSlot(int slot) {
		while (slot < 0)
			slot+=this.hotbarLength;
		slot%=this.hotbarLength;
		this.selectedSlot = slot;
	}
	
	public void removeSlot(int slot) {
		itemCounts[slot]--;
		if (itemCounts[slot] <= 0)
			items[slot] = null;
	}
	
	public void removeSelectedSlot() {
		removeSlot(selectedSlot);
	}
	
	public void swap(int index1, int index2) {
		Item temp = items[index1];
		int tempCount = itemCounts[index1];
		items[index1] = items[index2];
		itemCounts[index1] = itemCounts[index2];
		items[index2] = temp;
		itemCounts[index2] = tempCount;
	}
	
	public Item[] getArray() {
		return items;
	}
	
	public Item[] getHotbarItems() {
		Item[] hotbarItems = new Item[this.hotbarLength];
		for (int i = 0; i < this.hotbarLength; i++)
			hotbarItems[i] = items[i];
		return hotbarItems;
	}
	
	public Item[] getEquipmentItems() {
		return null;
	}
	
	public int getEquipmentLength() {
		return this.equipmentLength;
	}
	
	public int[] getCounts() {
		return itemCounts;
	}
	
	public void clear() {
		for (int i = 0; i < items.length; i++) {
			items[i] = null;
			itemCounts[i] = 0;
		}
	}
	
	public int getSize() {
		return this.items.length;
	}
	
	public BufferedImage getHotbarImage() {
		int width = this.hotbarLength*9+1;
		int height = 10;
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		for (int i = 0; i < this.hotbarLength; i++)
			g.drawImage(getTileImage(i),i*9,0,10,10,null);
		//outline the selected slot
		int selectedX = this.selectedSlot*9;
		g.setColor(Color.CYAN);
		g.drawRect(selectedX, 0, 9, 9);
		return img;
	}
	
	public BufferedImage getInventoryImage() {
		//box of items not in hotbar or equipment
		int index1 = this.hotbarLength, index2 = items.length-this.equipmentLength, size = index2-index1;
		int itemWidth = 9, itemHeight = size/itemWidth;
		int width = itemWidth*9+1, height = itemHeight*9+1;
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		for (int i = index1; i < index2; i++) {
			int x = (i-index1)%9*9, y = (i-index1)/9*9;
			g.drawImage(this.getTileImage(i), x, y, 10, 10, null);
		}
		return img;
	}
	
	public BufferedImage getEquipmentImage() {
		//a vertical box with the armor and another 
		int index1 = this.items.length-this.equipmentLength, index2 = this.items.length-1;
		int width = 2*9+1, height = (this.equipmentLength-1)*9+1;
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		for (int i = 0; i < index2-index1; i++) {
			int ind = index1+i;
			g.drawImage(this.getTileImage(ind),0,9*i,10,10,null);
		}
		g.drawImage(this.getTileImage(this.items.length-1), width-10, height-10, 10, 10, null);
		return img;
	}
	
	private BufferedImage getTileImage(int index) {
		BufferedImage img = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.drawImage(TILE_IMAGE, 0, 0, 10, 10, null);
		Item item = items[index];
		int count = itemCounts[index];
		if (item != null) {
			//draw it and how many
			g.drawImage(item.getIconImage(), 1, 1, 8, 8, null);
			BufferedImage countImage = Graphics2.getPixelatedNumber(count, Color.YELLOW);
			if (count > 1)
				g.drawImage(countImage, 10-countImage.getWidth(), 9-countImage.getHeight(), countImage.getWidth(), countImage.getHeight(), null);
		}
		return img;
	}
}
