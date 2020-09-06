package world;

import java.awt.Color;
import java.awt.image.BufferedImage;

import misc.MathUtils;

public class LightSource extends WorldObject {
	private static final long serialVersionUID = 1L;

	private double power = 0; //how much
	
	public LightSource(double x, double y, double power) {
		super(x,y,0.5,0.5);
		this.power = power;
	}

	public double getPower() {
		return this.power;
	}
	
	public void setPower(double power) {
		this.power = MathUtils.min(power, 0);
	}

	@Override
	public BufferedImage getImage() {
		BufferedImage img = newImage();
		img.getGraphics().setColor(new Color(255,125,0,125));
		img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());
		return newImage();
	}
	
	public String getID() {
		return "light_source";
	}
}
