package entities.miscellaneous;

import java.awt.*;
import java.awt.image.BufferedImage;

import entities.Entity;
import entities.player.Player;
import items.*;
import items.consumables.*;
import items.weapons.*;
import items.armor.*;
import items.misc.*;
import main.Program;
import misc.MathUtils;

public class Trader extends Entity {
	
	private Product[] products;
	private Preset preset;
	
	public Trader(Preset preset, double x, double y) {
		super(x,y,0.5,1.0);
		this.preset = preset;
		this.products = new Product[preset.products.length];
		for (int i = 0; i < this.products.length; i++) {
			Product p = preset.products[i].replicate();
			this.products[i] = p;
		}
		this.setInvulnerable(true);
		this.showHealthBar = false;
		this.xpOnDrop = 0;
		this.setKnockbackValue(0);
		this.setTeam(Entity.Team.MISCELLANEOUS);
	}
	
	private static class Product {
		private Item item;
		private int price, count;
		
		public Product(Item item, int price, int count) {
			this.item = item;
			this.price = price;
			this.count = count;
		}
		
		public Product replicate() {
			Product p = new Product(this.item,this.price,this.count);
			return p;
		}
	}
	
	public static enum Preset {
		WEAPON_SMITH(
			new Product(new BroadSword(BroadSword.Type.IRON),30,1),
			new Product(new BroadSword(BroadSword.Type.DIAMOND),100,1),
			new Product(new BroadSword(BroadSword.Type.STONE),20,1),
			new Product(new Wand(Wand.Type.FIRE_WAND),120,1),
			new Product(new Wand(Wand.Type.BLUE_WAND),150,1)
			),
		WEAPONS2(
			new Product(new Shuriken(),5,12),
			new Product(new Javelin(),40,2),
			new Product(new Lazer(),300,1)
			),
		BUTCHERER(
			new Product(new Food(Food.Type.COOKED_PORK),10,3),
			new Product(new Food(Food.Type.COOKED_BEEF),12,3),
			new Product(new Food(Food.Type.COOKED_MUTTON),8,3)
			),
		APOTHECIST(
			new Product(new Potion(Potion.Type.REGENERATION),20,1),
			new Product(new Potion(Potion.Type.QUICK_FEET),20,2)
			);
		
		Product[] products;
	
		Preset(Product ... products) {
			this.products = products;
		}
	}
	
	@Override
	public void individualUpdate() {
		
	}

	@Override
	public Entity replicate() {
		Trader t = new Trader(preset,getX(),getY());
		return t;
	}
	
	//returns the amount of money spent.. returns 0 if there was not enough money.. -1 if the item doesn't exist
	public int purchase(Player player, int index) {
		int money = player.getMoney();
		if (index < 0 || index > products.length-1)
			return -1;
		Product p = products[index];
		int price = p.price;
		int count = p.count;
		if (count <= 0)
			return -1;
		if (money < price) //not enough money to buy product
			return 0;
		else {
			player.getInventory().add(p.item);
			p.count--;
			player.addMoney(-p.price);
			return price;
		}
	}
	
	private static final BufferedImage 
		VIEW_MIDDLE = Program.getImage("shop/shopview_middle.png"),
		VIEW_SIDE = Program.getImage("shop/shopview_side.png"),
	    PRICE_MIDDLE = Program.getImage("shop/shopprice_middle.png"),
		PRICE_SIDE = Program.getImage("shop/shopprice_side.png");
	
	public BufferedImage getShopImage() {
		int scale = 5;
		int sideMargin = 2*5;
		int width = 10*products.length*scale+sideMargin+sideMargin;
		int height = 13*scale+5*scale;
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		//draw view sides
		g.drawImage(VIEW_SIDE, 0, 0, 2*scale, 13*scale, null);
		g.drawImage(VIEW_SIDE, width, 0, -2*scale, 13*scale, null);
		for (int i = 0; i < products.length; i++) {
			int x = 2*scale+i*10*scale;
			g.drawImage(VIEW_MIDDLE, x, 0, 10*scale, 13*scale, null);
			//now draw the item image
			Image iImg = products[i].item.getIconImage();
			if (products[i].count == 0) iImg = Program.getImage("items/none.png");
			g.drawImage(iImg, x, 2*scale, 8*scale, 8*scale, null);
			//draw the count below the image now
			Font font = new Font("Arial",Font.BOLD,scale*2);
			g.setFont(font);
			g.setColor(Color.BLACK);
			String s = "x"+products[i].count;
			g.drawString(s,x+7*scale-g.getFontMetrics().stringWidth(s),12*scale-scale/2);
		}
		g.drawImage(PRICE_SIDE, 2*scale, 13*scale, 10*scale, 4*scale, null);
		g.drawImage(PRICE_SIDE, width-2*scale, 13*scale, -10*scale, 4*scale, null);
		for (int i = 0; i < products.length-2; i++) {
			g.drawImage(PRICE_MIDDLE, 2*scale+10*scale+i*10*scale, 13*scale, 10*scale, 4*scale, null);
		}
		int balance = this.getGame().player().getMoney();
		for (int i = 0; i < products.length; i++) {
			//now draw the prices.. 
			int price = products[i].price;
			g.setColor(Color.GREEN);
			if (balance < price)
				g.setColor(Color.RED);
			String s = "$"+price;
			g.drawString(s, 2*scale+i*10*scale+4*scale-g.getFontMetrics().stringWidth(s)/2, 15*scale);
		}
		return img;
	}

	public int getNumberOfProducts() {
		return products.length;
	}
	
	private Image img = Program.getImage("entities/misc/trader"+MathUtils.randomInRange(0, 4)+".png");
	@Override
	public Image entityImage() {
		return img;
	}
}
