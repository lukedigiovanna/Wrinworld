package entities.miscellaneous;

import java.awt.Image;

import entities.Entity;
import main.Program;

public class ImageHolder extends Entity {
	private static final long serialVersionUID = 1L;

	private int lifeSpan;
	private Image image;
	
	public ImageHolder(String fp, double x, double y, double width, double height, int lifeSpan) {
		this(Program.getImage(fp),x,y,width,height,lifeSpan);
	}
	
	public ImageHolder(Image image, double x, double y, double width, double height, int lifeSpan) {
		super(x,y,width,height);
		this.lifeSpan = lifeSpan;
		this.image = image;
		this.team = Entity.Team.MISCELLANEOUS;
		this.setInvulnerable(true);
		this.showHealthBar = false;
	}

	@Override
	public void individualUpdate() {
		if (this.lifeSpan <= this.age)
			this.destroy();
	}

	@Override
	public Entity replicate() {
		return new ImageHolder(image,position.x,position.y,dimension.width,dimension.height,lifeSpan);
	}

	@Override
	public Image entityImage() {
		return image;
	}
	
	
}
