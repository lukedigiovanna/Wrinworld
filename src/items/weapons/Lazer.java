package items.weapons;

import java.awt.Image;

import entities.damageBox.DamageBox;
import entities.miscellaneous.ImageHolder;
import main.Program;
import misc.Dimension;
import misc.Position;

public class Lazer extends Weapon {
	private static final long serialVersionUID = 1L;

	public Lazer() {
		super("Lazer");
		this.cooldownPeriod = 1;
		this.allowHold();
	}

	@Override
	public void use() {
		//shoots out a line of damage boxes..
		double distance = 4;
		double radian = this.getGame().angleToMouse();
		for (double d = 0; d < distance; d+=0.2) {
			double x = Math.cos(radian)*d+this.getOwner().getX();
			double y = Math.sin(radian)*d+this.getOwner().getY();
			DamageBox b = new DamageBox(new Position(x,y), new Dimension(0.2,0.2), 2, getOwner());
			b = b.lifeSpan(1);
			this.getGame().entities().add(b);
			String[] images = {"lazer1","lazer2","lazer3"};
			ImageHolder i = new ImageHolder("entities/misc/lazer/"+images[(int)(Math.random()*images.length)]+".png",x,y,0.25,0.25,1);
			this.getGame().entities().add(i);
		}
	}

	private transient Image disp = Program.getImage("items/weapons/lazer.png");
	@Override
	public Image getDisplayImage() {
		return disp;
	}

	Image ico = Program.getImage("items/weapons/lazericon.png");
	@Override
	public Image getIconImage() {
		return ico;
	}

	@Override
	public boolean used() {
		return false;
	}
}
