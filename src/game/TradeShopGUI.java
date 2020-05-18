package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.miscellaneous.Trader;
import main.Program;
import misc.Mouse;

public class TradeShopGUI extends GUI {

	private Trader currentTrader;
	
	public TradeShopGUI() {
		super();
		System.out.println(this.getID());
	}
	
	public void setTrader(Trader trader) {
		this.currentTrader = trader;
		this.clear();
	}

	boolean clicked = false;
	@Override
	public void update() {
		//check for when the mouse is down and on an item...
		int mx = Mouse.getXOnDisplay()-x, my = Mouse.getYOnDisplay()-y;
		if (mx > 0 && my > 0 && my < h && Mouse.isLeftButtonDown() && !clicked) {
			int index = mx/(w/currentTrader.getNumberOfProducts());
			int spent = this.currentTrader.purchase(this.currentTrader.getGame().player(), index);
			if (spent == 0)
				this.currentTrader.getGame().console().log("<c=y><s=b>[TRADER]<s=p><c=r> You do not enough money for that!");
			else if (spent == -1)
				this.currentTrader.getGame().console().log("<c=y><s=b>[TRADER]<s=p><c=o> I don't have anymore of that!");
			else
				this.currentTrader.getGame().console().log("<c=y><s=b>[TRADER]<s=p><c=g> Used $"+spent+"!");
			clicked = true;
		} else if (!Mouse.isLeftButtonDown()){
			clicked = false;
		}
	}

	int h, w, x, y;
	@Override
	public void repaint() {
		//this.clear();
		Graphics g = this.getGraphics();
		if (currentTrader == null)
			return;
		BufferedImage img = currentTrader.getShopImage();
		int iw = img.getWidth(), ih = img.getHeight();
		double hPerc = 0.2;
		h = (int)(hPerc*Program.DISPLAY_HEIGHT);
		w = (int)((iw*h)/ih);
		x = Program.DISPLAY_WIDTH/2-w/2; 
		y = Program.DISPLAY_HEIGHT/2-h/2;
		g.drawImage(img,x,y,w,h,null);
	}
}
