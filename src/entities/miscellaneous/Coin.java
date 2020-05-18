package entities.miscellaneous;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import display.ImageProcessor;
import entities.Entity;
import entities.ItemEntity;
import main.Program;

public class Coin extends Entity {
	
	private Type type;
	
	public Coin(double x, double y, Type type) {
		super(x,y,0.25,0.25);
		this.type = type;
		this.setTeam(Entity.Team.MISCELLANEOUS);
		this.setInvulnerable(true);
		this.showHealthBar = false;
		Graphics g = image.getGraphics();
		g.drawImage(type.image, 0, 0, 4, 4, null);
	}
	
	public enum Type {
		BRONZE("bronzecoin.png",1),
		SILVER("silvercoin.png",10),
		GOLD("goldcoin.png",25),
		JEWEL("gem.png",100);
		
		int value;
		Image image;
		
		Type(String imagePath, int value) {
			image = Program.getImage("entities/misc/"+imagePath);
			this.value = value;
		}
	}

	@Override
	public void individualUpdate() {
		//if we are colliding with the player. give the player some money
		if (this.colliding(this.getGame().player())) {
			this.getGame().player().addMoney(type.value);
			this.destroy();
		}
	}

	@Override
	public Entity replicate() {
		return new Coin(getX(),getY(),type);
	}

	private transient BufferedImage image = new BufferedImage(4,4,BufferedImage.TYPE_INT_ARGB);
	@Override
	public Image entityImage() {
		double intensity = Math.sin(age/5.0)*0.4;
		return ImageProcessor.brighten(image, intensity);
	}
}
